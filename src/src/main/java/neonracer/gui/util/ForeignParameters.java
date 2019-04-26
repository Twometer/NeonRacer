package neonracer.gui.util;

import java.util.HashMap;
import java.util.Map;

public class ForeignParameters {

    private Map<String, String> parameters = new HashMap<>();

    private String getString(String namespace, String name) {
        return parameters.get(namespace + "." + name);
    }

    public int getInt(String namespace, String name) {
        return Integer.parseInt(getString(namespace, name));
    }

    public void put(String key, String value) {
        parameters.put(key, value);
    }

}
