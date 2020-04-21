package org.jiong.filetree.request;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * handle token request
 * Add token request to a queue
 * Remove token request to a queue
 * @author Mr.Jiong
 */
public class TokenRequestManager {
    private static final int SIZE = 50;
    private static final BlockingQueue<TokenRequest> QUEUE;

    static {
        QUEUE = new ArrayBlockingQueue<>(SIZE);
    }

    public static boolean addToQueue(TokenRequest request) {
        return QUEUE.add(request);
    }

    public static TokenRequest removeFromQueue(String key) {
        final TokenRequest[] target = {null};
        QUEUE.forEach(request -> {
            if (Objects.equals(request.getKey(), key)) {
                target[0] = request;
            }
        });

        if (target[0] != null) {
            QUEUE.remove(target[0]);
        }
        return target[0];
    }

    public static boolean isFull() {
        return QUEUE.size() == SIZE;
    }
}
