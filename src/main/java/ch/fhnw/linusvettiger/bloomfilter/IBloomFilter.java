package ch.fhnw.linusvettiger.bloomfilter;

public interface IBloomFilter {
    void train (String data);
    boolean contains(String data);
}
