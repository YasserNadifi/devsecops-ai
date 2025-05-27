package com.example.WaterWise_app;

import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.ServiceImp.OpenETService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenETServiceTest {

    private OpenETService openETService;

    @BeforeEach
    public void setUp() {
        openETService = new OpenETService();
    }

    // --- Tests pour getKcCoefficient ---

    @Test
    public void testGetKcCoefficient_knownCropAndStage() {
        double kc = openETService.getKcCoefficient("alfalfa", "adult");
        assertEquals(1.2, kc, 0.0001);

        kc = openETService.getKcCoefficient("onions", "seedling");
        assertEquals(0.7, kc, 0.0001);

        kc = openETService.getKcCoefficient("pears", "elderly");
        assertEquals(0.85, kc, 0.0001);
    }

    @Test
    public void testGetKcCoefficient_unknownCropOrStage_returnsDefault() {
        double kc = openETService.getKcCoefficient("unknowncrop", "adult");
        assertEquals(1.0, kc, 0.0001);

        kc = openETService.getKcCoefficient("alfalfa", "unknownstage");
        assertEquals(1.0, kc, 0.0001);
    }

    @Test
    public void testGetKcCoefficient_caseInsensitiveAndTrim() {
        double kc = openETService.getKcCoefficient("  Alfalfa  ", "ADULT");
        assertEquals(1.2, kc, 0.0001);
    }

    // --- Tests pour calculateFieldSurfaceM2 ---

     @Test
    void testCalculateFieldSurfaceM2_Rectangle() {
        // Rectangle de 1° latitude x 1° longitude autour de l'équateur
        List<CoordinateEntity> coords = List.of(
                createCoord(0, 0),
                createCoord(0, 1),
                createCoord(1, 1),
                createCoord(1, 0)
        );

        double surface = openETService.calculateFieldSurfaceM2(coords);

        // 1° lat = 111320 m, donc 111320 x 111320 = ~12_390_342_400 m²
        assertTrue(surface > 12_000_000_000.0 && surface < 13_000_000_000.0);
    }

    @Test
    void testCalculateFieldSurfaceM2_NotEnoughPoints() {
        List<CoordinateEntity> coords = List.of(
                createCoord(0, 0),
                createCoord(0, 1)
        );

        double surface = openETService.calculateFieldSurfaceM2(coords);
        assertEquals(0.0, surface);
    }

    @Test
    void testCalculateFieldSurfaceM2_LinePoints() {
        // 3 points alignés
        List<CoordinateEntity> coords = List.of(
                createCoord(0, 0),
                createCoord(0, 1),
                createCoord(0, 2)
        );

        double surface = openETService.calculateFieldSurfaceM2(coords);
        assertEquals(0.0, surface, 0.001); // surface nulle pour une ligne
    }

    

    private CoordinateEntity createCoord(double lat, double lon) {
        CoordinateEntity c = new CoordinateEntity();
        c.setLatitude(lat);
        c.setLongitude(lon);
        return c;
    }
}