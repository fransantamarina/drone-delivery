package com.musala.dronedelivery.mapper;

import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.generic.Load;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MedicationLoadMapper extends GenericMapper<LoadCreationDTO, Load> {

    MedicationLoadMapper MEDICATION_LOAD_MAPPER = Mappers.getMapper(MedicationLoadMapper.class);


    default MedicationLoadDTO toLoadCheckDTO(Load medicationLoad) {
        return MedicationLoadDTO.builder()
                .carrier(medicationLoad.getDrone().getSerialNumber())
                .medicationCodes(medicationLoad.getItemCodes())
                .build();
    }


}



