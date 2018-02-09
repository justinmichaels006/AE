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
            singleThread.process50Pops();
            System.out.println("Separate thread complete, the id is: " + Thread.currentThread().getId());
            this.running = false;
        }
    }
