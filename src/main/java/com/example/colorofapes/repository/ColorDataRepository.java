package com.example.colorofapes.repository;

import com.example.colorofapes.entity.ColorData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorDataRepository extends JpaRepository<ColorData, Long> {
    // 가장 최근 색상 데이터를 찾기 위한 메서드
    ColorData findTopByOrderByTimestampDesc();
}
