package com.example.farme.Service;

import com.example.farme.Repository.FieldRepository;
import com.example.farme.model.Field;
import com.example.farme.model.IrrigationScheduleResponse;
import com.example.farme.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //coefficient Kc
        double kc = CropCoefficientUtils.getKc(field.getCropType(), field.getGrowthStage());
        // OpenET



        List<Double> etData = openETClient.getDailyETData(field.getCoordinates());
        //Open-Meteo
        List<Double> precipitationData = openMeteoClient.getDailyPrecipitationData(field.getCoordinates());

        return null;
    }
}
