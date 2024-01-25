package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args) {
        if (args.length < 6) {
            System.out.println("Usage: myapp -a <algorithm> -h <hash> -l <length>");
            return;
        }

        String algorithm = null;
        String hash = null;
        int length = 0;

        // Парсинг аргументов командной строки
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-a":
                    algorithm = args[i + 1];
                    break;
                case "-h":
                    hash = args[i + 1];
                    break;
                case "-l":
                    length = Integer.parseInt(args[i + 1]);
                    break;
            }
        }

        if (algorithm == null || hash == null || length == 0) {
            System.out.println("Invalid arguments");
            return;
        }


        long startTime = System.currentTimeMillis();

        // Создание и запуск потоков для перебора строк
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];

        ConcurrentLinkedQueue<String> source = new ConcurrentLinkedQueue<>();
        StringBuilder sb = new StringBuilder();

        generateAllStrings(sb, length, source);
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new CalculateHashParallel(algorithm, hash, length, source));
            threads[i].start();
        }

        for (var th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    private static void generateAllStrings(StringBuilder sb, int length, ConcurrentLinkedQueue<String> source) {
        if (sb.length() == length) {
            source.add(sb.toString());
        } else {
            for (int i = 0; i <= 9; i++) {
                sb.append(i);
                generateAllStrings(sb, length, source);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }
}