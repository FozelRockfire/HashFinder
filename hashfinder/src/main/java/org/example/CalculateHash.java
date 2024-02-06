package org.example;

import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class CalculateHash {
    private final String algorithm;
    private final String hash;
    private final Integer length;
    private final Integer numberOfThreads;
    private final List<String> genStrings = new ArrayList<>();

    public void calculateHashParallel() {
        StringBuilder stringBuilder = new StringBuilder();
        generateAllStrings(stringBuilder, length, genStrings);

        List<String> results = new ArrayList<>();

        try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {
            var futures = genStrings.stream()
                    .map(genString -> executor.submit(() -> checkHash(genString)))
                    .toList();
            for (var fut : futures) {
                String checkHashResult = fut.get();
                if (checkHashResult != null){
                    results.add(fut.get());
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!results.isEmpty()){
            for (String result:results){
                System.out.println("Found matching string: " + result);
            }
        } else {
            System.out.println("No matching string found, try to use another -l argument value");
        }
    }

    public String checkHash(String genString) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String generatedHash = calculateHash(md, genString);

            if (Objects.equals(generatedHash, hash)) {
                return genString;
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String calculateHash(MessageDigest md, String input) {
        byte[] bytes = md.digest(input.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static void generateAllStrings(StringBuilder sb, int length, List<String> source) {
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