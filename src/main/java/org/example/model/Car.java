package org.example.model;

import java.time.Year;

public class Car {
    private final String model;
    private final int power;
    private final int year;


    public Car(String model, Integer year, Integer power) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Модель не должна быть пустой");
        }

        if (power <= 0 || power > 2300) {
            throw new IllegalArgumentException("Мощность должна быть от 0 до 2300 л.с.");
        }

        if (year < 1886 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Некорректный год выпуска");
        }

        this.model = model.trim();
        this.year = year;
        this.power = power;
    }

    @Override
    public String toString() {
        return "--------------------\n" +
                "Модель: " + model + "\n" +
                "Год выпуска: " + year + "\n" +
                "Мощность: " + power + "\n" +
                "--------------------";
    }

    public String getModel() {
        return model;
    }

    public Integer getPower() {
        return power;
    }

    public Integer getYear() {
        return year;
    }
}
