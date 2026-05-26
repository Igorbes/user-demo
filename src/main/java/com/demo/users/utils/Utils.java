package com.demo.users.utils;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static <K, V> Map<K, V> createMap(K k, V... args) {
        Map result = new HashMap();
        result.put(k, args[0]);

        for(int i = 1; i < args.length; ++i) {
            Object var10001 = args[i];
            ++i;
            result.put(var10001, args[i]);
        }

        return result;
    }
}
