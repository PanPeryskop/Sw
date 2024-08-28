# Projekt na Arduino

## 

## Wstęp

Projekt jest zintegrowany z projektem Szymona Czarneckiego. Pozwala na sterowanie sejfem za pomocą aplikacji mobilnej. Następnie esp8266 komunikuje się z arduino uno za pomocą autorskiego protokołu oneWire by przesłać kod. 
Projekt składa się z dwóch głównych części: kodu uruchamianego na mikrokontrolerze ESP8266 napisanego w C++ i html oraz aplikacji Android napisanej w Kotlinie.

## Mikrokontroler ESP8266

Program uruchomiony na ESP8266 to `esp8266.ino`. Jest odpowiedzialny za komunikację z wykorzystaniem sieci WiFi i obsługę żądań HTTP. Umożliwia on przekazywanie informacji za pomocą żądań HTTP wysyłanych z aplikacji Android. Kod jest przesyłany jako parametr żądania HTTP i jest następnie przetwarzany na napięcie, które jest przekazywane do arduino uno.

## Arduino Uno

Program uruchomiony na Arduino Uno to `arduino.ino`. Jest odpowiedzialny za odbieranie ciągów napięć od ESP8266 i zamiany go na kod.

## Aplikacja Android

Aplikacja Android, napisana w Kotlinie przy użyciu `Jetpack Compose` pełni rolę UI. Umożliwia ona wprowadzanie kodu, który jest następnie wysyłany do mikrokontrolera ESP8266 za pomocą żądań HTTP.

Aplikacja korzysta z sieci WiFi do komunikacji z mikrokontrolerem. W tym celu wykorzystuje ona usługę `WifiManager` do zarządzania połączeniem WiFi. Po uruchomieniu aplikacji, jeśli WiFi nie jest włączone, użytkownik jest przekierowywany do ustawień WiFi, aby je włączyć. Po włączeniu WiFi, aplikacja automatycznie łączy się z siecią WiFi utworzoną przez mikrokontroler ESP8266.

Aplikacja posiada weryfikację długości wprowadzanego tekstu:
<img src="https://lh3.googleusercontent.com/pw/AP1GczO2-pLTo3GENgruYWnFM-6dbttMb6A4VmjRAZCiSR4rOTNkWghjfBUjuRpZ_5vbHzbfsYUtqC97601ig5kmUkn9vAO1XfDNkKju9hUsc7GTS7srKrUXcMNMgo_ICZyee7T8XsQ3xyHmFS8--_kJOpWS=w437-h947-s-no">
## Wykorzystane biblioteki

Arduino:
- `ESP8266WiFi.h` 
- `ESPAsyncTCP.h`
- `ESPAsyncWebServer.h`

Kotlin:
- `android.content.Context`
- `android.content.Intent`
- `android.content.BroadcastReceiver`
- `android.content.IntentFilter`
- `android.net.wifi.WifiNetworkSuggestion`
- `android.net.wifi.WifiManager`
- `android.os.Bundle`
- `android.provider.Settings`
- `android.widget.Toast`
- `androidx.activity.ComponentActivity`
- `androidx.activity.compose.setContent`
- `androidx.compose.runtime.*`
- `java.net.HttpURLConnection`
- `java.net.URL`


## Podsumowanie

Projekt Arduinin to kompleksowe rozwiązanie IoT, które łączy mikrokontroler ESP8266 z aplikacją Android za pomocą sieci WiFi. Pozwala na zdalne sterowanie lampką za pomocą kodu wprowadzanego w aplikacji. Kod jest przesyłany do mikrokontrolera, który przetwarza go na napięcie i przekazuje do lampki, umożliwiając jej sterowanie.