# Storage System

This document defines the how maps, entities etc. are stored.

## Materials
```xml
<Materials>
    <Material Id="GRASS" Texture="texture_path" Slipperiness="..."/>
</Materials>
```

Defines all materials that can be used in map definitions.

- `Id`: Used for referencing the material
- `Texture`: A tileable background texture for rendering this material.

## Maps

```xml
<Map Id="map_id" Name="Map_Name" Thumbnail="thumbnail_tex" BaseMaterial="GRASS">
	<Path>
		<Node X="0" Y="0" W="20" Material="STREET"/>
		<Node X="0" Y="100" W="20" Material="STREET"/>
		<Node X="100" Y="100" W="20" Material="DIRT"/>
	</Path>
	<Entities>
        <EntityItem X="5" Y="0"/>
	</Entities>
    <Objects>
        <Lamp X="10" Y="50" CanCollide="False" Intensity="0.6"/>
    </Objects>
</Map>
```

- `Id`: A unique identifier to reference this map later on
- `Name` and `Thumbnail`: Will be displayed in the map selector GUI.
- `BaseMaterial`: What material should be used outside of the track.

- `Path`: A set of nodes that defines the race track. This path will be [spline-interpolated](https://gist.github.com/lecho/7627739). 
  - `Node`: A node in the race track has a center point `X` and `Y` and a track width `W`. The `Material` property defines the texture of the track at this node and the track properties for the physics engine.
- `Entities`: Entities are interactable objects with control code behind them, such as items that can be collected etc.
  - An `Entity` has a `X` and `Y` position
- `Objects`: Objects are decorative things that can be placed into the world such as lamps, houses etc. They do not have individual handling. One can define whether cars should be able to collide with this object or not.

**Note:** The coordinate system is defined so that `0|0` is at the center of the map. Also, with camera rotation `0`, positive Y is up, and positive X is to the right.

## Cars

```xml
<Cars>
    <Car Id="car_id" Name="Car_name" Desc="...">
        <Render ColorTexture="tex_path" GlowTexture="glow_tex"/>
        <Physics TopSpeed="" Acceleration="" TurningRadius="" BrakeStrength=""/>
        <Gameplay PrimaryAbility="ability_id"/>
    </Car>
</Cars>
```

- `Name`: This name will be displayed to the user in the car selector UI
- `Desc`: Description that is displayed to the user in the car selector UI
- `Render`: Defines all properties required for rendering the car
  - `ColorTexture`: The texture that is used to draw the car itself
  - `GlowTexture`: The texture that only contains the glowing parts of the color texture.vo
- `Physics`: Relevant information for the physics engine
  - *TODO*: Define relevant physics variables here.
- `Gameplay`: Defines everything that is related to gameplay. Right now, this is only the `PrimaryAbility`.

## Abilities

```xml
<Abilities>
    <Ability Id="ability_id" Name="Ability_Name" Desc="Ability_Description"/>
</Abilities>
```

Defines all abilities that can be used as a `PrimaryAbility`. `Name` and `Desc` will be displayed to the user in the car selector UI.