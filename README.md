# Mühle (PiS, SoSe 2020) 

Autor: Benedikt Jensen, 5302211

Ich habe die Zulassung für PiS im SoSe 2020 bei Herrn Herzberg erhalten.

1. [Einleitung](#introduction)
2. [Spielregeln](#rules)
3. [Bedienungsanleitung](#manual)
4. [Dateiübersicht](#overview)

## Einleitung <a name="introduction"></a>

### Spielregeln <a name="rules"></a>

Weiß spielt traditionell immer zuerst. In der ersten Spielphase wird abwechselnd je ein Spielstein auf ein freies Feld 
gesetzt, bis insgesamt je 9 Spielsteine eingesetzt wurden.<br>
In der nächsten Phase wird stattdessen je Zug ein eigener Spielstein auf
ein angrenzendes freies Feld bewegt. Wenn zu irgendeinem Zeitpunkt im Spielsteine eine Dreierreihe, genannt eine Mühle, 
bilden, kann der Spieler, welcher diese Reihe gebildet hat, einen Spielstein des Gegners aus dem Spiel nehmen. Dabei ist 
es nicht erlaubt einen Stein aus einer Mühle zu nehmen. Befindet sich allerdings jeder der gegnerischen Spielsteine in
einer Mühle, wird diese Regel ungültig und der zu nehmende Spielstein darf frei gewählt werden.<br>
Sobald ein Spieler nur noch drei Spielsteine auf dem Feld hat, ist es ihm erlaubt mit seinen Spielsteinen auf ein 
beliebiges freies Feld zu springen, anstatt nur auf angrenzende Felder zu ziehen.<br>

Das Spiel endet, wenn einem Spieler 
entweder kein gültiger Zug mehr möglich ist, oder er weniger als drei Spielsteine übrig hat. 
Der jeweilige Spieler verliert das Spiel.<br>
Remis: Das Spiel endet auch, wenn dreimal hintereinander, die gleiche Abfolge von Spielzügen gemacht hat, oder
wenn 20 Spielzüge gemacht wurden, ohne das sich eine Mühle gebildet hat. In beiden Fällen endet das Spiel mit 
unentschieden.

### Bedienungsanleitung <a name="manual"></a>

![Screenshot](Screenshot.png)

<Beschreiben Sie die Bedienung Ihres Programms.>

### Dateiübersicht <a name="overview"></a>

    \build.gradle
    \README.md
    \bin\main\public\index.html
    \bin\main\TicTacToe\App.kt
    \bin\main\TicTacToe\T3.kt
    \src\main\kotlin\TicTacToe\App.kt
    \src\main\kotlin\TicTacToe\T3.kt
    \src\main\resources\public\index.html

    -------------------------------------------------------------------------------
    Language                     files          blank        comment           code
    -------------------------------------------------------------------------------
    Markdown                         1             71              0            270
    Kotlin                           3             27              3            113
    HTML                             1             11             17             80
    XML                              2              0              0             41
    Gradle                           1              8             12             16
    INI                              1              0              0             13
    -------------------------------------------------------------------------------
    SUM:                             9            117             32            533
    -------------------------------------------------------------------------------
~~~

**Hinweise zum Aufbau**

* Die `<Spielbezeichnung>` ist zum Beispiel zu ersetzen mit "Vier gewinnt", "Mühle", "Dame" etc.
* Geben Sie bitte Ihren Namen _und_ Ihre Matrikelnummer an. Ohne Matrikelnummer haben wir es unnötig schwer, Ihr Prüfungsergebnis zu melden.
* Geben Sie unbedingt an, bei wem Sie wann Ihre Prüfungsvorleistung erhalten haben.
* Es gibt Editoren, wie Visual Studio Code, für die Sie eine Erweiterung herunterladen können, damit Sie in einem Markdown-Dokument ein Inhaltsverzeichnis (_Table of Content_) einfügen und automatisch aktualisieren lassen können. Bitte fügen Sie ein Inhaltsverzeichnis ein. (Wenn Ihnen das nicht möglich ist, ist das unkritisch.)
* Fügen Sie nur _einen_(!) Screenshot Ihrer Anwendung in Aktion ein. Bitte denken Sie daran, einen relativen Link wie gezeigt zu der Bilddatei zu verwenden!  
* Ihre Anwendung sollte so selbstverständlich nutzbar sein, dass es eigentlich keiner Anleitung bedarf. Geben Sie sich bitte dennoch die Mühe, eine kurze Nutzungsanleitung zur Bedienung des Spiels zu schreiben. Dazu gehört nicht das Starten der Anwendung! Es geht um die Bedienung der Oberfläche.

**Zur Dateiübersicht**

Die Dateiübersicht erstellen Sie bitte automatisch und fügen sie entsprechend dem obigen Beispiel eingerückt ein. Die Einrückung sorgt dafür, dass der Text wie ein Codeblock behandelt und hervorgehoben wird.

Wechseln Sie unter Windows mit `cd` in das Projektverzeichnis Ihrer Anwendung; im folgenden Beispiel heißt das Verzeichnis `TicTacToe`. Listen Sie alle Dateien mit `dir /S /B /A-D .` auf:

~~~
> dir /S /B /A-D .
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\build.gradle
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\README.md
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\bin\main\public\index.html
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\bin\main\TicTacToe\App.kt
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\bin\main\TicTacToe\T3.kt
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\src\main\kotlin\TicTacToe\App.kt
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\src\main\kotlin\TicTacToe\T3.kt
C:\Users\Dominikus\GitTHM\JbX\TicTacToe\src\main\resources\public\index.html
~~~

Wenn Sie Linux oder MacOS verwenden, werden Sie die entsprechenden Befehle für die Kommandozeile sicher leicht herausfinden; es wird etwas in der Art von `ls -la` sein.

Entfernen Sie aus der Auflistung händisch die Angabe des Pfadkopfs bis einschließlich des Projektnamens. Das ist übersichtlicher.

    \build.gradle
    \README.md
    \bin\main\public\index.html
    \bin\main\TicTacToe\App.kt
    \bin\main\TicTacToe\T3.kt
    \src\main\kotlin\TicTacToe\App.kt
    \src\main\kotlin\TicTacToe\T3.kt
    \src\main\resources\public\index.html

Fügen Sie dieses Resultat in Ihre Dokumentation wie oben gezeigt ein. Sollten Sie Dateien dabei haben mit einem Pfad wie z.B. `\.gradle\...` oder auch `\.git\...`, dann beachten Sie: Diese Dateien gehören nicht zur Abgabe und sollten auch nicht gelistet werden. Das sind Dateien, die Gradle, die Versionsverwaltung oder auch die Entwicklungsumgebung anlegen, die uns nicht interessieren.

**Zu den Lines of Code (LoC)**

Fügen Sie darüber hinaus die mit `cloc .` erzeugte Übersicht zu den Lines of Code in Ihrer Dokumentation ein; achten Sie wieder auf die Einrückung. Infos zur `cloc` finden Sie in der Bewertungsdokumentation.

> Die folgenden Kapitel orientieren sich in der Reihenfolge am Dokumentationsaufbau, so wie er in den Bewertungskriterien beschrieben ist.

## Dokumentation zur Spiel-Engine (ENG)

~~~
## Spiel-Engine (ENG)

Feature    | AB  | H+S | MC  | -   | B+I | Summe
-----------|-----|-----|-----|-----|------|----
Umsetzung  | 120 | 100 | 100 |   0 | 66.6 |
Gewichtung | 0.4 | 0.3 | 0.3 | 0.3 |  0.3 | 
Ergebnis   |  48 |  30 |  30 |   0 |   20 | **128%**

<Erläuterungen zur Ihrer Implementierung der Spiele-Engine>

~~~

Fügen Sie eine Kurzform der Bewertungstabelle für die Spiel-Engine ein, so wie oben gezeigt. Verwenden Sie folgende Abkürzungen: Minimax (M), Negamax (N), Alpha-Beta (AB), Hash-Map (H), Symmetrien (S), Kriterien (K), Monte-Carlo (M), eigene DB (eD), fremde DB (fD), Immutabilität (Im), Bitboards (B), Interface (I). Wenn ein "Plus" an Featuren erlaubt ist, verwenden Sie ein `+` zwischen den Abkürzungen.

Ergänzen Sie die Dokumentation mit Erläuterungen zur Ihrer Implementierung: Wo ist was in welchen Dateien zu finden? Welche Besonderheiten zeichnen Ihren Code aus?

Nehmen Sie diesen Teil der Dokumentation ernst. Die Spiel-Engine ist Ihr Herzstück.

## Dokumentation zu den Test-Szenarien

~~~
## Tests (TST)

Szenario |  1  |  2  |  3  |  4  |  5  | Summe
---------|-----|-----|-----|-----|-----|-------
ok       |  X  |  -  |  X  |  -  |  -  | 0.4

Die Tests werden wie folgt ausgeführt:

<Beschreiben Sie, wie die Tests auszuführen sind.>

Die Testausführung protokolliert sich über die Konsole wie folgt:

    <eingerückte Konsolenausgabe>
~~~

* Füllen Sie die obige Tabelle aus dem Bewertungsdokument aus.
* Ergänzen Sie die Beschreibung zur Ausführung der Tests.
* Fügen Sie eingerückt die geforderte Ausgabe auf der Konsole ein.

## Dokumentation der GUI

~~~
## Umsetzung der GUI

<Erläutern Sie die technische Umsetzung der GUI>
~~~

Ihnen ist die Gestaltung dieses Abschnitts frei gestellt.

## Optionale Hinweise und Quellenangaben

Der Abschnitt "Hinweise" ist optional (d.h. kein Muss). Allerdings sollten Sie hier dokumentieren und offenlegen, was wir zur Begutachtung und Bewertung Ihres Projekts wissen sollten. Gibt es Fälle, bei denen Ihr Programm merkwürdig reagiert oder Fehler produziert? Was auch immer: Legen Sie solche Probleme bitte offen und lassen Sie uns die Probleme nicht finden. Das schmälert sonst den Gesamteindruck.

~~~
## Hinweise

<Anmerkungen, die wichtig für die Begutachtung sind.>
~~~

Im letzten Kapitel sind die von Ihnen verwendeten Quellen aufzuführen, sofern Sie das nicht innerhalb der entsprechenden Abschnitte an Ort und Stelle getan haben. 

~~~
## Quellennachweis

* <Quelle A>
* <Quelle B>
~~~

Bitte geben Sie Links an, wann immer das bei einer Quellenangabe möglich und sinnvoll ist.

## Abgabe

> Benennen Sie Ihre `zip`-Datei nach Ihrem Nachnamen samt Matrikelnummer. Müsste ich eine Abgabe machen, so würde die Datei `Herzberg123456.zip` heißen.

Das Projekt ist im `zip`-Format in Moodle hochzuladen -- beachten Sie die dort angegebene Deadline; Sie können bis zur Deadline Ihre Abgabe beliebig oft aktualisieren. Die Verzeichnisstruktur orientiert sich an den für Gradle üblichen Konventionen. Abzuliefern ist ein auf das Nötigste bereinigte Verzeichnis aus Quellcode, Dokumentation und eventuellen Tests. Die Ausführung der Anwendung muss mit `gradle run` zu starten sein.

Sie dürfen selbstverständlich auch Ihren Programmcode mit Kommentaren und Anmerkungen versehen. Aber übertreiben Sie es nicht. Zuviel Dokumentation im Code schadet dem Überblick. Besser ist es oft, entscheidende Codeabschnitte im `README.md` zu besprechen und vorzustellen.

## Tipps und Hinweise

Mittlerweile haben wir einige Erfahrungen mit studentischen Abgaben gesammelt. Beherzigen Sie bitte die Hinweise und die Tipps:

* Bitte verwenden Sie das `>` am Anfang eines Absatzes nur dann, wenn Sie den Absatz farblich aus einem guten Grund hervorheben wollen. Wenn jeder Absatz hervorgehoben wird, ist das Markdown-Dokument nicht sehr angenehm zu lesen.
* Bitte verwenden Sie innerhalb Ihres Abgabeordners nur relative Links, wenn Sie auf Dateien in Ihrem Abgabeordner verweisen. Absolute Links sind eigentlich nur für URLs passend.
* Überprüfen Sie vor der Abgabe, ob die Links im `README.md` funktionieren. Verschieben Sie Ihren Abgabeordner auf Ihrem Rechner an einen anderen Ort im Dateisystem und probieren Sie dann, ob die Links noch funktionieren.
* Bitte auf gar keinen Fall Dateien abgeben, die das Ergebnis einer Kompilierung der Quellcode-Dateien (bzw. Testcode-Dateien) sind.
* Wenn Sie Ihr Projekt mit git verwaltet haben, entfernen Sie den meist unsichtbaren Ordner `.git` aus Ihrem Verzeichnis
* Eine Daumenregel: Wenn Ihre `zip`-Datei mehrere Hundert Kilobyte (KB) groß ist, dann haben Sie vermutlich Dateien mit eingepackt, die nicht zur Abgabe gehören.

Was zeichnet eine gute Dokumentation aus? Lesbarkeit, Verständlichkeit, eine klare Gliederung und Struktur, Korrektheit der Sachinformationen, eine Abwesenheit von Schreib- und Grammatikfehlern. Und: Die Dokumentation soll für fachinformierte Leser*innen geschrieben sein. Hilfreich, erklärend und dennoch auf den Punkt gebracht. Beispiele sind immer gut!

Viel Erfolg!