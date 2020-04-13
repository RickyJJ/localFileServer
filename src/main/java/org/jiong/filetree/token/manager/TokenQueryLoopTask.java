package org.jiong.filetree.token.manager;

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

    private ThreadPoolExecutor executor = null;

    TokenQueryLoopTask(BlockingDeque<TokenPageEntity> pages, int intervalSeconds) {
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
    @Override
    public void run() {
        while (!isStopped || hasPageToStore()) {

            if (!hasPageToStore()) {
                log.warn("page is null");
                try {
                    Thread.sleep(intervalSeconds * 1000);
                } catch (InterruptedException e) {
                    log.error("sleep failed", e);
                }
                continue;
            }

            TokenPageEntity tokenPageEntity = pages.poll();
            if (tokenPageEntity == null) {
                continue;
            }

            if (tokenPageEntity.getLastCheckTime().isBefore(Instant.now())) {
                executor.submit(() -> {
                    tokenPageEntity.listenOverTime();
                    TokensManager.removeTokenPage(tokenPageEntity.getPageName());
                });
            } else {
                // put the token page to last for new loop
                pages.addLast(tokenPageEntity);
            }
        }
    }

    void shutdown() {
        this.isStopped = true;
    }
}
