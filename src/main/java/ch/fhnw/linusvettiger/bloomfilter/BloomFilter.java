package ch.fhnw.linusvettiger.bloomfilter;

public class BloomFilter implements IBloomFilter {
    private int filterSize;

    public BloomFilter (int filterSize) {
        this.filterSize = filterSize;

    }

    public void train (String data) {

        // TODO train filter
    }

    @Override
    public String toString() {
        return filterSize + "";
    }
}
