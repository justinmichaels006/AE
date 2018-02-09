package com.couchbase.AE;

public class Worker implements Runnable
    {
        public boolean running = false;
        private SingleThread singleThread = new SingleThread();

        public Worker ()
        {
            Thread thread = new Thread(this);
            thread.start();
        }

        @Override
        public void run()
        {
            this.running = true;
            System.out.println("This is currently running on a separate thread, " +
                    "the id is: " + Thread.currentThread().getId());
            singleThread.process50Pops();
            this.running = false;
        }
    }
