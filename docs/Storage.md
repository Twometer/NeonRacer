# Storage System

This document defines the how maps, entities etc. are stored.

## Materials
```yaml
- id: "grass"
  texture: ""
  grip: 0
- id: "street"
  texture: ""
  grip: 0
```

Defines all materials that can be used in map definitions.

- `Id`: Used for referencing the material
- `Texture`: A tileable background texture for rendering this material.
- `Grip`: How well the tire can grab onto the surface. Required for the physics engine.

## Track

```yaml
id: ""
name: ""
desc: ""
thumbnail: ""
base_material: ""
path:
  - {x: 0, y: 0, w: 20, mat: "street"}
  - {x: 0, y: 100, w: 20, mat: "street"}
  - {x: 100, y: 100, w: 20, mat: "dirt"}
entities:
  - {type: "item", x: 5, y: 0}
objects:
  - {type: "lamp", x: 10, y: 50, collision: false, intensity: 0.6}
```

- `Id`: A unique identifier to reference this map later on
- `Name` and `Thumbnail`: Will be displayed in the map selector GUI.
- `BaseMaterial`: What material should be used outside of the track.

- `Path`: A set of nodes that defines the race track. This path will be [spline-interpolated](https://gist.github.com/lecho/7627739). 
  - A node in the race track has a center point `X` and `Y` and a track width `W`. The `Mat` property defines the material of the track at this node. This will be interpolated as well.
- `Entities`: Entities are interactable objects with control code behind them, such as items that can be collected etc.
  - An `Entity` has a `X` and `Y` position
- `Objects`: Objects are decorative things that can be placed into the world such as lamps, houses etc. They do not have individual handling. One can define whether cars should be able to collide with this object or not.

**Note:** The coordinate system is defined so that `0|0` is at the center of the map. Also, with camera rotation `0`, positive Y is up, and positive X is to the right.

## Cars

```yaml
- id: ""
  name: ""
  desc: ""
  color_texture: ""
  glow_texture: ""
  primary_ability: ""
  acceleration: 0
  brake_strength: 0
  aerodynamics: 0
  friction: 0
  horsepower: 0
  weight: 
- ...
```

- `Id`: Used for referencing the car
- `Name`: This name will be displayed to the user in the car selector UI
- `Desc`: Description that is displayed to the user in the car selector UI
- `ColorTexture`: The texture that is used to draw the car itself
- `GlowTexture`: The texture that only contains the glowing parts of the color texture.
