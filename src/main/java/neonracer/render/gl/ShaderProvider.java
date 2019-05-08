package neonracer.render.gl;

import neonracer.render.gl.shaders.Shader;

import java.util.HashMap;
import java.util.Map;

public class ShaderProvider {

    private Map<Class<? extends Shader>, Shader> cache = new HashMap<>();

    public <T extends Shader> T getShader(Class<T> shaderClass) {
        Shader shader = cache.get(shaderClass);
        if (shader == null) {
            try {
                shader = shaderClass.newInstance();
                cache.put(shaderClass, shader);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) shader;
    }

}
