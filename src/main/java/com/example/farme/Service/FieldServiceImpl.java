package com.example.farme.Service;

import com.example.farme.Repository.FieldRepository;
import com.example.farme.model.Field;
import com.example.farme.model.IrrigationDayPlan;
import com.example.farme.model.IrrigationScheduleResponse;
import com.example.farme.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class FieldServiceImpl  implements FieldService{

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private OpenETClient openETClient;

    @Autowired
    private OpenMeteoClient openMeteoClient;

    @Override
    public Field saveField(Field field){ return fieldRepository.save(field);}

    @Override
    public Field getFieldById(Long id){
        return fieldRepository.findById(id).orElse(null);
    }

    @Override
    public Field deleteField(Long id) {
        Field field = fieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Field not found"));
        fieldRepository.delete(field);
        return field;
    }


    @Override
    public Field updateField(Field field) {
        return null;
    }

//    @Override
//    public Field updateField(Field field) {
//
//
//        //Field existingField = fieldRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Field not found"))
//      //  existingField.set
//        //existingField.set
//      //  existingField.set
//      //  existingField.set
//      //  return fieldRepository.save(existingField);
//    //}

    @Override
    public List<Field> getAllFields(){
        return fieldRepository.findAll();}

    @Override
    public IrrigationScheduleResponse generateIrrigationSchedule(Long fieldId) {
        //  la logique OpenET + calculs
        //récupérer le champ
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new IllegalArgumentException("Champ non trouvé"));
        //calcul de la surface
        double areaInSquareMeters = GeoUtils.calculateArea(field.getCoordinates());

        System.out.println("Champ récupéré : " + field.getId());
        System.out.println("Surface du champ (m²) : " + areaInSquareMeters);

        //coefficient Kc
        double kc = CropCoefficientUtils.getKc(field.getCropType(), field.getGrowthStage());

        System.out.println("CropType : " + field.getCropType());
        System.out.println("GrowthStage : " + field.getGrowthStage());
        System.out.println("Kc utilisé : " + kc);

        // OpenET

        List<Double> etData = openETClient.getDailyETData(field.getCoordinates());
        //List<Double> etData = Arrays.asList(5.1, 5.3, 5.0, 4.8, 5.2, 5.0, 5.4);  // fake ET0

        System.out.println("Valeurs ET₀ récupérées depuis OpenET :");
        for (int i = 0; i < etData.size(); i++) {
            System.out.println("Jour " + (i + 1) + " → ET₀ = " + etData.get(i) + " mm");
        }

        //Open-Meteo
        List<Double> precipitationData = openMeteoClient.getDailyPrecipitationData(field.getCoordinates());
        //List<Double> precipitationData = Arrays.asList(1.0, 0.5, 0.0, 0.0, 1.2, 2.0, 0.7);  // fake rain

        System.out.println("Précipitations récupérées depuis Open-Meteo :");
        for (int i = 0; i < precipitationData.size(); i++) {
            System.out.println("Jour " + (i + 1) + " → Pluie = " + precipitationData.get(i) + " mm");
        }

        List<IrrigationDayPlan> schedule = IrrigationUtils.generateSchedule(
                areaInSquareMeters, kc, field.getFlowRate(), etData, precipitationData
        );

        IrrigationScheduleResponse response = new IrrigationScheduleResponse();
        response.setFieldId(fieldId);
        response.setFieldName(field.getName());
        response.setSchedule(schedule);

        return response;
    }
}
