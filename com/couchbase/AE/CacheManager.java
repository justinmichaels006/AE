package com.couchbase.AE;

import com.couchbase.client.java.CouchbaseBucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.datastructures.collections.CouchbaseQueue;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class CacheManager {


        //private static CacheManager cacheManager;

        private static CouchbaseCluster couchbaseCluster;

        private static CouchbaseBucket couchbaseBucket;
        static {System.setProperty("com.couchbase.datastructureCASRetryLimit", "100");};

        public static CouchbaseBucket getInstance()
        {
            if(couchbaseBucket == null)
            {
                try {

                    //String CPS = "cb-cache.cps";
                    CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder().build();
                    List<String> nodes = new ArrayList<String>(Arrays.asList("192.168.61.101", "192.168.61.102"));
                    couchbaseCluster = CouchbaseCluster.create(env, nodes);
                    couchbaseBucket = (CouchbaseBucket) couchbaseCluster.openBucket("testload", "password");
                    //cacheManager = new CacheManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return couchbaseBucket;
        }
        /**
         *
         * @param key  ut:DPAN
         * @param value  json string
         * @param featureSet  string
         * Insert into feature set queue and with key value and also in local cache json document
         */
        public void insertCacheData(String key, String value, String featureSet) {

            System.out.println("Generate Perso DATA CacheManager_insertCacheData executed. key = "+ key +" :: value = " + value + " :: featureSet = " + featureSet);
            JsonObject object =   JsonObject.fromJson(value);
            JsonDocument jsonDocument =  JsonDocument.create(key,object);

            Queue<String> featureSetQueue = new CouchbaseQueue<String>(featureSet, couchbaseBucket);
            System.out.println("Inserting into Queue" + featureSetQueue + featureSet);
            couchbaseBucket.queuePush(featureSet, key);
            System.out.println("Inserting completed");
            couchbaseBucket.insert(jsonDocument);

            System.out.println("Queue Size" + featureSetQueue.size());

        }

        public String popFromQueue(String queueId) {

            String queueData = couchbaseBucket.queuePop(queueId, String.class);
            return queueData;

        }

        public JsonDocument getCacheData(String key) {
            JsonDocument jsonDocument =  couchbaseBucket.get(key);
            return jsonDocument;
        }
}
