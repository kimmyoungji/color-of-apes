#include <WiFiS3.h>
#include <ArduinoHttpClient.h>
#include <FastLED.h>
#include <Arduino_JSON.h>

#define LED_PIN     5
#define NUM_LEDS    64
#define BRIGHTNESS  64
#define COLOR_ORDER GRB
#define CHIPSET     WS2812B

CRGB leds[NUM_LEDS];

// WiFi credentials
const char* ssid = "kmjoyitwifi";
const char* password = "kmjoyit21045";

// Render deployment info
const char* host = "color-of-apes.onrender.com";
const int port = 443;

WiFiClient wifi;
HttpClient client = HttpClient(wifi, host, port);
char response[2048];  // Buffer for HTTP response

void setup() {
  Serial.begin(9600);
  while (!Serial) {
    ; // Wait for serial port to connect
  }
  Serial.println("Arduino board initialized");
  
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
  Serial.println("\nStarting connection to server...");
  
  Serial.println("Making HTTP GET request to /api/arduino/colors");
  client.get("/api/arduino/colors");
  
  int statusCode = client.responseStatusCode();
  String response = client.responseBody();
  
  Serial.print("Status code: ");
  Serial.println(statusCode);
  
  if (statusCode == 200) {
    Serial.println("Response: " + response);
    parseAndDisplay(response);
  } else {
    Serial.print("HTTP Error: ");
    Serial.println(statusCode);
    fill_solid(leds, NUM_LEDS, CRGB::Black);
    FastLED.show();
  }
}
}
}

void parseAndDisplay(String jsonStr) {
  Serial.println("Parsing JSON: " + jsonStr);
  
  JSONVar data = JSON.parse(jsonStr);
  
  if (JSON.typeof(data) == "undefined") {
    Serial.println("Failed to parse JSON");
    return;
  }
  
  Serial.println("JSON parsed successfully");
  Serial.print("Number of colors: ");
  Serial.println(data.length());
  
  fill_solid(leds, NUM_LEDS, CRGB::Black); // Clear all LEDs
  
  for (int i = 0; i < data.length(); i++) {
    Serial.print("Processing color ");
    Serial.println(i);
    
    int x = (int)data[i]["x"];
    int y = (int)data[i]["y"];
    int r = (int)data[i]["r"];
    int g = (int)data[i]["g"];
    int b = (int)data[i]["b"];
    
    Serial.print("Position (x,y): (");
    Serial.print(x);
    Serial.print(",");
    Serial.print(y);
    Serial.println(")");
    
    Serial.print("Color (r,g,b): (");
    Serial.print(r);
    Serial.print(",");
    Serial.print(g);
    Serial.print(",");
    Serial.print(b);
    Serial.println(")");
    
    if (x >= 0 && x < 8 && y >= 0 && y < 8) {
      int idx = xyToIndex(x, y);
      Serial.print("LED index: ");
      Serial.println(idx);
      leds[idx] = CRGB(r, g, b);
    } else {
      Serial.println("Position out of bounds");
    }
  }
  
  FastLED.show();
  Serial.println("LEDs updated");
}

int xyToIndex(int x, int y) {
  // ZigZag wiring
  if (y % 2 == 0) return y * 8 + x;
  else return y * 8 + (7 - x);
}
