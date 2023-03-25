package com.musala.dronedelivery.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Component
public class FileHandler {

    public void createFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                log.info("Created battery check log");
                System.out.println("File created: " + file.getName());
            }
        } catch (IOException e) {
            log.error("Error while trying to create file: {}", fileName);
        }
    }

    public void writeToFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            log.error("Error while trying to write to file: {} - Content: ", fileName, content);

        }
    }
}
