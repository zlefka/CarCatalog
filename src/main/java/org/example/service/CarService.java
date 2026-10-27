package org.example.service;

import org.example.model.Car;

import java.util.ArrayList;
import java.util.List;

public class CarService {
    private final List<Car> cars = new ArrayList<>();

    public void addCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Автомобиль не может быть null");
        }
        cars.add(car);
    }

    public void addCars(List<Car> newCars) {
        if (newCars == null) {
            throw new IllegalArgumentException("Список автомобилей не может быть null");
        }

        if (newCars.contains(null)) {
            throw new IllegalArgumentException("Список не должен содержать null");
        }

        cars.addAll(newCars);
    }

    public List<Car> getAllCars() {
        return List.copyOf(cars);
    }


}
