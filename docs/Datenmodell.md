# Datenmodell

In diesem Dokument wird defniniert, wie Spieldaten gespeichert werden.

Das Speicherformat für diese Daten ist `YAML`.

## Allgemein

### Pfade

Alle Pfade, die in diesem Dokument definiert werden beziehen sich auf die Ressourcenstruktur innerhalb des JAR-Files. Pfade sehen dann bspw. so aus: `data/tracks.yml`.

### Koordinatensytem

Das world-space Koordinatensystem ist so definiert, dass `0|0` im Mittelpunkt ist. Wenn die Kamerarotation `0` ist,  dann zeigt die Y-Achse nach oben, und die X-Achse nach rechts. Als Einheit werden hier Meter verwendet.

### Texturen

Texturen werden referenziert, indem der relative Pfad **innerhalb des `textures/` Ordner** angegeben wird.

Um Texturen in world-space zu rendern, wird das Umrechnungsverhältnis 150:1 verwendet. Das heißt, dass 150 Pixel in der Textur einem Meter in der Welt entsprechen.

## Material (`materials.yml`)
```yaml
- id: "grass"
  texture: ""
  grip: 0
- id: "street"
  texture: ""
  grip: 0
```

Ein `Material` ist eine Art von Untergrund auf einer Rennstrecke (z.B. Straße, Schlamm, etc.). Alle Materialien, die in Streckendefinitionen verwendet werden können, werden hier definiert.

- `id`: Ein Identifier, der benutzt wird, um das `Material` später zu referenzieren.
- `texture`: Eine tileable Hintergrundtextur, die zum Rendern des Materials benutzt wird.
- `grip`:  Definiert, wie viel Halt der Reifen auf diesem Material hat.

## Track (`tracks.yml`)

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
```

Ein `Track` definiert eine Rennstrecke.

- `id`: Ein Identifier, der benutzt wird, um die Strecke später zu referenzieren.
- `name`, `desc`  and `thumbnail`: Wird dem Benutzer in der Streckenauswahl-UI angezeigt
- `base_material`: Das Material, das außerhalb der Rennstrecke benutzt werden soll.
- `path`: Eine Liste von Nodes, die die Rennstrecke definiert. Dieser Pfad wird spline-interpoliert.
  - Eine Node der Rennstrecke hat eine Position (`x` und `y`), eine Breite (`w`) und ein Material (`mat`). `w` und `mat` werden zwischen zwei Nodes linear interpoliert.
  - Die Start- bzw. Ziellinie befindet sich bei der ersten definierten Node. Die Autos starten hinter der Startlinie in Richtung der definierten Strecke.
- `entities`: Entities sind Objekte, mit denen der Benutzer interagieren kann, wie z.B. Items oder Autos. Manche Entities wie Items werden von vorneherein in der Strecke platziert.
  - Eine `Entity` hat eine Position (`x` und `y`). Diese Position definiert den Mittelpunkt der Entity.

## Cars (`cars.yml`)

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
  weight: 0
- ...
```

- `id`: Used for referencing the car
- `name` und `desc`: Wird dem Benutzer auf der UI angezeigt
- `color_texture`: Eine Textur, mit der das Auto selbst gerendert wird.
- `glow_texture`: Eine Textur, die nur die hellen ("glühenden") Bereiche des Autos enthält.
- `primary_ability`: TODO
- Alle weiteren Parameter werden von der Physik-Engine benötigt.

