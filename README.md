# Projekt na Arduino

Projekt jest zintegrowany z projektem Szymona Czarneckiego. Pozwala na sterowanie sejfem za pomocą aplikacji mobilnej. Następnie esp8266 komunikuje się z arduino uno za pomocą autorskiego protokołu oneWire by przesłać kod. 
Projekt składa się z dwóch głównych części: kodu uruchamianego na mikrokontrolerze ESP8266 napisanego w C++ i html oraz aplikacji Android napisanej w Kotlinie.

## Mikrokontrolery

Mikrokontroler: ESP8266 NodeMCU V3

<img src="https://a.allegroimg.com/s720/11ebb2/96aa321f471b9d865260ebe84031/Modul-WiFi-ESP8266-NodeMcu-V3-IoT.jpg" width="200" height="200">

Wybrałem ten mikrokontroler ze względu na wbudowany moduł WiFI, który otworzył mi wiele możliwości. Dodatkowym atutem była niska cena i dobra wydajność.

Mikrokontroler: Arduino Uno

img src="https://neorobot.pl/environment/cache/images/500_500_productGfx_5443/Arduino-UNO-Rev3---oryginal.webp" width="200" height="200">

Ten mikrokontroler został zakupiony przez Szymona, więc musiałem się dostosować :).

## Mikrokontroler ESP8266

Program uruchomiony na ESP8266 to `esp8266.ino`. Jest odpowiedzialny za komunikację z wykorzystaniem sieci WiFi i obsługę żądań HTTP. Umożliwia on przekazywanie informacji za pomocą żądań HTTP wysyłanych z aplikacji Android. Kod jest przesyłany jako parametr żądania HTTP i jest następnie przetwarzany na napięcie, które jest przekazywane do arduino uno.

## Arduino Uno

Program uruchomiony na Arduino Uno to `arduino.ino`. Jest odpowiedzialny za odbieranie ciągów napięć od ESP8266 i zamiany go na kod.

## Aplikacja Android

Wygląd aplikacji

<img src=https://github.com/PanPeryskop/Sw/blob/main/Images/interface.jpg">

Aplikacja Android, napisana w Kotlinie przy użyciu `Jetpack Compose` pełni rolę UI. Umożliwia ona wprowadzanie kodu, który jest następnie wysyłany do mikrokontrolera ESP8266 za pomocą żądań HTTP.

Aplikacja korzysta z sieci WiFi do komunikacji z mikrokontrolerem. W tym celu wykorzystuje ona usługę `WifiManager` do zarządzania połączeniem WiFi. Po uruchomieniu aplikacji, jeśli WiFi nie jest włączone, użytkownik jest przekierowywany do ustawień WiFi, aby je włączyć. Po włączeniu WiFi, aplikacja automatycznie łączy się z siecią WiFi utworzoną przez mikrokontroler ESP8266.

Aplikacja posiada weryfikację długości wprowadzanego tekstu

<img src="https://github.com/PanPeryskop/Sw/blob/main/Images/verif.jpg">

## Schamat

Schemat jest bardzo prosty, gdyż projekt opiera się na komunikacji przez sieć WiFi.

<img src="https://github.com/PanPeryskop/Sw/blob/main/Images/UI.jpg">

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