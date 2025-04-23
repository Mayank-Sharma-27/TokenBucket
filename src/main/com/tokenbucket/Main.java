package com.tokenbucket;

import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main( String args[] ) throws InterruptedException {

        Set<Thread> allThreads = new HashSet<Thread>();
        final TokenBucket tokenBucketFilter = new TokenBucket(1);

        for (int i = 0; i < 10; i++) {

            Thread thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        tokenBucketFilter.getToken();
                    } catch (Exception ie) {
                        System.out.println("We have a problem");
                    }
                }
            });
            thread.setName("Thread_" + (i + 1));
            allThreads.add(thread);
        }

        for (Thread t : allThreads) {
            t.start();
        }

        for (Thread t : allThreads) {
            t.join();
        }

    }

}
