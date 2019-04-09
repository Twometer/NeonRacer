package neonracer.render.gl;

import neonracer.render.gl.core.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureProvider {

    private Map<String, Texture> map = new HashMap<>();

    public Texture getTexture(String path) {
        Texture mapTexture = map.get(path);
        if (mapTexture == null) {
            mapTexture = Texture.create(path);
            map.put(path, mapTexture);
            return mapTexture;
        } else return mapTexture;
    }

}
