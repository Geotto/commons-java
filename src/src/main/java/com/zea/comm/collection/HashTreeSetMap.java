package com.zea.comm.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * 按哈希值索引的TreeSet集合
 * @param <K> 键类型
 * @param <V> 元素类型
 */
public class HashTreeSetMap<K, V extends Comparable<V>> {
    /**
     * 索引
     */
    private final HashMap<K, TreeSet<V>> map = new HashMap<>();

    /**
     * 添加到索引
     * @param key 键
     * @param value 值
     */
    public void add(K key, V value) {
        TreeSet<V> values = map.computeIfAbsent(key, k -> new TreeSet<>());
        values.add(value);
    }

    /**
     * 判断是否包含指定的键
     * @param key 键
     * @return 是否包含指定的键
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    /**
     * 获取集合
     * @param key 键
     * @return 集合，如果不存在则返回null
     */
    public NavigableSet<V> get(K key) {
        return map.containsKey(key) ? Collections.unmodifiableNavigableSet(map.get(key)) : null;
    }
}
