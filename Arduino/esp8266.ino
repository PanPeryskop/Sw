#include <ESP8266WiFi.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebServer.h>

#define LAMP_PIN 4

// nazwa i hasło do accespointa
const char* ssid = "Opel Corso";
const char* password = "Maklowicz";

//kod html strony głównej
const char index_html[] PROGMEM = R"rawliteral(
<!DOCTYPE HTML><html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        @keyframes gradient {
            0% {background-position: 0% 50%;}
            50% {background-position: 100% 50%;}
            100% {background-position: 0% 50%;}
        }
        html {
            font-family: Arial;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
            background-size: 400% 400%;
            animation: gradient 15s ease infinite;
        }
        h1 {
            color: white;
            text-align: center;
            font-size: 3em;
        }
    </style>
</head>
<body>
    <h1>Arduining</h1>
</body>
</html>)rawliteral";

volatile bool flagey = false;
String codens;

AsyncWebServer server(80);

void setup() {

  Serial.begin(9600);

  pinMode(LAMP_PIN, OUTPUT);

  //ustawienia accesspointu o nazwie ustalonej na początku
  WiFi.softAP(ssid, password);

  IPAddress IP = WiFi.softAPIP();

  //strona główna
  server.on("/", HTTP_GET, [](AsyncWebServerRequest* request) {
    request->send_P(200, "text/html", index_html);
  });

  //przesyłanie kodu odbywa się tutaj za pomocą /sendcode?value=$kod
  server.on("/sendcode", HTTP_GET, [](AsyncWebServerRequest* request) {
    if (request->hasParam("value")) {
      String code = request->getParam("value")->value();
      Serial.println(code);
      codens = code;
      flagey = true;
    }
    request->send(200, "text/plain", "OK");
  });

  server.begin();
}

//jeśli ustawiona jest flaga mówiąca o tym że odebrano sygnał wywołujemy funkcję process i resetujemy flagę

void loop() {
  if (flagey) {
    process(codens);
    flagey = false;
  }
}

// 1: 0.1833 V - 0.55 V
// 2: 0.55 V - 0.9167 V
// 3: 0.9167 V - 1.2833 V
// 4: 1.2833 V - 1.65 V
// 5: 1.65 V - 2.0167 V
// 6: 2.0167 V - 2.3833 V
// 7: 2.3833 V - 2.75 V
// 8: 2.75 V - 3.1167 V
// 9: 3.1167 V - 3.3 V

void process(String code) {
  for (int i = 0; i < code.length(); i++) {
    char chr = code[i];
    int digit = code[i] - '0';
    float voltage = (digit + 0.5) / 10;
    int analogValue = voltage * 1023;
    Serial.println(analogValue);
    analogWrite(LAMP_PIN, analogValue);
    delay(1000);
  }
}