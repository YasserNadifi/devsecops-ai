package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class OpenETService {

    private static final String API_KEY = "zr96efKx5Lng5j7QlLyWUoDxWf0E76E6kuKiOUq0HRpJtCXvkIWkYCAlhLDG"; // ta clé API
    private static final String OPENET_URL = "https://openet-api.org/raster/timeseries/point";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<DailyIrrigation> calculateWeeklyWaterNeeds(CropEntity crop) {
        FieldEntity field = crop.getField();
        List<CoordinateEntity> coords = field.getCoordinates();

        // Moyenne des coordonnées
        double avgLat = coords.stream().mapToDouble(CoordinateEntity::getLatitude).average().orElse(0);
        double avgLon = coords.stream().mapToDouble(CoordinateEntity::getLongitude).average().orElse(0);

        // Surface approximative (m²)
        double surfaceM2 = 1000.0;

        // Coefficient cultural (Kc)
        double kc = getKcCoefficient(crop.getCropType(), crop.getGrowthStage());

        // Appel à l'API OpenET
        Map<LocalDate, Double> etoValues = getEtoValuesFromApi(avgLat, avgLon);

        List<DailyIrrigation> results = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : etoValues.entrySet()) {
            LocalDate date = entry.getKey();
            double eto = entry.getValue(); // mm/jour
            double waterNeedLitres = eto * kc * surfaceM2;
            results.add(new DailyIrrigation(date, waterNeedLitres));
        }

        return results;
    }

    private double getKcCoefficient(String cropType, String growthStage) {
        if (cropType.equalsIgnoreCase("Maïs")) {
            return switch (growthStage.toLowerCase()) {
                case "germination" -> 0.4;
                case "croissance" -> 0.7;
                case "floraison" -> 1.1;
                case "recolte" -> 0.7;
                default -> 1.0;
            };
        } else if (cropType.equalsIgnoreCase("blé")) {
            return switch (growthStage.toLowerCase()) {
                case "germination" -> 0.3;
                case "croissance" -> 0.5;
                case "floraison" -> 1.0;
                case "recolte" -> 0.6;
                default -> 1.0;
            };
        }
        return 1.0;
    }

    private Map<LocalDate, Double> getEtoValuesFromApi(double lat, double lon) {
        try {
            LocalDate startDate = LocalDate.now().minusDays(7);  // 7 jours avant aujourd’hui
            LocalDate endDate = LocalDate.now();

            Map<String, Object> payload = new HashMap<>();
            payload.put("date_range", Arrays.asList(startDate.toString(), endDate.toString()));
            payload.put("interval", "daily");
            // Longitude puis latitude !
            payload.put("geometry", Arrays.asList(lon, lat));
            payload.put("model", "Ensemble");
            payload.put("variable", "ET");  // ET = Evapotranspiration
            payload.put("reference_et", "gridMET");
            payload.put("units", "mm");
            payload.put("file_format", "JSON");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", API_KEY);  // clé brute, sans "Bearer "

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(payload), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    OPENET_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Erreur API OpenET : " + response.getStatusCode() + " " + response.getBody());
            }

            String jsonResponse = response.getBody();

            // La réponse est une liste JSON : [{"time":"2025-05-10","et":0.56}, ...]
            JsonNode root = objectMapper.readTree(jsonResponse);

            Map<LocalDate, Double> etoMap = new LinkedHashMap<>();
            for (JsonNode node : root) {
                String dateStr = node.path("time").asText(null);
                double eto = node.path("et").asDouble(Double.NaN);

                if (dateStr != null && !Double.isNaN(eto)) {
                    etoMap.put(LocalDate.parse(dateStr), eto);
                }
            }

            return etoMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur API OpenET : " + e.getMessage(), e);
        }
    }

    @Data
    public static class DailyIrrigation {
        private final LocalDate date;
        private final double waterInLitres;
    }
}
