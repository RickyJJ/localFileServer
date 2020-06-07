package localfileserver.server.request;

import localfileserver.entity.TokenRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * handle token request
 * Add token request to a queue
 * Remove token request to a queue
 *
 * @author Mr.Jiong
 */
@Slf4j
public class TokenRequestManager {
    private static final int SIZE = 50;
    private static final BlockingQueue<TokenRequest> QUEUE;
    private static final BlockingQueue<TokenRequest> WAITING;

    static {
        QUEUE = new ArrayBlockingQueue<>(SIZE);
        WAITING = new ArrayBlockingQueue<>(SIZE);
    }

    public static boolean addToQueue(TokenRequest request) {
        return QUEUE.add(request);
    }

    public static boolean addToWaitQueue(TokenRequest request) {
        return WAITING.add(request);
    }

    public static TokenRequest removeFromQueue(String key) {
        TokenRequest target = null;
        for (TokenRequest request : QUEUE) {
            if (Objects.equals(request.getKey(), key)) {
                target = request;
            }
        }

        if (target != null) {
            QUEUE.remove(target);
        }
        return target;
    }

    public static boolean isFull() {
        return QUEUE.size() == SIZE;
    }

    public static TokenRequest findInQueue(String userName, String key) {
        log.debug("find token request by username[{}], key[{}]", userName, key);
        TokenRequest target = null;
        for (TokenRequest tokenRequest : QUEUE) {
            if (Objects.equals(tokenRequest.getKey(), key)) {
                target = tokenRequest;
                break;
            }
        }

        log.debug("find token request: {}", target == null);
        return target;
    }

    public static TokenRequest findSameRequestInWaitQueue(String userName) {
        for (TokenRequest tokenRequest : WAITING) {
            if (Objects.equals(tokenRequest.getUserName(), userName)) {
                return tokenRequest;
            }
        }
        return null;
    }

    public static TokenRequest findInWaitQueue(String key) {
        for (TokenRequest tokenRequest : WAITING) {
            if (Objects.equals(tokenRequest.getKey(), key)) {
                return tokenRequest;
            }
        }
        return null;
    }

    public static TokenRequest removeFromWaitQueue(String key) {
        TokenRequest target = null;
        for (TokenRequest request : WAITING) {
            if (Objects.equals(request.getKey(), key)) {
                target = request;
            }
        }

        if (target != null) {
            WAITING.remove(target);
        }
        return target;
    }

    public static List<TokenRequest> getTokenRequests() {
        return new ArrayList<>(WAITING);
    }
}
