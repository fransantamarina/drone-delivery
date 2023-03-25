package com.musala.dronedelivery.service.impl;

import com.musala.dronedelivery.dto.drone.DroneCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.LoadCreationDTO;
import com.musala.dronedelivery.dto.medicationLoad.MedicationLoadDTO;
import com.musala.dronedelivery.entity.Drone;
import com.musala.dronedelivery.generic.Load;
import com.musala.dronedelivery.enums.DroneModel;
import com.musala.dronedelivery.enums.DroneState;
import com.musala.dronedelivery.exception.AppException;
import com.musala.dronedelivery.mapper.DroneMapper;
import com.musala.dronedelivery.repository.DroneRepository;
import com.musala.dronedelivery.service.DroneService;
import com.musala.dronedelivery.service.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.musala.dronedelivery.util.DroneConstants.DEFAULT_WEIGHT_LIMIT;
import static com.musala.dronedelivery.util.DroneConstants.MINIMUM_BATTERY_CAPACITY_FOR_LOAD;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {

    @Spy
    DroneMapper droneMapper;
    @Mock
    private DroneRepository droneRepository;

    @Mock
    private LoadService medicationLoadService;

    private DroneService droneService;

    @BeforeEach
    void setUp() {
        openMocks(this);
        droneService = new DroneServiceImpl(droneRepository, medicationLoadService);
        droneMapper = Mappers.getMapper(DroneMapper.class);
    }


    @Test
    void testRegister() throws AppException {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setModel(DroneModel.CRUISERWEIGHT);
        drone.setSerialNumber("123456789");
        drone.setWeightLimit(DEFAULT_WEIGHT_LIMIT);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100);

        DroneCreationDTO droneCreationDTO = new DroneCreationDTO();
        droneCreationDTO.setModel(DroneModel.CRUISERWEIGHT.name().toUpperCase());
        droneCreationDTO.setWeightLimit(DEFAULT_WEIGHT_LIMIT);
        droneCreationDTO.setSerialNumber("123456789");

        when(droneRepository.existsBySerialNumber(droneCreationDTO.getSerialNumber())).thenReturn(false);

        when(droneRepository.save(any(Drone.class))).thenReturn(drone);

        DroneCreationDTO response = droneService.register(droneCreationDTO);

        verify(droneRepository, times(1)).existsBySerialNumber(droneCreationDTO.getSerialNumber());

        assertEquals(droneCreationDTO, response);
    }

    @Test
    void testFindAll() {
        Drone drone1 = new Drone();
        drone1.setId(1L);
        drone1.setModel(DroneModel.CRUISERWEIGHT);
        drone1.setSerialNumber("12345");
        drone1.setWeightLimit(DEFAULT_WEIGHT_LIMIT);
        drone1.setState(DroneState.IDLE);
        drone1.setBatteryCapacity(100);

        Drone drone2 = new Drone();
        drone2.setId(2L);
        drone2.setModel(DroneModel.LIGHTWEIGHT);
        drone2.setSerialNumber("6789");
        drone2.setWeightLimit(DEFAULT_WEIGHT_LIMIT);
        drone2.setState(DroneState.IDLE);
        drone2.setBatteryCapacity(50);

        when(droneRepository.findAll()).thenReturn(List.of(drone1, drone2));

        List<DroneCreationDTO> response = droneService.findAll();

        verify(droneRepository, times(1)).findAll();

        List<DroneCreationDTO> expected = List.of(droneMapper.entityToDTO(drone1), droneMapper.entityToDTO(drone2));
        assertEquals(expected, response);
    }

    @Test
    void testLoad() throws AppException {
        String serialNumber = "12345";
        LoadCreationDTO medicationLoadDTO = new LoadCreationDTO();
        medicationLoadDTO.setItemCodes(Set.of("12341B", "12341BA"));
        // Set up any necessary fields for medicationLoadDTO

        Drone drone = new Drone();
        drone.setSerialNumber(serialNumber);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100);

        when(droneRepository.findBySerialNumberAndStateAndBatteryCapacityGreaterThanEqual(drone.getSerialNumber(), DroneState.IDLE, MINIMUM_BATTERY_CAPACITY_FOR_LOAD)).thenReturn(Optional.of(drone));
        when(droneRepository.existsBySerialNumber(drone.getSerialNumber())).thenReturn(true);

        Load inputLoad = mock(Load.class);
        when(inputLoad.getItemCodes()).thenReturn(List.of("12341B", "12341BA"));

        doAnswer(invocation -> {
            drone.setState(DroneState.LOADED);
            return MedicationLoadDTO.builder()
                    .medicationCodes(List.of("12341B", "12341BA"))
                    .build();
        }).when(medicationLoadService).save(drone, medicationLoadDTO);


        MedicationLoadDTO result = droneService.load(serialNumber, medicationLoadDTO);

        assertArrayEquals(inputLoad.getItemCodes().toArray(), result.getMedicationCodes().toArray());

        assertEquals(DroneState.LOADED, drone.getState());
    }




}