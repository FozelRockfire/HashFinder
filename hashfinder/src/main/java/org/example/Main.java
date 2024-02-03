package org.example;

public class Main {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        CommandBuilder commandBuilder = new CommandBuilder(args);
        commandBuilder.buildCommand();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " ms");
    }
}