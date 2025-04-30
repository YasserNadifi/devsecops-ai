package com.example.farme.utils;

import com.example.farme.model.Coordinate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class OpenETClient {

    private static final String API_URL = "https://openet-api-url.example.com/evapotranspiration"; // ici tu mets la vraie URL de OpenET

    private final RestTemplate restTemplate;

    public OpenETClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Double> getDailyETData(List<Coordinate> coordinates) {
        // 1. Construire la requête HTTP
        double latitude = coordinates.get(0).getLatitude(); // pour simplifier, on prend premier point
        double longitude = coordinates.get(0).getLongitude();

        String url = API_URL + "?lat=" + latitude + "&lon=" + longitude;

        // 2. Envoyer la requête
        ResponseEntity<OpenETResponse> response = restTemplate.getForEntity(url, OpenETResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // 3. Extraire les valeurs ET₀ de la réponse
            return response.getBody().getEt0Daily();
        } else {
            return new ArrayList<>(); // liste vide si erreur
        }
    }

    // 4. Classe interne pour mapper le JSON de la réponse OpenET
    public static class OpenETResponse {
        private List<Double> et0Daily;

        public List<Double> getEt0Daily() {
            return et0Daily;
        }

        public void setEt0Daily(List<Double> et0Daily) {
            this.et0Daily = et0Daily;
        }
    }
}
