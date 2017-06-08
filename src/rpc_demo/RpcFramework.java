package rpc_demo;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cynric on 17-6-6.
 */
public class RpcFramework {

    public static void export(final Object service,int port) throws Exception{
        if (null == service)
            throw new IllegalArgumentException("service instance is null");
        if (port<=0 || port>=65535)
            throw new IllegalArgumentException("Invalid port :" + port);

        System.out.println("export service:" +service.getClass().getName() + " on port:" + port);

        ServerSocket server = new ServerSocket(port);

        for (;;){
            final Socket s = server.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String methodeName;
                    Class<?>[] parameterTypes;
                    Object[] arguments;

                    ObjectOutput output = null;
                    Method method = null;
                    Object result = null;

                    try (ObjectInputStream input = new ObjectInputStream(s.getInputStream())) {
                        methodeName = input.readUTF();
                        parameterTypes = (Class<?>[]) input.readObject();
                        arguments = (Object[]) input.readObject();

                        //拿到方法名 方法参数类型 以及方法参数， 下面准备在服务器端调用方法，并写还给客户端

                        System.out.println(service.getClass().getName());
                        method = service.getClass().getMethod(methodeName,parameterTypes);
                        result = method.invoke(service,arguments);

                        output = new ObjectOutputStream(s.getOutputStream());
                        output.writeObject(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public static <T> T refer(final Class<T> interfaceClass, final String host, final int port ){
        if (null == interfaceClass)
            throw new IllegalArgumentException("interfaceClass == null");

        if (!interfaceClass.isInterface())
            throw new IllegalArgumentException("must be an interface class");

        if (host == null || host.length()==0)
            throw new IllegalArgumentException("host == null");

        if (port<=0 || port>=65535)
            throw new IllegalArgumentException("Invalid port :" + port);


        System.out.println("get remote service :" + interfaceClass + " from server " + host + ":" + port);

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               Socket socket = new Socket(host,port);

                try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                    output.writeUTF(method.getName());
                    output.writeObject(method.getParameterTypes());
                    output.writeObject(args);

                    try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                        Object result = in.readObject();

                        if (result instanceof Throwable){
                            throw (Throwable)result;
                        }

                        return result;
                    }
                }


            }
        });

    }

}
