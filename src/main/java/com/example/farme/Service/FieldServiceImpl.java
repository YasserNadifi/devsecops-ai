package com.example.farme.Service;

import com.example.farme.Repository.FieldRepository;
import com.example.farme.model.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class FieldServiceImpl  implements FieldService{

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public Field saveField(Field field){ return fieldRepository.save(field);}

    @Override
    public Field getFieldById(Long id){
        return fieldRepository.findById(id).orElse(null);
    }

    @Override
    public Field deleteField(Long id){
        Field field = fieldRepository.findById(id).orElse(null);
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

}
