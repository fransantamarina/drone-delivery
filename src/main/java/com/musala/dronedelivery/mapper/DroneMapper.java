package com.musala.dronedelivery.mapper;

import com.musala.dronedelivery.dto.drone.DroneBatteryDTO;
import com.musala.dronedelivery.dto.drone.DroneCreationDTO;
import com.musala.dronedelivery.entity.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DroneMapper extends GenericMapper<DroneCreationDTO, Drone> {

    DroneMapper DRONE_MAPPER = Mappers.getMapper(DroneMapper.class);

    @Mapping(source = "serialNumber", target = "serialNumber")
    @Mapping(source = "weightLimit", target = "weightLimit")
    @Mapping(source = "model", target = "model")
    Drone dtoToEntity(DroneCreationDTO dto);

    @Mapping(source = "serialNumber", target = "serialNumber")
    @Mapping(source = "weightLimit", target = "weightLimit")
    DroneCreationDTO entityToDTO(Drone drone);

    DroneBatteryDTO droneToBatteryDTO(Drone drone);

}