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

    /**
     * Retrieve an integer value from the foreign parameter map
     *
     * @param namespace The namespace of the owner control (e.g. "Grid")
     * @param name      The name of the property (e.g. "Row")
     * @return The integer value
     */
    public int getInt(String namespace, String name) {
        return Integer.parseInt(getString(namespace, name));
    }

    /**
     * Retrieve an integer value from the foreign parameter map, or return
     * the default value if no parameter has been defined
     *
     * @param namespace The namespace of the owner control (e.g. "Grid")
     * @param name      The name of the property (e.g. "Row")
     * @return The integer value or the default value
     */
    public int getIntOrDefault(String namespace, String name, int defaultValue) {
        String str = getString(namespace, name);
        return str == null ? defaultValue : Integer.parseInt(str);
    }

    /**
     * Used by the layout parser to store parameters in this map
     *
     * @param key   The name of the parameter (e.g. "Grid.Row")
     * @param value The value of the parameter
     */
    public void put(String key, String value) {
        parameters.put(key, value);
    }

}
