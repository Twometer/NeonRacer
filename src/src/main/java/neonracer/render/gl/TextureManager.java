package neonracer.render.gl;

import neonracer.render.gl.core.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private Map<String, Texture> map = new HashMap<>();

    public Texture getTexture(String name) {
        Texture mapTexture = map.get(name);
        if (mapTexture == null) {
            mapTexture = Texture.create(name);
            map.put(name, mapTexture);
            return mapTexture;
        } else return mapTexture;
    }

}
