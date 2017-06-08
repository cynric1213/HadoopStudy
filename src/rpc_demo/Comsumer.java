package rpc_demo;

/**
 * Created by cynric on 17-6-6.
 */
public class Comsumer {

    public static void main(String[] args) throws InterruptedException {
        HelloService service = RpcFramework.refer(HelloService.class,"192.168.31.129",1234);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String hello = service.hello("world" + i);
            System.out.println(hello);
            Thread.sleep(1000);
        }
    }
}
