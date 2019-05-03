# NeonRacer

Die Dokumentation befindet sich im [GitHub Wiki](https://github.com/Twometer/NeonRacer/wiki).

## Konfiguration

Neon Racer wurde mit _IntelliJ Idea_ entwickelt. Zusätzlich sind noch _Git_ und ein _JDK 1.8_ erforderlich.
Für Änderungen am Protokoll ist außerdem das `Protobuf Support` IntelliJ-Plugin empfehlenswert.

1. Das Repository klonen  
`git clone https://github.com/Twometer/NeonRacer`
2. Das Projekt in _IntelliJ_ öffnen, nicht importieren! (`File -> Open`)  
Dabei `Create directories for empty content roots automatically` und `use default gradle wrapper` auswählen
3. Nachdem der Gradle Build abgeschlossen ist, den `src` Ordner ausklappen und auf `NeonRacerMain` rechtsklicken und dann `Run 'NeonRacerMain.main()'` auswählen.

Alle weiteren verwendeten Libraries wie `LWGL`, `JBox2D` und der `protobuf` Compiler werden von _Gradle_ automatisch heruntergeladen und konfiguriert.

## Sprachen

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
