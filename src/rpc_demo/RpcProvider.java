package rpc_demo;

/**
 * Created by cynric on 17-6-6.
 */
public class RpcProvider {
    public static void main(String[] args) throws Exception {
        HelloService hello = new HelloServiceImpl();
        RpcFramework.export(hello,1234);
    }
}
