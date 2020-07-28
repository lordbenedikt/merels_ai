# Mühle mit KI
Mühle ist ein in Deutschland weit verbreitetes Strategiespiel. Dieses Programm ist eine Umsetzung des Spiels mit Kotlin,
Javascript und HTML/CSS. Es ist möglich gegen einen Computerspieler anzutreten, welcher algorithmisch den besten Spielzug
berechnet.
###Spielregeln
Weiß spielt traditionell immer zuerst. In der ersten Spielphase wird abwechselnd je ein Spielstein auf ein freies Feld 
gesetzt, bis insgesamt je 9 Spielsteine eingesetzt wurden. In der nächsten Phase wird stattdessen je Zug ein eigener Spielstein auf
ein angrenzendes freies Feld bewegt. Wenn zu irgendeinem Zeitpunkt im Spielsteine eine Dreierreihe, genannt eine Mühle, 
bilden, kann der Spieler, welcher diese Reihe gebildet hat, einen Spielstein des Gegners aus dem Spiel nehmen. Dabei ist 
es nicht erlaubt einen Stein aus einer Mühle zu nehmen. Befindet sich allerdings jeder der gegnerischen Spielsteine in
einer Mühle, wird diese Regel ungültig und der zu nehmende Spielstein darf frei gewählt werden.
Sobald ein Spieler nur noch drei Spielsteine auf dem Feld hat, ist es ihm erlaubt mit seinen Spielsteinen auf ein 
beliebiges freies Feld zu springen, anstatt nur auf angrenzende Felder zu ziehen. Das Spiel endet, wenn einem Spieler 
entweder kein gültiger Zug mehr möglich ist, oder er weniger als drei Spielsteine übrig hat. 
Der jeweilige Spieler verliert das Spiel.

Unentschieden: Das Spiel endet auch, wenn dreimal hintereinander, die gleiche Abfolge von Spielzügen gemacht hat, oder
wenn 20 Spielzüge gemacht wurden, ohne das sich eine Mühle gebildet hat. In beiden Fällen endet das Spiel mit 
unentschieden.

###Spielsteuerung
Das Spiel wird ausschließlich mit der Maus gesteuert. Durch Anklicken eines Spielsteins, wird dieser angewählt. Durch
Klicken auf ein Spielfeld, wird der gewählte Spielstein auf dieses Feld gezogen, soweit gültig. Eingesetzt werden 
Spielsteine, durch Klicken auf ein freies Feld. Genommen werden Spielsteine durch Anklicken des jeweiligen Spielsteins.
Auf der rechten Seite befinden sich Buttons mit verschiedenen Funktionen:

- Best Move: Berechnet und spielt den bestmöglichen Zug des aktuellen Spielers
- Random: Macht einen zufälligen Zug
- Restart: Startert ein neues Spiel

Außerdem verrät die Anzeige, wer am Zug ist, welche Art von Spielzug gemacht werden muss, wie viele Spielsteine beide
Spieler auf dem Feld haben und eine heuristische Evaluation der Spielsituation.

###Alphabeta-Algorithmus
Die Grundstruktur des Alphabeta-Algorithmus habe ich aus Wikipedia entnommen und zu funktionierendem Kotlin-Code
umgeschrieben. Aufgerufen wird er per Funktion alphabeta(). Durch Anpassung der Variable maxDepth lässt die Suchtiefe 
festlegen. Beispiel: Für den Wert maxDepth = 3 simuliert der Algorithmus von der momentanen Spielstellung ausgehend 
jeweils einen Zug beider Spieler und gibt die Bewertung der resultierenden Spielstellung zurück(Bewertung durch 
Monte-Carlo oder Heuristik). Erreicht der Algorithmus eine Spielestellung, welche das Spiel beendet, dann wird die 
Bewertung außerdem mit (1 + maxDepth - derzeitigeSuchtiefe) multipliziert. Dies gewährleistet, dass bei nahendem 
Spielende immer der schnellste Weg zum Sieg gewählt wird bzw. die Niederlage möglichst lange hinausgezögert wird.<br>
Die Funktion bestMove() bewertet alle unmittelbar erreichbaren Spielstellung anhand von alphabeta() und gibt den
besten Zug zurück(Klasse Move).

###Hashing
Einmal mit Alphabeta bewertete Spielstellungen werden in einer Hashmap gespeichert. Dabei werden spieltechnisch
equivalente Positionen nur einmal gespeichert. Mit equivalent sind Positionen gemeint, welche durch Spiegeln, Drehen und
Invertieren des Spielfeldes erreicht werden gemeint. Zu beachten ist, das bei Muehle der innere und der äußere Ring des
Spielfeldes ebenfalls gleichwertig sind. Dazu kommt noch, dass weiß und schwarz vertauscht werden können, soweit es sich
nicht um die Anfangsphase handelt, in welcher Spielsteine eingesetzt werden. Werden all diese Punkte beachtet, reduziert
sich die Anzahl an möglichen Spielpositionen nahezu auf ein Zweiunddreißigstel. Damit lässte sich eine Menge Speicher
und Rechenleistung einsparen.<br>
Die Hashfunktion berechnet zu gegebener Spielposition eine Long Zahl. In einer Map wird jeder Hashkey zusammen mit der
Bewertung abgespeichert. Im Falle, dass die Position schon einmal berechnet wurde, ruft der Alphabeta Algorithmus diesen 
Wert ab anstatt eine neue Bewertung zu starten.<br>
Der Hashkey ergibt sich aus den Spielsteinen des aktiven Spielers, denen des Gegenspielers und der Anzahl an noch
einzusetzenden Spielsteine. Der Alphabeta-Algorithmus sieht positive Zahlen als gut an, wenn weiß am Zug ist, bevorzugt
allerdings negative Zahlen, wenn schwarz am Zug ist. In der Hashmap jedoch, werden alle Werte so abgespeichert, dass
positive Zahlen eine gute Stellung für den aktiven Spieler bedeuten. Wird also eine Position, bei welcher schwarz am Zug
ist bewertet, wird sie vor dem Speichern in der Hashmap mit (-1) multipliziert.

###Tests
Folgende Abbildung stellt das Spielfeld mit den nummerierten Feldern dar:

    O--------1--------2
    |  8-----9-----1O |
    |  |  16-17-18 |  |
    7--15-23    19-11-3
    |  |  22-21-20 |  |
    |  14----13----12 |
    6--------5--------4

Spielzüge werden in folgender Form in der Konsole ausgegeben: 
    
    Move: 0 -> 1 (8)
    
Das Beispiel repräsentiert einen Spielzug, bei welchem ein eigener Spielstein von Feld 0 auf Feld 1 gesetzt wird und
ein gegnerischer Spielstein von Feld 8 aus dem Spiel genommen wird. Es ist anzumerken, dass die Standardeinstellung für
die Sichttiefe des Alphabeta-Algorithmus 3 ist. Für die Tests wird sie jedoch auf die jeweils notwendige Tiefe gesetzt.
Es folgt eine Schilderung der 5 verfügbaren Tests.

- Test 1: Spieler schwarz spielt den Gewinnzug, indem er einen eigenen Spielstein auf Feld 2 zieht.
- Test 2: Spieler schwarz ist am Zug und verhindert eine unmittelbare Bedrohung durch Spieler weiß, indem er auf Feld
          15 springt.
- Test 3: Spieler schwarz sichert sich den Sieg im übernächsten Zug durch Bilden von 2 Zweierreihen. Weiß kann im
Folgezug nur eine davon blockieren.
- Test 4: Spieler schwarz ist am Zug und verhindert eine Niederlage im übernächsten Zug des Gegners. Das ist nur möglich
durch blockieren von Feld 9 oder 10. (Bei Sichttiefe 4 wird immer Feld 10 blockiert. Dass liegt daran, dass obwohl 
Blockieren von Feld 9 die Niederlage verhindert, ein solcher Spielzug schlechter bewertet wird.)
- Test 5: Spieler schwarz ist am Zug. Keiner der Spieler hat eine Zweierreihe. Durch geschickte Spielzüge erlangt
schwarz erst 2 Zweierreihen und spielt dann den Gewinnzug.

###Genutzte Quellen
https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning <br>
(Den Pseudocode zum Alphabeta-Algorithmus habe ich übernommen
und darauf aufbauend meinen fertigen Algorithmus geschrieben)

Herzberg Dominikus, BitBoard.Mühle.png: Erklärung zu Bitboars für Mühle auf Slack, 3.Juli 2020

http://zetcode.com/kotlin/writefile/ (zu schreiben und lesen von txt-Dokumenten in Kotlin)

