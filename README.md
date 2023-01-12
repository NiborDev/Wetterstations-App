# Wetterstation-App

Willkommen bei der Wetterstation-App, entwickelt für ein Schulprojekt (KS-DILL, Klasse 13BG).

Diese App ist dafür konzipiert, um mit einem ESP32 Mikrocontroller über Bluetooth Low Energy Daten auszutauschen.
Mit der App können Daten wie zum Beispiel Luftfeuchtigkeit, Temperatur und CO-Gehalt vom ESP32 gelesen werden.
Zusätzlich nutzt die App auch einer API, um externe Wetterdaten vorallem für Außen zuerhalten und anzuzeigen.
Dafür verwendet die App die WeatherAPI (https://www.weatherapi.com/).

## Funktionen
- Echtzeit-Anzeige von Luftfeuchtigkeit, Temperatur und CO-Gehalt (DHT11, MQ7)
- Bluetooth Low Energy-Verbindung mit einem ESP32 Mikrocontroller
- Integration mit WeatherAPI zur Empfang von externen Wetterdaten
- Anzeige von WetterStationen
- Anzeige von Netzwerke in der Nähe

## Einrichtung

Um mit der Wetterstation-App zu verwenden, benötigen Sie Folgendes:
- Einen ESP32 Mikrocontroller mit den erforderlichen Sensoren zur Messung von Luftfeuchtigkeit, Temperatur und CO-Gehalt
- Eine Internetverbindung, um auf WeatherAPI zugreifen zu können
Sobald Sie alles haben, können Sie die Wetterstation-App aus dem PlayStore herunterladen und sie mit Ihrem ESP32 verbinden.
Die App wird automatisch die aktuellen Wetterdaten sowohl von Ihrem ESP32 als auch von WeatherAPI abrufen und anzeigen.
