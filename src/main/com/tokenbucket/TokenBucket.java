package com.tokenbucket;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {

    long currentNumberOfTokens = 0;
    int maxNumberOfTokens;
    private long lastRequestedTime;
    private Lock lock;
    private Condition condition;

    public TokenBucket(int maxNumberOfTokens) {
        this.maxNumberOfTokens = maxNumberOfTokens;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();

        Thread dt = new Thread(() -> {
            fillerThread();
        });
        dt.setDaemon(true);
        dt.start();
    }

    private void fillerThread() {
        while (true) {
            try {
                lock.lock();
                if (currentNumberOfTokens < maxNumberOfTokens) {
                    currentNumberOfTokens++;
                }
                condition.signal();
                lock.unlock();
                Thread.sleep(1000);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void getToken() {
        lock.lock();
        try {
           while (currentNumberOfTokens  == 0) {
               condition.await();
           }
            currentNumberOfTokens--;
            System.out.println(
                    "Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }

    }
}
