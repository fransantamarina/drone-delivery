package com.musala.dronedelivery.mapper;

public interface GenericMapper<DTO, Entity> {

    DTO entityToDTO(Entity entity);

    Entity dtoToEntity(DTO dto);

}
