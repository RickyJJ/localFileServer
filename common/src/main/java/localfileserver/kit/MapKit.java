package localfileserver.kit;

import java.util.HashMap;
import java.util.Map;

public class MapKit {
    public static <K, V> Map<K, V> map(K key, V val) {
        HashMap<K, V> kvHashMap = new HashMap<>();
        kvHashMap.put(key, val);
        return kvHashMap;
    }
}
