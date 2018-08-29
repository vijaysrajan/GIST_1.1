package com.fratics.precis.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PrettyPrintingMap<K, V> {
    private Map<K, V> map;

    public PrettyPrintingMap(Map<K, V> map) {
        this.map = map;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            sb.append(entry.getValue() + "\n");
        }
        return sb.toString();
    }
}