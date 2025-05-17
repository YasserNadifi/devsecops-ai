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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class OpenETService {

    private static final String OPENET_API_KEY = "zr96efKx5Lng5j7QlLyWUoDxWf0E76E6kuKiOUq0HRpJtCXvkIWkYCAlhLDG";
    private static final String OPENET_URL = "https://openet-api.org/raster/timeseries/point";

    private static final String WEATHER_API_KEY = "dc87fcbe174d8fdf21975befd687cc2b";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/forecast";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<DailyIrrigation> calculateWeeklyWaterNeeds(CropEntity crop) {
        FieldEntity field = crop.getField();
        List<CoordinateEntity> coords = field.getCoordinates();

        double avgLat = coords.stream().mapToDouble(CoordinateEntity::getLatitude).average().orElse(0);
        double avgLon = coords.stream().mapToDouble(CoordinateEntity::getLongitude).average().orElse(0);

        //double surfaceM2 = calculateFieldSurfaceM2(coords);
        double kc = getKcCoefficient(crop.getCropType(), crop.getGrowthStage());


        // 1. Récupérer ET des 7 derniers jours
        Map<LocalDate, Double> etoValues = getEtoValuesFromApi(avgLat, avgLon);

        // 2. Récupérer précipitations pour aujourd’hui + 5 jours suivants (forecast 5 jours)
        Map<LocalDate, Double> precipitationValues = getPrecipitationFromApi(avgLat, avgLon);

        LocalDate today = LocalDate.now();
        double etoToday = etoValues.getOrDefault(today, 0.0);


        List<DailyIrrigation> results = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            LocalDate date = today.plusDays(i);
            double precipitation = precipitationValues.getOrDefault(date, 0.0);

            double adjustedET = etoToday - precipitation;
            if (adjustedET < 0) adjustedET = 0;

            double waterNeedLitres = adjustedET * kc * 10;
            results.add(new DailyIrrigation(date, waterNeedLitres));

        }


        return results;
    }





    private double calculateFieldSurfaceM2(List<CoordinateEntity> coordinates) {
        if (coordinates == null || coordinates.size() < 3) {
            // Pas assez de points pour former un polygone
            return 0;
        }

        // Constantes pour conversion degrés -> mètres
        // 1 degré latitude ≈ 111 320 m
        final double metersPerDegreeLat = 111320.0;

        // Utiliser la latitude moyenne pour corriger longitude
        double avgLat = coordinates.stream().mapToDouble(CoordinateEntity::getLatitude).average().orElse(0);
        double metersPerDegreeLon = 111320.0 * Math.cos(Math.toRadians(avgLat));

        // Convertir les points en coordonnées x, y en mètres
        double[] x = new double[coordinates.size()];
        double[] y = new double[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            CoordinateEntity coord = coordinates.get(i);
            x[i] = coord.getLongitude() * metersPerDegreeLon;
            y[i] = coord.getLatitude() * metersPerDegreeLat;
        }

        // Calcul surface polygone avec la formule du shoelace
        double area = 0.0;
        int n = coordinates.size();
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += x[i] * y[j] - x[j] * y[i];
        }
        area = Math.abs(area) / 2.0;

        return area; // en mètres carrés
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
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now();

            Map<String, Object> payload = new HashMap<>();
            payload.put("date_range", Arrays.asList(startDate.toString(), endDate.toString()));
            payload.put("interval", "daily");
            payload.put("geometry", Arrays.asList(lon, lat)); // longitude, latitude !
            payload.put("model", "Ensemble");
            payload.put("variable", "ET");
            payload.put("reference_et", "gridMET");
            payload.put("units", "mm");
            payload.put("file_format", "JSON");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", OPENET_API_KEY);

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

    /**
     * Récupère la précipitation quotidienne prévue (en mm) à partir de l'API OpenWeatherMap.
     *
     * @param lat latitude
     * @param lon longitude
     * @return map LocalDate -> précipitation en mm
     */
    private Map<LocalDate, Double> getPrecipitationFromApi(double lat, double lon) {
        try {
            String url = WEATHER_API_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + WEATHER_API_KEY + "&units=metric";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Erreur API météo : " + response.getStatusCode() + " " + response.getBody());
            }

            String jsonResponse = response.getBody();
            JsonNode root = objectMapper.readTree(jsonResponse);

            // OpenWeatherMap renvoie un tableau 'list' avec des prévisions toutes les 3h
            JsonNode listNode = root.path("list");

            Map<LocalDate, Double> precipitationMap = new HashMap<>();

            // On agrège la précipitation par jour
            for (JsonNode forecastNode : listNode) {
                long timestamp = forecastNode.path("dt").asLong();
                LocalDate date = Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC).toLocalDate();

                double rainVolume = 0.0;
                if (forecastNode.has("rain") && forecastNode.path("rain").has("3h")) {
                    rainVolume = forecastNode.path("rain").path("3h").asDouble(0.0);
                }

                // Somme des précipitations du jour (en mm)
                precipitationMap.put(date, precipitationMap.getOrDefault(date, 0.0) + rainVolume);
            }

            return precipitationMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur API météo : " + e.getMessage(), e);
        }
    }

    @Data
    public static class DailyIrrigation {
        private final LocalDate date;
        private final double waterInLitres;
    }
}
