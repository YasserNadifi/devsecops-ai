package com.example.farme.utils;

import com.example.farme.model.IrrigationDayPlan;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class IrrigationUtils {

    public static List<IrrigationDayPlan> generateSchedule(
            double areaInSquareMeters, double kc, double flowRate,
            List<Double> etData, List<Double> precipitationData
    ) {
        if (etData == null || etData.isEmpty()) {
            System.err.println("❌ Données ET₀ manquantes → planification impossible.");
            return Collections.emptyList();
        }

        List<IrrigationDayPlan> schedule = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            double et0 = etData.get(i);
            double precipitation = precipitationData.get(i);

            // Besoin net en mm : ETc = ET0 * Kc - précipitations
            double etc = Math.max(0, (et0 * kc) - precipitation);

            // Volume (litres) = ETc (mm) × Surface (m²)
            double volumeLiters = etc * areaInSquareMeters;

            // Gallons = litres / 3.785
            double volumeGallons = volumeLiters / 3.785;

            // Durée (heures) = volume (litres) / débit (L/h)
            double durationHours = flowRate > 0 ? volumeLiters / flowRate : 0;

            // Heure de fin : 6h du matin + durée
            LocalTime endTime = LocalTime.of(6, 0).plusMinutes((long) (durationHours * 60));

            // Créer le plan journalier
            IrrigationDayPlan dayPlan = new IrrigationDayPlan();
            dayPlan.setDate(LocalDate.now().plusDays(i));
            dayPlan.setWaterAmountGallons(Math.round(volumeGallons * 100.0) / 100.0); // 2 chiffres
            dayPlan.setEndTime(endTime);

            schedule.add(dayPlan);
        }

        return schedule;
    }
}
