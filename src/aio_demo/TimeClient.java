package aio_demo;

/**
 * Created by cynric on 17-6-8.
 */
public class TimeClient {

    public static void main(String[] args) {
        int port ;
        try {
            port = Integer.valueOf(args[0]);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        new Thread( new AsyncTimeTimeHandler("127.0.0.1",port), "aio-001").start();
    }
}
