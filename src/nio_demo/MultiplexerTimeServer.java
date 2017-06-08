package nio_demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by cynric on 17-6-7.
 */
public class MultiplexerTimeServer implements Runnable{
    private Selector selector ;
    private ServerSocketChannel servChannel;
    private volatile boolean stop = false;

    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking(false);
            servChannel.socket().bind(new InetSocketAddress(port),1024);
            servChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("The time server is start in port :" + port);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }


    @Override
    public void run() {

        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;

                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch(Exception t){
                        if (null != key){
                            key.cancel();
                            if (null != key.channel()){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != selector)
            try {
                selector.close();
            }catch (IOException e){
                e.printStackTrace();
            }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            if (key.isAcceptable()){
                //accpet the new connection
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }

            if (key.isReadable()){

                //read the data
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer readbuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readbuffer);

                if (readBytes > 0){
                    readbuffer.flip();
                    //readbuffer.remaining()返回读到的长度
                    byte[] bytes = new byte[readbuffer.remaining()];
                    //将读到的数据放到bytes数组中
                    readbuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("The time server receive order :" + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                    doWrite(sc,currentTime);
                }else if (readBytes < 0){
                    key.cancel();
                    sc.close();
                }else
                    ;//没有读到东西
            }
        }
    }

    private void doWrite(SocketChannel channel ,String response) throws IOException{
        if (null != response && response.trim().length() > 0){
            byte [] bytes = response.getBytes();
            System.out.println(bytes.length);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer);
        }
    }
}
