package com.musala.dronedelivery.cron;

import com.musala.dronedelivery.repository.DroneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Slf4j
@Component
public class BatteryChecker {

    private static final Logger BATTERY_LOGGER = Logger.getLogger(BatteryChecker.class.getName());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final DroneRepository droneRepository;

    @Autowired
    public BatteryChecker(DroneRepository droneRepository, @Value("${drone.battery-check.file.name}") String fileName) {
        this.droneRepository = droneRepository;
        try {
            var fileHandler = new FileHandler(fileName);
            fileHandler.setFormatter(new SimpleFormatter());
            BATTERY_LOGGER.setUseParentHandlers(false);
            BATTERY_LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            log.error("Error while trying to create file: {}", fileName);
        }
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void batteryCheck() {
        var drones = droneRepository.findAll();
        if (!drones.isEmpty()) {
            var timestamp = LocalDateTime.now();
            var content = new StringJoiner("\n");
            drones.forEach(drone -> content.add(drone.getSerialNumber() + ", " + drone.getBatteryCapacity()));
            BATTERY_LOGGER.info(timestamp.format(DATE_TIME_FORMATTER) + "\n" + content.toString());
        }
    }

}

