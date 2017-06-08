package nio_demo;

/**
 * Created by cynric on 17-6-7.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null &&args.length >0){

            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //采用默认值
            }

            MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
            new Thread(timeServer,"NIO-server-001").start();
        }
    }
}
