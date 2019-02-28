package neonracer.model.track;

import neonracer.render.gl.Texture;
import neonracer.model.entities.Entity;

import java.util.List;

/**
 * Defines a race track using a background material and a
 * path where the track is on.
 */
public class Track {

    private String id;

    private String name;

    private String description;

    private Texture thumbnail;

    private Material baseMaterial;

    private List<Node> path;

    private List<Entity> entities;

}
