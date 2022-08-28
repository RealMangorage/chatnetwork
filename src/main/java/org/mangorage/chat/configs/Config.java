package org.mangorage.chat.configs;

import java.io.*;
import java.util.Properties;

public class Config {

    private final File file;
    private final Properties properties;

    public Config(String fileName) {
        this.file = new File(fileName);
        this.properties = new Properties();

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                properties.load(new FileInputStream(fileName));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void saveProperties() {
        try {
            properties.store(new FileWriter(file.getAbsolutePath()), "store to properties file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
