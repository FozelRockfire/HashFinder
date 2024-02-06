package org.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandBuilder {

    String[] args;

    public void buildCommand() {
        if (args.length < 6) {
            System.out.println("Usage: myapp -a <algorithm> -h <hash> -l <length>");
            return;
        }

        String algorithm = null;
        String hash = null;
        int length = 0;

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

        CalculateHash calculateHash = new CalculateHash(algorithm, hash, length);
        calculateHash.calculateHashParallel();
    }

}
