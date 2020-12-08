package ch.fhnw.linusvettiger.bloomfilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static int[] computeDimensions(float p, int n) {
        // Calculations from https://en.wikipedia.org/wiki/Bloom_filter#Examples
        var filterSize = -(n * Math.log(p)) / Math.pow(Math.log(2), 2);
        var numHashes = Math.round((filterSize / n) * Math.log(2));
        return new int[] { (int)filterSize, (int)numHashes };
    }

    public static void main(String[] args) throws IOException {
        // Error Probability is taken in as the first argument
        float errorProbability = Float.parseFloat(args[0]);
        List<String> input = Files.readAllLines(Paths.get("words.txt"));

        int[] computedDimensions = computeDimensions(errorProbability, input.size());
        int filterSize = computedDimensions[0];
        int numHashes = computedDimensions[1];

        IBloomFilter filter = new BloomFilter(filterSize, numHashes);
        // Train the bloom filter with the provided list of words
        input.forEach(filter::train);

        // Experimentally calculate the error probability
        long falsePositive = 0, trueNegative = 0;
        // Modify the input by prefixing each value with "$$$" so they are 100% not in the collection.
        var modifiedInput = input.stream().map(str -> "$$$"+str).collect(Collectors.toList());
        // Run all those modified inputs through the filter and check if it falsely returns true
        for (String s : modifiedInput) {
            if (filter.contains(s)) {
                falsePositive ++;
            } else {
                trueNegative ++;
            }
        }

        System.out.println(filter);
        System.out.printf("Expected error probability: %f\n", errorProbability);
        System.out.printf("Experimentally calculated error probability: %.10f(%d/%d)\n", (double)falsePositive/trueNegative, falsePositive, trueNegative);
    }
}
