package aio_demo;

import java.nio.channels.CompletionHandler;

/**
 * Created by cynric on 17-6-8.
 */
public class AsyncTimeClientHandler extends ThreadGroup implements Runnable,CompletionHandler<Void,AsyncTimeClientHandler> {



    public AsyncTimeClientHandler(String s, int port) {
    }
}
