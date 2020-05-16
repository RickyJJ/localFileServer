package localfileserver.server.manager;

import java.util.concurrent.ThreadFactory;

/**
 * @author Mr.Jiong
 */
public class ThreadFactoryExample implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, "factory-thread-clear-token-queue");
    }
}
