package com.example.WaterWise_app.ServiceImp;

import com.example.WaterWise_app.Entity.CoordinateEntity;
import com.example.WaterWise_app.Entity.CropEntity;
import com.example.WaterWise_app.Entity.FieldEntity;
import com.example.WaterWise_app.Repository.CropRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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

        double surfaceM2 = calculateFieldSurfaceM2(coords);
        double kc = getKcCoefficient(crop.getCropType(), crop.getGrowthStage());


        // 1. Récupérer ET des 7 derniers jours
        double etoToday = getTodayEtoFromApi(avgLat, avgLon);


        // 2. Récupérer précipitations pour aujourd’hui + 5 jours suivants (forecast 5 jours)
        Map<LocalDate, Double> precipitationValues = getPrecipitationFromApi(avgLat, avgLon);

        LocalDate today = LocalDate.now();



        List<DailyIrrigation> results = new ArrayList<>();

        for (int i = 0; i <= 5; i++) {
            LocalDate date = today.plusDays(i);
            double precipitation = precipitationValues.getOrDefault(date, 0.0);

            double adjustedET = etoToday - precipitation;
            if (adjustedET < 0) adjustedET = 0;

            double waterNeedLitres = adjustedET * kc * surfaceM2;
            results.add(new DailyIrrigation(date, waterNeedLitres));

        }
        System.out.println("ET0 today = " + etoToday);
        System.out.println("surfaceM2= " + surfaceM2);

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

    private double getTodayEtoFromApi(double lat, double lon) {
        try {
            LocalDate today = LocalDate.now();
            LocalDate targetDate = today.minusDays(7);  // 3 jours avant aujourd'hui
            String dateStr = targetDate.format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
            String startDate = dateStr;
            String endDate = dateStr;

            String url = String.format(Locale.US,
                    "https://power.larc.nasa.gov/api/temporal/daily/point" +
                            "?latitude=%.6f&longitude=%.6f&start=%s&end=%s&community=AG" +
                            "&parameters=T2M_MAX,T2M_MIN,ALLSKY_SFC_SW_DWN&format=JSON",
                    lat, lon, startDate, endDate);


            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Erreur API NASA POWER : " + response.getStatusCode());
            }

            String jsonResponse = response.getBody();
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Accéder aux données sous "properties" -> "parameter"
            JsonNode parameters = root.path("properties").path("parameter");

            JsonNode tMaxNode = parameters.path("T2M_MAX").path(startDate);
            JsonNode tMinNode = parameters.path("T2M_MIN").path(startDate);
            JsonNode radNode = parameters.path("ALLSKY_SFC_SW_DWN").path(startDate);

            if (tMaxNode.isMissingNode() || tMinNode.isMissingNode() || radNode.isMissingNode()) {
                throw new RuntimeException("Données manquantes pour calcul ET0");
            }

            double tMax = tMaxNode.asDouble();
            double tMin = tMinNode.asDouble();
            double radiation = radNode.asDouble();

            // Appliquer formule ET0 (Hargreaves)
            double et0 = 0.0023 * 0.408 * radiation * Math.sqrt(tMax - tMin) * ((tMax + tMin) / 2 + 17.8);

            return et0;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du calcul ET0 NASA POWER : " + e.getMessage(), e);
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
