package com.example.colorofapes.dto;

import lombok.Data;

@Data
public class ArduinoColorData {
    private int x;  // 0-7 range
    private int y;  // 0-7 range
    private int r;
    private int g;
    private int b;
    
    public static ArduinoColorData fromParticipant(String hexColor, int position) {
        // Remove the '#' if present
        String cleanHex = hexColor.replace("#", "");
        
        ArduinoColorData data = new ArduinoColorData();
        // Convert position to x,y coordinates (0-7 range)
        data.x = position % 8;
        data.y = position / 8;
        
        // Parse RGB values from hex
        data.r = Integer.parseInt(cleanHex.substring(0, 2), 16);
        data.g = Integer.parseInt(cleanHex.substring(2, 4), 16);
        data.b = Integer.parseInt(cleanHex.substring(4, 6), 16);
        
        return data;
    }
}
