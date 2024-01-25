package org.example;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Queue;


public class CalculateHashParallel implements Runnable {
    private final String algorithm;
    private final String hash;
    private final int length;
    private final Queue<String> source;

    public CalculateHashParallel(String algorithm, String hash, int length, Queue<String> source) {
        this.algorithm = algorithm;
        this.hash = hash;
        this.length = length;
        this.source = source;
    }

    @Override
    public void run() {
        while (!source.isEmpty()) {
            checkHashes(source.poll());
        }
    }

    private void checkHashes(String genString) {
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
}