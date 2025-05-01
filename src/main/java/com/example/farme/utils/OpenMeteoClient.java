package com.example.farme.utils;

import com.example.farme.model.Coordinate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component  /// for test
public class OpenMeteoClient {

    public static List<Double> getDailyPrecipitationData(List<Coordinate> coordinates) {
        // code temporaire de test
        return List.of(2.0, 1.5, 0.0, 0.8, 0.0, 3.2, 0.5);
    };

}
