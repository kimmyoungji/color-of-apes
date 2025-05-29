package com.example.colorofapes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ColorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int x;
    private int y;
    private int r;
    private int g;
    private int b;
    
    // 생성 시간을 자동으로 저장
    private long timestamp = System.currentTimeMillis();
}
