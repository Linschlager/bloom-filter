package ch.fhnw.linusvettiger.bloomfilter;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * Minimal bloom filter implementation as a data structure
 */
public class BloomFilter implements IBloomFilter {
    // BitSet that acts as the filter
    private BitSet filter;
    // Array of hash functions to be used
    private HashFunction[] hashFunctions;

    private int filterSize;
    private int numHashes;

    public BloomFilter (int filterSize, int numHashes) {
        var rnd = new Random();
        this.filterSize = filterSize;
        this.numHashes = numHashes;

        this.filter = new BitSet(filterSize);
        this.hashFunctions = new HashFunction[numHashes];

        // Generate a given number of hash functions as randomly seeded murmur3_128
        // Possible improvement; Check that the same seed hasn't been used twice - this would significantly weaken the filter
        for (int i = 0; i < numHashes; i++) {
            int seed = rnd.nextInt();
            this.hashFunctions[i] = Hashing.murmur3_128(seed);
        }
    }

    /**
     * Small helper function that passes the given data through all hash functions and returns an array of ints as a result.
     * The returned ints are bounded by the filterSize
     * @param data String input to be hashed
     * @return Hashed data as int bounded by filterSize
     */
    private int[] computeHashes(String data) {
        return Arrays
            .stream(hashFunctions)
            .map(h -> (h.hashString(data, Charset.defaultCharset()).asInt() % filterSize + filterSize) % filterSize)// Mod f_length so the returned int is directly the index in the bit-array
            .mapToInt(i -> i)
            .toArray();
    }

    /**
     * Add a String to the learned data-set
     * @param data Input data to put in the filter
     */
    @Override
    public void train (String data) {
        int[] hashes = computeHashes(data);
        Arrays.stream(hashes).forEach(this.filter::set);
    }

    /**
     * Queries the bloom filter and returns one of the following results:
     * - true  -> The input may or may not be contained in the filter. The probability for false positives is calculated beforehand
     * - false -> The input is definitely not in the filter
     * @param data Input to query the bloom filter with
     * @return data may be in the filter or definitely isn't
     */
    @Override
    public boolean contains (String data) {
        int[] hashes = computeHashes(data);
        return Arrays.stream(hashes).allMatch(this.filter::get);
    }

    @Override
    public String toString() {
        return "This bloom filter is " + filterSize + "bit long and uses " + numHashes + " hash functions";
    }
}
