package ru.sergjavacode;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0, 1000)
                .forEach(x -> {
                    Runnable runnable = () -> {
                        String s = generateRoute("RLRFR", 100);
                        int numberOfRepetitionsR = (int) s.chars().filter(cr -> cr == 'R').count();
                        addCountRepetitionsR(numberOfRepetitionsR);
                    };
                    executorService.execute(runnable);
                });

        executorService.shutdown();
        int xx = sizeToFreq.values().stream().mapToInt(x -> x).sum();
        System.out.println("Проверка. Сумма значений в мапе (должна быть равна 1000): " + xx);
        String maxKey = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey()
                .toString();
        String maxValue = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getValue()
                .toString();

        System.out.printf("\nСамое частое количество повторений %s (встретилось %s раз)\n", maxKey, maxValue);
        System.out.println("Другие размеры:");
        sizeToFreq.entrySet()
                .stream()
                .filter(f -> {
                    return !f.getKey().toString().equals(maxKey.toString());
                })
                .sorted(Map.Entry.comparingByValue())
                .forEach(f -> {
                    System.out.printf("- %d (%d раз)\n", f.getKey(), f.getValue());
                });
    }

    public static synchronized void addCountRepetitionsR(int numberOfRepetitionsR) {
        int value;
        if (sizeToFreq.containsKey(numberOfRepetitionsR)) {
            value = sizeToFreq.get(numberOfRepetitionsR);
            value += 1;
            sizeToFreq.put(numberOfRepetitionsR, value);
        } else {
            sizeToFreq.put(numberOfRepetitionsR, 1);
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
