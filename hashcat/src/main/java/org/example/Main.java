package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

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

        // Генерация строк и проверка контрольной суммы
        StringBuilder sb = new StringBuilder(length);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        CheckHashRecursive(md, hash, length, sb);
    }

    private static void CheckHashRecursive(MessageDigest md, String hash, int length, StringBuilder sb) {
        if (sb.length() == length) {
            String generatedHash = calculateHash(md, sb.toString());
            if (Objects.equals(generatedHash, hash)) {
                System.out.println("Found matching string: " + sb);
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                sb.append(i);
                CheckHashRecursive(md, hash, length, sb);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    private static String calculateHash(MessageDigest md, String input) {
        byte[] bytes = md.digest(input.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}