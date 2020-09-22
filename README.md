# NeonRacer

### Spielen

Aktuelle Releases können [hier](https://github.com/Twometer/NeonRacer/releases) heruntergeladen werden. Die `neonracer.jar` Datei kann einfach per Doppelklick ausgeführt werden. Einzige Voraussetzung ist, dass Java installiert ist.

NeonRacer kann nur im Multiplayer gespielt werden und es gibt keine öffentlichen Server. Um spielen zu können, musst du dich also selbst um einen Server kümmern.

### Server

#### Windows
1. Stelle sicher, dass Java installiert ist 
2. Lade die aktuelle Version von NeonRacer herunter
3. Öffne die Eingabeaufforderung
4. Wechsle in den Ordner mit der JAR-Datei  
`cd Downloads`
5. Starte den Server  
`java -cp neonracer.jar neonracer.server.ServerMain`
6. Falls dir eine Meldung von der Windows Firewall angezeigt wird, stimme zu, dass Java eingehende Verbindungen annehmen darf

#### Ubuntu
Diese Anleitung wurde für Ubuntu 18.04 LTS erstellt, sollte aber sehr ähnlich auch mit anderen Linux-Distributionen fuktionieren.
1. Stelle sicher, dass Java installiert ist  
`sudo apt update && sudo apt install -y default-jre`
2. Lade die aktuelle Version von NeonRacer herunter
3. Lege eine Firewall-Regel für NeonRacer an  
`sudo ufw enable && sudo ufw allow 5000/tcp`
4. Starte den Server
`java -cp neonracer.jar neonracer.server.ServerMain`

---

## Entwicklung
Die Dokumentation befindet sich im [GitHub Wiki](https://github.com/Twometer/NeonRacer/wiki).

### Konfiguration

Neon Racer wurde mit _IntelliJ Idea_ entwickelt. Zusätzlich sind noch _Git_ und ein _JDK 1.8_ erforderlich.
Für Änderungen am Protokoll ist außerdem das `Protobuf Support` IntelliJ-Plugin empfehlenswert.

1. Das Repository klonen  
`git clone https://github.com/Twometer/NeonRacer`
2. Das Projekt in _IntelliJ_ öffnen, nicht importieren! (`File -> Open`)  
Dabei `Create directories for empty content roots automatically` und `use default gradle wrapper` auswählen
3. Builds immer mit Gradle ausführen  
Unter `File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Runner` und `Delegate IDE build/run actions to Gradle` auswählen.
4. Nachdem der Gradle Build abgeschlossen ist, den `src` Ordner ausklappen und auf `NeonRacerMain` rechtsklicken und dann `Run 'NeonRacerMain.main()'` auswählen.

Alle weiteren verwendeten Libraries wie `LWGL`, `JBox2D` und der `protobuf` Compiler werden von _Gradle_ automatisch heruntergeladen und konfiguriert.

Für komplizierte Anwendungsfälle kann NeonRacer über die Konsole gestartet werden:
- Client: `java -jar neonracer.jar`
- Server: `java -cp neonracer.jar neonracer.server.ServerMain`
- Designer: `java -cp neonracer.jar neonracer.designer.TrackDesignerMain`

### Sprachen

Da es bei Neon Racer um ein Projekt handelt, das in einer deutschen Schule zu Unterrichtszwecken verwendet werden soll, werden nicht alle Texte auf Englisch verfasst.

Englisch
- Commit messages
- Kommentare im Code
- Identifier
- Dokumentation im Code (JavaDoc)

Deutsch
- Dokumentation
- Issues und Pull Requests
- Externe Dokumente
