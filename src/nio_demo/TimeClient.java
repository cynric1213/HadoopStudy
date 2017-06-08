package nio_demo;

/**
 * Created by cynric on 17-6-7.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null &&args.length >0){

            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //采用默认值
            }
            for (int i = 0; i < 100; i++) {
                String name = "Timeclient-" + i;
                new Thread(new TimeClientHandle("127.0.0.1",port),name).start();
            }

        }
    }
}
