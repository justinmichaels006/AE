package com.couchbase.AE;

import com.couchbase.client.java.CouchbaseBucket;
import com.couchbase.client.java.datastructures.collections.CouchbaseQueue;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SingleThread {

    CouchbaseBucket cachemanager1 = CacheManager.getInstance();

    public void process50Pops() {

        //for (int i = 0; i < cachemanager1.queueSize("AE::queue::1"); i++) {
        for (int i = 0; i < 10; i++) {

            try {

                String key = cachemanager1.queuePop("AE::queue::1", String.class);
                JsonObject content = JsonObject.empty().put("name", "Justin");
                JsonDocument doc = JsonDocument.create(key, content);
                cachemanager1.upsert(doc);

                System.out.println("Key in Queue -- > " + key);
                System.out.println("Queue Size ::" + cachemanager1.queueSize("AE::queue::1"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void qBuild() {

        Queue<String> AEQ = new CouchbaseQueue<String>("AE::queue::1", cachemanager1);
        UUID anID;
        for (int dm = 0; dm <10; dm++) {
            for (int j = 0; j < 1000; j++) {
                anID = UUID.randomUUID();
                AEQ.add(anID.toString());
            }
        }
    }

    private static void writeUsingFileWriter(String data) {
        File file = new File("SingleThread.log");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args) throws InterruptedException
    {
        List<Worker> workers = new ArrayList<Worker>();
        System.out.println("This is currently running on the main thread, " + "the id is: " + Thread.currentThread().getId());

        Date start = new Date();
        // start i number of workers
        for (int i=0; i<10; i++)
        {
            workers.add(new Worker());
        }

        for (Worker worker : workers)
        {
            while (worker.running)
            {
                worker.wait();
                //Thread.sleep(1);
            }
        }

        Date end = new Date();
        long difference = end.getTime() - start.getTime();
        System.out.println ("This whole process took: " + difference/1000 + " seconds.");

    }
}
