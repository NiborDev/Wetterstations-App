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

### Destinations erstellen
![Screenshot_20230112_180123](https://user-images.githubusercontent.com/38883662/212132181-d337de67-0e77-4faf-ba22-236070dee9e4.png)
![Screenshot_20230112_175923](https://user-images.githubusercontent.com/38883662/212132174-2cc12424-43d0-4ac2-9784-3a7fe4df7a96.png)


Um mit der Wetterstation-App zu verwenden, benötigen Sie Folgendes:
- Einen ESP32 Mikrocontroller mit den erforderlichen Sensoren zur Messung von Luftfeuchtigkeit, Temperatur und CO-Gehalt
- Eine Internetverbindung, um auf WeatherAPI zugreifen zu können
Die App wird automatisch die aktuellen Wetterdaten sowohl von Ihrem ESP32 als auch von WeatherAPI abrufen und anzeigen.

## Bibliotheken
- https://github.com/JamalMulla/ComposePrefs
- https://github.com/airbnb/lottie-android/
- https://github.com/google/accompanist
- https://github.com/raamcosta/compose-destinations
- https://github.com/square/retrofit
- https://github.com/coil-kt/coil
