package neonracer.gui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A widget can hold foreign parameters. These are parameters such as "Grid.Row" that are defined
 * for each widget but belong to a different one, such as the parent. This is a wrapper class
 * over a Map that simplifies access to such foreign parameters.
 */
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
