package org.example.console;

import org.example.file.CarFileReader;
import org.example.model.Car;
import org.example.service.CarService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private final Scanner scanner = new Scanner(System.in);
    private final CarService carService = new CarService();

    public void printMenu() {
        System.out.println("""
                ===== КАТАЛОГ АВТОМОБИЛЕЙ =====
                1. Добавить автомобиль вручную
                2. Загрузить автомобили из файла
                3. Показать все автомобили
                4. Обработать автомобили через Stream API
                5. Отсортировать собственным алгоритмом
                0. Выход
                """);
    }

    public void run() {
        while (true) {
            printMenu();
            System.out.println("Выберите пункт меню: ");

            String input = scanner.nextLine();
            int command;

            try {
                command = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
                continue;
            }

            switch (command) {
                case 1 -> {
                    System.out.println("Добавление данных вручную");
                    addCarManually();
                }
                case 2 -> {
                    System.out.println("Введите путь к файлу");
                    CarFileReader carFileReader = new CarFileReader();
                    try {
                        List<Car> cars = carFileReader.readFile(scanner.nextLine());
                        System.out.println("Загрузка данных из файла");
                        carService.addCars(cars);
                        System.out.println("Загружено " + cars.size() + " автомобилей");
                    } catch (IOException e) {
                        System.out.println("Не удалось прочитать файл");
                        System.out.println(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (UncheckedIOException e) {
                        System.out.println("Не удалось прочитать файл: неверный формат.");
                        System.out.println(e.getCause().getMessage());
                    }
                }
                case 3 -> {
                    List<Car> allCars = carService.getAllCars();
                    if(allCars.isEmpty()) {
                        System.out.println("Вы еще не добавили автомобили в коллекцию");
                    } else {
                        System.out.println("Ваши автомобили:");
                        allCars.forEach(System.out::println);
                    }
                }
                case 4 -> {
                    System.out.println("Обработка автомобилей");
                    // TODO
                }
                case 5 -> {
                    System.out.println("Сортировка автомобилей");
                    // TODO
                }
                case 0 -> {
                    System.out.println("Завершение программы...");
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void addCarManually() {
        System.out.println("Введите марку и модель автомобиля: ");
        String model = scanner.nextLine();
        int year;
        int power;
        while (true) {

            System.out.println("Введите год выпуска:");
            String yearStr = scanner.nextLine();

            try {
                year = Integer.parseInt(yearStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
            }
        }
        while (true) {

            System.out.println("Введите мощность в л.с.:");
            String powerStr = scanner.nextLine();

            try {
                power = Integer.parseInt(powerStr);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Необходимо ввести число");
            }
        }

        try {
            Car car = new Car(model, year, power);
            carService.addCar(car);
            System.out.println("Добавлен автомобиль " + car.getModel() + " " +car.getYear() +
                    " года выпуска с мощностью " + car.getPower() + " л.с.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
