#define INPUT_PIN A0

void setup() {
  Serial.begin(9600);
  pinMode(INPUT_PIN, INPUT);
}

void loop() {
  int analogValue = analogRead(INPUT_PIN);
  float voltage = analogValue * 3.3 / 1023;
  if (voltage > 2){
    String codens = getCodens();
  }
}

String getCodens() {
    delay(1000);
    String codens = "";
    for (int i = 0; i < 4; i++) {
        int analogValue = analogRead(INPUT_PIN);
        float voltage = analogValue * 3.3 / 1023;
        int code = dekodens(voltage);
        char codeChar = code + '0';
        codens += codeChar;
        delay(1000);
    }
    return codens;
}

int dekodens(float voltage) {
  if (voltage < 0.55) return 1;
  else if (voltage < 0.9167) return 2;
  else if (voltage < 1.2833) return 3;
  else if (voltage < 1.65) return 4;
  else if (voltage < 2.0167) return 5;
  else if (voltage < 2.3833) return 6;
  else if (voltage < 2.75) return 7;
  else if (voltage < 3.1167) return 8;
  else return 9;
}