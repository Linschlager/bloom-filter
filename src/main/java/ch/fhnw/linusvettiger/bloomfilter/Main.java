package ch.fhnw.linusvettiger.bloomfilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // Error Probability is taken in as the first argument
        int errorProbability = Integer.parseInt(args[0]);
        List<String> input = Files.readAllLines(Paths.get("words.txt"));



    }
}
