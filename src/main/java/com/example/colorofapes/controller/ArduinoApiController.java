package com.example.colorofapes.controller;

import com.example.colorofapes.entity.ColorData;
import com.example.colorofapes.model.Participant;
import com.example.colorofapes.repository.ParticipantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/arduino")
@CrossOrigin // Enable CORS for Arduino access
public class ArduinoApiController {

    private final ParticipantRepository participantRepository;

    public ArduinoApiController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    private ColorData convertToColorData(Participant participant) {
        ColorData colorData = new ColorData();
        String hexColor = participant.getFavoriteColor();
        
        // Convert hex color to RGB
        int rgb = Integer.parseInt(hexColor.substring(1), 16);
        colorData.setR((rgb >> 16) & 0xFF);
        colorData.setG((rgb >> 8) & 0xFF);
        colorData.setB(rgb & 0xFF);
        
        // Convert LED position to x,y coordinates (assuming 8x8 matrix)
        int position = participant.getLedPosition();
        colorData.setX(position % 8);
        colorData.setY(position / 8);
        
        return colorData;
    }

    @GetMapping("/colors")
    public ResponseEntity<List<ColorData>> getColors() {
        List<ColorData> colorDataList = participantRepository.findAll().stream()
                .map(this::convertToColorData)
                .collect(Collectors.toList());
        return ResponseEntity.ok(colorDataList);
    }

    @PostMapping("/colors")
    public ResponseEntity<ColorData> saveColor(@RequestBody ColorData colorData) {
        // Currently, we don't need to save color data from Arduino
        // as we're using the participant data as the source of truth
        return ResponseEntity.ok(colorData);
    }

    @GetMapping("/colors/latest")
    public ResponseEntity<ColorData> getLatestColor() {
        List<ColorData> colorDataList = participantRepository.findAll().stream()
                .map(this::convertToColorData)
                .collect(Collectors.toList());
        
        return colorDataList.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(colorDataList.get(colorDataList.size() - 1));
    }
}
