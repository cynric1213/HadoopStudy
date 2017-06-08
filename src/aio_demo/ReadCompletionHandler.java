package aio_demo;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by cynric on 17-6-7.
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, AsyncTimeServerHandler> {
    public ReadCompletionHandler(AsynchronousSocketChannel result) {
    }

    @Override
    public void completed(Integer result, AsyncTimeServerHandler attachment) {

    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {

    }
}
