package neonracer.util;

import neonracer.resource.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

public class BuildInfo {

    private static final String PROPERTIES_PATH = "build_info.properties";

    private static final String KEY_NAME = "product.name";

    private static final String KEY_VERSION = "product.version";

    private static final String KEY_PORT = "network.port";

    private static Properties properties;

    public static String getGameTitle() {
        ensureLoaded();
        return properties.getProperty(KEY_NAME);
    }

    public static String getGameVersion() {
        ensureLoaded();
        return properties.getProperty(KEY_VERSION);
    }

    public static int getNetworkPort() {
        ensureLoaded();
        return Integer.valueOf(properties.getProperty(KEY_PORT));
    }

    private static void ensureLoaded() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(ResourceLoader.openReader(PROPERTIES_PATH));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
