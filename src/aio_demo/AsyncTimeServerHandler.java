package aio_demo;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by cynric on 17-6-7.
 */
public class AsyncTimeServerHandler implements Runnable {

    private int port ;
    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port){
        this.port = port;
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("the time server lanche on port :" + port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        try {
            latch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public  void doAccpet(){
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }
}
