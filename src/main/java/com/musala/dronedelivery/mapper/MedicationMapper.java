package com.musala.dronedelivery.mapper;

import com.musala.dronedelivery.dto.medication.MedicationCreationDTO;
import com.musala.dronedelivery.dto.medication.MedicationDTO;
import com.musala.dronedelivery.entity.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MedicationMapper extends GenericMapper<MedicationDTO, Medication> {

    MedicationMapper MEDICATION_MAPPER = Mappers.getMapper(MedicationMapper.class);

    @Mapping(target = "weight", source = "weight")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Medication medicationCreationDtoToEntity(MedicationCreationDTO dto);

}
