# Arduino LED Control Setup

This guide explains how to set up the Arduino component of the Color of Apes project.

## Hardware Requirements

1. ESP8266-based board (NodeMCU or similar)
2. WS2812B LED strip (50 LEDs)
3. 5V power supply (capacity depends on number of LEDs)
4. Connecting wires

## Wiring Instructions

1. Connect the LED strip to the ESP8266:
   - Connect GND of LED strip to GND of ESP8266
   - Connect 5V of LED strip to 5V power supply
   - Connect DIN (Data In) of LED strip to D4 (GPIO2) of ESP8266

## Software Setup

1. Install required Arduino libraries:
   - ESP8266WiFi
   - ESP8266HTTPClient
   - ArduinoJson (version 6 or later)
   - Adafruit_NeoPixel

2. Configure the sketch:
   - Update `ssid` and `password` with your WiFi credentials
   - Update `serverUrl` with your computer's IP address
   - Adjust `LED_PIN` if you're using a different GPIO pin
   - Adjust `BRIGHTNESS` if needed (lower it if you're having power issues)

## Power Considerations

- Each WS2812B LED can draw up to 60mA at full brightness (20mA per color)
- For 50 LEDs, maximum theoretical current draw: 50 * 60mA = 3A
- Recommended power supply: 5V, 3A or higher
- The code sets brightness to 50 (out of 255) to reduce power consumption

## Troubleshooting

1. If LEDs flicker or show wrong colors:
   - Add a 470Ω resistor between the data pin and LED strip
   - Add a large capacitor (1000µF) between 5V and GND
   - Keep data wires short and away from noise sources

2. If can't connect to WiFi:
   - Double-check WiFi credentials
   - Ensure ESP8266 is within range of WiFi router
   - Check if your WiFi is 2.4GHz (ESP8266 doesn't support 5GHz)

3. If can't connect to server:
   - Verify server IP address is correct
   - Ensure server is running and accessible
   - Check if firewall is blocking connections
