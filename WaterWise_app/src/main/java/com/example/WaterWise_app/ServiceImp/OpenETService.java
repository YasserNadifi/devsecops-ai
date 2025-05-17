package com.example.WaterWise_app.ServiceImp;



import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.*;

@Service
public class OpenETService {

    private static final String API_KEY = "YOUR_API_KEY"; // remplace par ta clé API si nécessaire
    private static final String OPENET_BASE_URL = "https://api.openetdata.org/eto";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<DailyIrrigation> calculateWeeklyWaterNeeds(CropEntity crop) {
        FieldEntity field = crop.getField();
        List<CoordinateEntity> coords = field.getCoordinates();

        // Moyenne des coordonnées
        double avgLat = coords.stream().mapToDouble(CoordinateEntity::getLatitude).average().orElse(0);
        double avgLon = coords.stream().mapToDouble(CoordinateEntity::getLongitude).average().orElse(0);

        // Surface approximative (à remplacer si tu la calcules réellement)
        double surfaceM2 = 1000.0;

        // Coefficient cultural selon le type et stade
        double kc = getKcCoefficient(crop.getCropType(), crop.getGrowthStage());

        // Appel API pour 7 jours
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
            switch (growthStage.toLowerCase()) {
                case "germination": return 0.4;
                case "croissance": return 0.7;
                case "floraison": return 1.1;
                case "recolte": return 0.7;
            }
        } else if (cropType.equalsIgnoreCase("blé")) {
            switch (growthStage.toLowerCase()) {
                case "germination": return 0.3;
                case "croissance": return 0.5;
                case "floraison": return 1.0;
                case "recolte": return 0.6;
            }
        }
        return 1.0; // valeur par défaut
    }

    private Map<LocalDate, Double> getEtoValuesFromApi(double lat, double lon) {
        try {
            // Construire l'URL avec les paramètres (adapté à ta vraie API)
            String url = UriComponentsBuilder.fromHttpUrl(OPENET_BASE_URL)
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("days", 7)
                    .queryParam("api_key", API_KEY)
                    .toUriString();

            // Appel HTTP GET
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // Parser la réponse JSON
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Supposons que la réponse contient un tableau 'daily' avec des objets { date: "yyyy-MM-dd", eto: float }
            JsonNode dailyArray = root.path("daily");

            Map<LocalDate, Double> etoMap = new LinkedHashMap<>();

            for (JsonNode dayNode : dailyArray) {
                String dateStr = dayNode.path("date").asText();
                double eto = dayNode.path("eto").asDouble();

                LocalDate date = LocalDate.parse(dateStr);
                etoMap.put(date, eto);
            }

            return etoMap;

        } catch (Exception e) {
            e.printStackTrace();
            // En cas d'erreur, retourner des valeurs par défaut simulées
            return getFallbackEtoValues();
        }
    }

    // Fallback en cas d'erreur d'appel API
    private Map<LocalDate, Double> getFallbackEtoValues() {
        Map<LocalDate, Double> etoMap = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            etoMap.put(today.plusDays(i), 4.0 + Math.random()); // 4.0 à 5.0 mm/jour simulé
        }

        return etoMap;
    }

    @Data
    public static class DailyIrrigation {
        private final LocalDate date;
        private final double waterInLitres;
    }
}
