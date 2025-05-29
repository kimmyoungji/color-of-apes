package com.example.colorofapes.controller;

import com.example.colorofapes.model.Participant;
import com.example.colorofapes.repository.ParticipantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    public ParticipantController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @GetMapping("/")
    public String showRegistrationForm(Model model) {
        model.addAttribute("participant", new Participant());
        return "registration";
    }

    private static final int MAX_LEDS = 64;  // 8x8 matrix
    private final Random random = new Random();

    @PostMapping("/register")
    public String registerParticipant(@ModelAttribute Participant participant) {
        // Find an unused random position
        Set<Integer> usedPositions = participantRepository.findAll().stream()
            .map(Participant::getLedPosition)
            .collect(Collectors.toSet());

        int position;
        if (usedPositions.size() >= MAX_LEDS) {
            // If all positions are taken, reuse a random existing position
            position = random.nextInt(MAX_LEDS);
        } else {
            // Find an unused random position
            do {
                position = random.nextInt(MAX_LEDS);
            } while (usedPositions.contains(position));
        }

        participant.setLedPosition(position);
        participantRepository.save(participant);
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "success";
    }

    @PostMapping("/participants/delete/{id}")
    public String deleteParticipant(@PathVariable Long id) {
        participantRepository.deleteById(id);
        return "redirect:/participants";
    }

    @GetMapping("/participants")
    public String listParticipants(Model model) {
        model.addAttribute("participants", participantRepository.findAll());
        return "participants";
    }
}
