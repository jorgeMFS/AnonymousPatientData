///*  Copyright   2011 - Luís A. Bastião Silva
// *
// *  This file is part of AnonymousPatientData.
// *
// *  AnonymousPatientData is free software: you can redistribute it and/or modify
// *  it under the terms of the GNU General Public License as published by
// *  the Free Software Foundation, either version 3 of the License, or
// *  (at your option) any later version.
// *
// *  AnonymousPatientData is distributed in the hope that it will be useful,
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *  GNU General Public License for more details.
// *
// *  You should have received a copy of the GNU General Public License
// *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
// */
//
//package pt.ieeta.anonymouspatientdata.core.impl;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Map;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import net.sf.ehcache.Cache;
//import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.Element;
//import net.sf.ehcache.config.CacheConfiguration;
//import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
//import net.sf.ehcache.store.disk.DiskStore;
//
///**
// *
// * @author Luís A. Bastião Silva <bastiao@ua.pt>
// */
//public class PersistentCache<K, V> implements Map
//{
//
//    public static final Integer MAX_ELEMENTS = 2;
//    
//    private Cache cache;
//    private CacheManager manager ;
//    
//    public  PersistentCache(String name)
//    {
//        
//         manager = CacheManager.create();
//        //Create a Cache specifying its configuration.
//        cache = new Cache(
//                new CacheConfiguration(name, MAX_ELEMENTS)
//                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
//                .overflowToDisk(true)
//                .overflowToOffHeap(false)
//                .eternal(true)
//                .timeToLiveSeconds(0)        
//                .timeToIdleSeconds(0)
//                .maxMemoryOffHeap("1k") 
//                //.maxElementsInMemory(MAX_ELEMENTS)
//                .diskPersistent(true)
//                .diskExpiryThreadIntervalSeconds(0));
//        cache.setDiskStorePath("/Users/bastiao/Projects/devel/apps/AnonymousPatientData/"+"storage-"+name+".cache");
//        DiskStore store = DiskStore.create(cache, "/Users/bastiao/Projects/devel/apps/AnonymousPatientData/"+"storage-"+name+".cache");
//        manager.addCache(cache);
//        store.put(new Element("lol", "as"));
//        try {
//            store.flush();
//        } catch (IOException ex) {
//            Logger.getLogger(PersistentCache.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        cache.flush();
//    }
//    public void shutdown()
//    {
//    
//        manager.shutdown();
//    }
//    
//    public Object get(Object o) 
//    {
//        return cache.get(o);
//    }
//
//    public Object put(Object k, Object v) {
//        cache.put(new Element(k, v));
//        cache.flush();
//        return v;
//    }
//
//    public Object remove(Object o) {
//        cache.remove(o);
//        return o;
//    }
//    
//    
//    public static void main(String [] args)
//    {
//     
//        
//        PersistentCache<String, String> p = new PersistentCache("mycache"); 
//        p.put("test", "fuck1");
//        p.put("test2", "fuck2");
//        p.put("test3", "fuck3");
//        int elementsInMemory = p.size();
//        System.out.println("size: " + elementsInMemory);
//        Cache cache = CacheManager.getInstance().getCache("mycache");
//        long _elementsInMemory = cache.getMemoryStoreSize();
//        System.out.println("size: " + _elementsInMemory);
//
//        p.put("test4", "fuck4");
//        p.put("test5", "fuck5");
//        //cache.flush();
//        System.out.println("Memory stora size : "  + cache.getMemoryStoreSize());
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(PersistentCache.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(p.get("test"));
//        System.out.println(p.get("test2"));
//        System.out.println(p.get("test3"));
//        System.out.println(p.get("test4"));
//        System.out.println(p.get("lol"));
//        System.out.println(p.get("test5"));
//        //p.shutdown();
//        //CacheManager.getInstance().shutdown();
//        System.exit(0);
//    }
//
//    public int size() {
//        return cache.getSize();
//    }
//
//    public boolean isEmpty() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public boolean containsKey(Object o) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public boolean containsValue(Object o) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void putAll(Map map) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void clear() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public Set keySet() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public Collection values() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public Set entrySet() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
