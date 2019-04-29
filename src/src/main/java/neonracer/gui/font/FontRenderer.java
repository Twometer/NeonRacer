package neonracer.gui.font;

import neonracer.core.GameContext;
import neonracer.render.RenderContext;
import neonracer.render.engine.mesh.MeshBuilder;
import neonracer.render.engine.mesh.Rectangle;
import neonracer.render.gl.core.Model;
import neonracer.render.gl.shaders.FontShader;
import neonracer.util.Log;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class FontRenderer {

    private RenderContext renderContext;

    private GameContext gameContext;

    private FontShader fontShader;

    private String fontFaceName;

    private FontFace fontFace;

    private Model textModel;

    public FontRenderer(String fontFace) {
        this.fontFaceName = fontFace;
    }

    public void setup(RenderContext renderContext, GameContext gameContext) {
        this.renderContext = renderContext;
        this.gameContext = gameContext;
        fontShader = new FontShader();
        try {
            fontFace = FontFace.load(gameContext, fontFaceName);
        } catch (IOException e) {
            Log.e(e);
        }
    }

    public void draw(String text, float x, float y, float fontSize) {
        draw(text, x, y, fontSize, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public void draw(String text, float x, float y, float fontSize, Vector4f color) {
        fontSize *= gameContext.getGameWindow().getScale();
        if (textModel != null)
            textModel.destroy();
        textModel = build(text, fontSize, x, y);
        fontShader.bind();
        fontShader.setProjectionMatrix(renderContext.getGuiMatrix());
        fontShader.setColor(color.x, color.y, color.z, color.w);
        glActiveTexture(GL_TEXTURE0);
        fontFace.getFontTexture().bind();
        textModel.draw();
        fontFace.getFontTexture().unbind();
        fontShader.unbind();
        textModel.destroy();
    }

    public float getLineHeight(float fontSize) {
        return 50f * fontSize * gameContext.getGameWindow().getScale();
    }

    public float getStringHeight(String string, float fontSize) {
        fontSize *= gameContext.getGameWindow().getScale();
        float height = 0;
        for (char c : string.toCharArray()) {
            Glyph glyph = fontFace.getGlyph(c);
            float glyphHeight = (glyph.getHeight() + glyph.getyOffset()) * fontSize;
            if (height < glyphHeight)
                height = glyphHeight;
        }
        return height;
    }

    public float getStringWidth(String string, float fontSize) {
        fontSize *= gameContext.getGameWindow().getScale();
        float cursor = 0;
        for (char c : string.toCharArray()) {
            Glyph glyph = fontFace.getGlyph(c);
            cursor += (glyph.getAdvance() - 15) * fontSize;
        }
        return cursor;
    }

    public void destroy() {
        if (textModel != null)
            textModel.destroy();
    }

    private Model build(String text, float fontSize, float x, float y) {
        MeshBuilder meshBuilder = new MeshBuilder(text.length() * 6);

        float cursor = x;
        for (char c : text.toCharArray()) {
            Glyph glyph = fontFace.getGlyph(c);

            Rectangle charRect = createChar(cursor, y, glyph, fontSize);
            meshBuilder.putRectVertices(charRect);

            float w = fontFace.getFontTexture().getWidth();
            float h = fontFace.getFontTexture().getHeight();
            meshBuilder.putRectTexCoords((glyph.getX()) / w, (glyph.getY()) / h, (glyph.getX() + glyph.getWidth()) / w, (glyph.getY() + glyph.getHeight()) / h);
            cursor += (glyph.getAdvance() - 15) * fontSize;
        }

        Model model = Model.create(meshBuilder.getMesh(), GL_TRIANGLES);
        meshBuilder.destroy();
        return model;
    }

    private Rectangle createChar(float cursorX, float cursorY, Glyph glyph, float fontSize) {
        float x1 = cursorX + (glyph.getxOffset() * fontSize);
        float y1 = cursorY + (glyph.getyOffset() * fontSize);
        float x2 = x1 + (glyph.getWidth() * fontSize);
        float y2 = y1 + (glyph.getHeight() * fontSize);
        return new Rectangle(x1, y1, x2, y2);
    }

}
