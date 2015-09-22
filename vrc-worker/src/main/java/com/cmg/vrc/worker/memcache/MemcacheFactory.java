package com.cmg.vrc.worker.memcache;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cmg on 17/09/15.
 */
public class MemcacheFactory {

    private static final Logger logger = Logger.getLogger(MemcacheFactory.class.getName());

    private static final ConcurrentHashMap<String, MemcachedClient> POOL = new ConcurrentHashMap<String, MemcachedClient>();

    public static MemcachedClient getMemcachedClient(String address) {
        if (!POOL.containsKey(address)) {
            synchronized (POOL) {
//                try {
//                    POOL.put(address, new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(address)));
//                } catch (IOException e) {
//                    logger.log(Level.SEVERE, "Could not create memcached client",e);
//                }
            }
        }
        return POOL.get(address);
    }
}
