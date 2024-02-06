package org.example;

public class Main {

    public static void main(String[] args) {

        for (int i = 1; i<=Runtime.getRuntime().availableProcessors(); i++) {
            long startTime = System.currentTimeMillis();

            CommandBuilder commandBuilder = new CommandBuilder(args, i);
            commandBuilder.buildCommand();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println(i + " Threads, Execution time: " + executionTime + " ms");
        }
    }
}