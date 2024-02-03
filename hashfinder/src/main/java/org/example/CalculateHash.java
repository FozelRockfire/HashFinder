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
    private final List<String> genStringList = new ArrayList<>();

    public void calculateHashParallel() {
        StringBuilder stringBuilder = new StringBuilder();
        generateAllStrings(stringBuilder, length, genStringList);

        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            var futures = genStringList.stream()
                    .map(it -> executor.submit(() -> checkHash(it)))
                    .toList();

            for (var fut : futures) {
                fut.get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkHash(String genString) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            String generatedHash = calculateHash(md, genString);

            if (Objects.equals(generatedHash, hash)) {
                System.out.println("Found matching string: " + genString);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
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