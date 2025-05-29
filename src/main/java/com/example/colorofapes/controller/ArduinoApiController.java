package com.example.colorofapes.controller;

import com.example.colorofapes.entity.ColorData;
import com.example.colorofapes.repository.ColorDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arduino")
@CrossOrigin // Enable CORS for Arduino access
public class ArduinoApiController {

    private final ColorDataRepository colorDataRepository;

    public ArduinoApiController(ColorDataRepository colorDataRepository) {
        this.colorDataRepository = colorDataRepository;
    }

    @GetMapping("/colors")
    public ResponseEntity<List<ColorData>> getColors() {
        return ResponseEntity.ok(colorDataRepository.findAll());
    }

    @PostMapping("/colors")
    public ResponseEntity<ColorData> saveColor(@RequestBody ColorData colorData) {
        return ResponseEntity.ok(colorDataRepository.save(colorData));
    }

    @GetMapping("/colors/latest")
    public ResponseEntity<ColorData> getLatestColor() {
        ColorData latestColor = colorDataRepository.findTopByOrderByTimestampDesc();
        if (latestColor != null) {
            return ResponseEntity.ok(latestColor);
        }
        return ResponseEntity.notFound().build();
    }
}
