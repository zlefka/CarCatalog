package org.example.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.model.Car;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

public class CarFileReader {

    public List<Car> readFile(String path) throws IOException {
        List<Car> cars = new ArrayList<>();
        Path pathOfFile = Path.of(path);
        boolean isTxt = path.endsWith(".txt");
        boolean isCsv = path.endsWith(".csv");
        boolean isJson = path.endsWith(".json");
        if (isTxt) {
            System.out.println("Выбран формат TXT");
            cars = readTxt(pathOfFile);
            return cars;
        } else if (isCsv) {
            System.out.println("Выбран формат CSV");
            cars = readCsv(pathOfFile);
        } else if (isJson) {
            System.out.println("Выбран формат Json");
            cars = readJson(pathOfFile);
        } else {
            throw new IllegalArgumentException("Формат файла не поддерживается");
        }
        return cars;
    }

    private List<Car> readJson(Path path) throws IOException {
        List<Car> cars = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonNode root = mapper.readTree(reader);
            if (root == null || !root.isArray()) {
                throw new IllegalArgumentException("Массив автомобмлей не найден");
            }
            int counter = 0;
            for (JsonNode node : root) {
                JsonNode jModel = node.get("model");
                JsonNode jYear = node.get("year");
                JsonNode jPower = node.get("power");
                counter++;
                if (jPower == null || jModel == null || jYear == null || !jModel.isTextual()
                        || !jYear.isIntegralNumber() || !jPower.isIntegralNumber() || !jYear.canConvertToInt()
                        || !jPower.canConvertToInt()) {
                    throw new IllegalArgumentException("Неверный формат содержимого объекта номер " + counter +
                            ": Убедитесь, что модель в " +
                            "виде текста, а год и мощность целые числа в диапазоне int");
                }
                try {
                    String model = jModel.textValue();
                    int year = jYear.intValue();
                    int power = jPower.intValue();

                    Car car = new Car(model, year, power);
                    cars.add(car);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Неверный формат ввода в объекте номер " + counter
                            + ": " + e.getMessage());
                }
            }
        }
        return cars;
    }

    private List<Car> readCsv(Path path) throws IOException {
        List<Car> cars = new ArrayList<>();
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
        try (BufferedReader reader = Files.newBufferedReader(path);
             CSVParser parser = format.parse(reader)) {
            if (!parser.getHeaderMap().containsKey("model") || !parser.getHeaderMap().containsKey("power")
                    || !parser.getHeaderMap().containsKey("year")) {
                throw new IllegalArgumentException("Неверный формат заголовков таблицы");
            }
            for (CSVRecord line : parser) {
                if (!line.isConsistent()) {
                    throw new IllegalArgumentException("Неверный формат ввода в записи номер " + line.getRecordNumber());
                }
                try {
                    String model = line.get("model");
                    int power = Integer.parseInt(line.get("power"));
                    int year = Integer.parseInt(line.get("year"));

                    Car car = new Car(model, year, power);
                    cars.add(car);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Неверный формат ввода в записи " + line.getRecordNumber()
                            + ": " + e.getMessage());
                }
            }
        }
        return cars;
    }

    private List<Car> readTxt(Path path) throws IOException {
        List<Car> cars = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            int counter = 0;
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                counter++;
                if (line.isBlank()) {
                    continue;
                }
                String[] tokens = line.split(";", -1);
                if (tokens.length != 3) {
                    throw new IllegalArgumentException("Неверный формат ввода в строке " + counter);
                }
                String model = tokens[0];

                try {
                    int year = Integer.parseInt(tokens[2]);
                    int power = Integer.parseInt(tokens[1]);
                    Car car = new Car(model, year, power);
                    cars.add(car);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Неверный формат ввода в строке " + counter + ": "
                            + e.getMessage());
                }
            }
        }
        return cars;
    }
}
