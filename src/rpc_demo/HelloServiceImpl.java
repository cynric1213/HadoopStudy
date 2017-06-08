package rpc_demo;

/**
 * Created by cynric on 17-6-6.
 */
public class HelloServiceImpl implements HelloService{

    @Override
    public String hello(String name) {
        return "hello " + name;
    }
}
