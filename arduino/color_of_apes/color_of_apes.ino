#include <WiFiS3.h>            // Uno R4 WiFi용
#include <FastLED.h>
#include <ArduinoHttpClient.h>
#include <Arduino_JSON.h>      // JSON 파싱용

#define LED_PIN     5
#define NUM_LEDS    64
#define BRIGHTNESS  64
#define COLOR_ORDER GRB
#define CHIPSET     WS2812B

CRGB leds[NUM_LEDS];

const char* ssid = "kmjoyitwifi";
const char* password = "kmjoyit21045";

// Spring Boot 서버 정보
const char serverAddress[] = "192.168.50.148";  // PC의 IP 주소 (localhost 아님!)
int port = 8082;
const char requestPath[] = "/api/arduino/colors";

WiFiClient wifi;
HttpClient client = HttpClient(wifi, serverAddress, port);

void setup() {
  Serial.begin(9600);
  delay(1000);
  FastLED.addLeds<CHIPSET, LED_PIN, COLOR_ORDER>(leds, NUM_LEDS);
  FastLED.setBrightness(BRIGHTNESS);
  connectToWiFi();
}

void loop() {
  getColorDataFromServer();
  delay(3000);  // 3초 주기
}

void connectToWiFi() {
  Serial.print("Connecting to WiFi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println(" connected!");
  Serial.println(WiFi.localIP());
}

void getColorDataFromServer() {
  client.get(requestPath);

  int statusCode = client.responseStatusCode();
  String response = client.responseBody();

  if (statusCode == 200) {
    Serial.println("Received: " + response);
    parseAndDisplay(response);
  } else {
    Serial.print("HTTP Error: ");
    Serial.println(statusCode);
  }
}

void parseAndDisplay(String jsonStr) {
  JSONVar data = JSON.parse(jsonStr);

  Serial.println("JSON parsed: ");
  Serial.println(data);

  if (JSON.typeof(data) == "undefined") {
    Serial.println("Failed to parse JSON");
    return;
  }

  fill_solid(leds, NUM_LEDS, CRGB::Black); // 초기화

  for (int i = 0; i < data.length(); i++) {
    int x = (int)data[i]["x"];
    int y = (int)data[i]["y"];
    int r = (int)data[i]["r"];
    int g = (int)data[i]["g"];
    int b = (int)data[i]["b"];

    if (x >= 0 && x < 8 && y >= 0 && y < 8) {
      int idx = xyToIndex(x, y);
      leds[idx] = CRGB(r, g, b);
    }
  }

  FastLED.show();
}

int xyToIndex(int x, int y) {
  // ZigZag wiring
  if (y % 2 == 0) return y * 8 + x;
  else return y * 8 + (7 - x);
}
