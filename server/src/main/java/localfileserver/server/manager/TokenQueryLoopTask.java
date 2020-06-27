package localfileserver.server.manager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
class TokenQueryLoopTask extends Thread {
    private int intervalSeconds = 120;

    private BlockingDeque<TokenPageEntity> pages = null;

    private boolean isStopped = false;

    private String name;

    private ThreadPoolExecutor executor = null;

    TokenQueryLoopTask(String queueName, BlockingDeque<TokenPageEntity> pages, int intervalSeconds) {
        this.name = queueName;
        this.intervalSeconds = intervalSeconds;
        this.executor = new ThreadPoolExecutor(3, 6, 30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(8),
                new ThreadFactoryExample());
        this.pages = pages;
    }

    boolean hasPageToStore() {
        return pages != null && pages.size() != 0;
    }

    /**
     * write token status to token file
     */
    @SneakyThrows
    @Override
    public void run() {
        while (!isStopped || hasPageToStore()) {

            if (!hasPageToStore()) {
                log.warn("[{}] page is null", this.name);
                try {
                    Thread.sleep(intervalSeconds * 1000);
                } catch (InterruptedException e) {
                    log.error("sleep failed", e);
                }
                continue;
            }

            TokenPageEntity tokenPageEntity = pages.poll();
            if (tokenPageEntity == null) {
                log.debug("token page is null");
                continue;
            }

            if (tokenPageEntity.getLastCheckTime().isBefore(Instant.now())) {
                log.debug("Token page is out of time, ready to remove page");
                executor.submit(() -> {
                    if (tokenPageEntity.getUsedSize() == 0) {

                        log.debug("Token page is out of scope, get to remove page from token manager");
                        tokenPageEntity.listenOverTime();
                        TokensManager.removeTokenPage(tokenPageEntity.getPageName());
                    } else {
                        tokenPageEntity.setLastCheckTime(Instant.now());
                    }
                });
            } else {
                // put the token page to last for new loop
                log.debug("add page to the last of queue");
                pages.addLast(tokenPageEntity);
            }
        }
    }

    void shutdown() {
        this.isStopped = true;
    }
}
