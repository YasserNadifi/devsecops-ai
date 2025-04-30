package com.example.farme.utils;


import com.example.farme.model.Coordinate;
import java.util.List;

public class GeoUtils {

    // Rayon moyen de la Terre en mètres
    private static final double EARTH_RADIUS = 6371000;

    public static double calculateArea(List<Coordinate> coordinates) {
        if (coordinates == null || coordinates.size() < 3) return 0;

        double area = 0.0;
        int n = coordinates.size();

        for (int i = 0; i < n; i++) {
            Coordinate p1 = coordinates.get(i);
            Coordinate p2 = coordinates.get((i + 1) % n);

            double lat1 = Math.toRadians(p1.getLatitude());
            double lon1 = Math.toRadians(p1.getLongitude());
            double lat2 = Math.toRadians(p2.getLatitude());
            double lon2 = Math.toRadians(p2.getLongitude());

            area += (lon2 - lon1) * (2 + Math.sin(lat1) + Math.sin(lat2));
        }

        area = area * EARTH_RADIUS * EARTH_RADIUS / 2.0;
        return Math.abs(area); // m²
    }
}
