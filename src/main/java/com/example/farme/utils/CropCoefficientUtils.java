package com.example.farme.utils;

import java.util.HashMap;
import java.util.Map;
// KC
public class CropCoefficientUtils {

    private static final Map<String, Map<String, Double>> kcMap = new HashMap<>();

    static {
        // 🌾 Wheat
        Map<String, Double> wheatStages = new HashMap<>();
        wheatStages.put("Seedling", 0.3);
        wheatStages.put("Adult", 1.15);
        wheatStages.put("Elderly", 0.25);
        kcMap.put("wheat", wheatStages);

        // 🍅 Tomato
        Map<String, Double> tomatoStages = new HashMap<>();
        tomatoStages.put("Seedling", 0.4);
        tomatoStages.put("Adult", 1.05);
        tomatoStages.put("Elderly", 0.65);
        kcMap.put("tomato", tomatoStages);

        // 🥬 Lettuce
        Map<String, Double> lettuceStages = new HashMap<>();
        lettuceStages.put("Seedling", 0.35);
        lettuceStages.put("Adult", 1.0);
        lettuceStages.put("Elderly", 0.6);
        kcMap.put("lettuce", lettuceStages);

        // Tu peux ajouter d'autres cultures ici...

        Map<String, Double> cornStages = new HashMap<>();
        cornStages.put("Seedling", 0.4);
        cornStages.put("Adult", 1.2);
        cornStages.put("Elderly", 0.35);
        kcMap.put("corn", cornStages);

        Map<String, Double> potatoStages = new HashMap<>();
        potatoStages.put("Seedling", 0.5);
        potatoStages.put("Adult", 1.10);
        potatoStages.put("Elderly", 0.65);
        kcMap.put("potato", potatoStages);

        Map<String, Double> peanutStages = new HashMap<>();
        peanutStages.put("Seedling", 0.4);
        peanutStages.put("Adult", 1.10);
        peanutStages.put("Elderly", 0.5);
        kcMap.put("peanut", peanutStages);

        Map<String, Double> beanStages = new HashMap<>();
        beanStages.put("Seedling", 0.4);
        beanStages.put("Adult", 1.05);
        beanStages.put("Elderly", 0.9);
        kcMap.put("bean", beanStages);

        Map<String, Double> sunflowerStages = new HashMap<>();
        sunflowerStages.put("Seedling", 0.3);
        sunflowerStages.put("Adult", 1.15);
        sunflowerStages.put("Elderly", 0.5);
        kcMap.put("sunflower", sunflowerStages);

        Map<String, Double> sorghumStages = new HashMap<>();
        sorghumStages.put("Seedling", 0.4);
        sorghumStages.put("Adult", 1.15);
        sorghumStages.put("Elderly", 0.6);
        kcMap.put("sorghum", sorghumStages);

        Map<String, Double> riceStages = new HashMap<>();
        riceStages.put("Seedling", 1.05);
        riceStages.put("Adult", 1.20);
        riceStages.put("Elderly", 0.9);
        kcMap.put("rice", riceStages);
    }

    public static double getKc(String cropType, String growthStage) {
        if (kcMap.containsKey(cropType.toLowerCase())) {
            Map<String, Double> stageMap = kcMap.get(cropType.toLowerCase());
            return stageMap.getOrDefault(growthStage, 1.0); // default kc si stage non trouvé
        }
        return 1.0; // valeur par défaut si culture inconnue
    }
}
