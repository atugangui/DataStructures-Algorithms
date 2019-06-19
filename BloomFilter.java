public class BloomFilter<T> {
    private static int mask = (1 << 16) - 1; // accessible everywhere
    private long[] filter; // tracks presence of domains

    public static void main(String[] args) {
    }

    BloomFilter() {
        filter = new long[1024];
    }

    public void add(String domain) {
        int h = domain.hashCode(); // creates hash code for domain we want to add to filter
        int low = h & mask; // creates low-order bits from hash code
        int high = (h >> 16) & mask; // creates high-order bits from hash code
        int longIndexLo = low / 64; // calculates Long index using low-order bits
        int bitIndexLo = low % 64; // calculates bit index using low-order bits
        int longIndexHi = high / 64; // calculates Long index using high-order bits
        int bitIndexHi = high % 64; // calculates bit index using high-order bits
        filter[longIndexLo] = filter[longIndexLo] | 1L << bitIndexLo; // changes correct index to 1 for low-order bits
        filter[longIndexHi] = filter[longIndexHi] | 1L << bitIndexHi; // changes correct index to 1 for high-order bits
    }

    public boolean mightContain(T s) {
        int h = s.hashCode(); // creates hash code for domain of interest
        int low = h & mask; // creates low-order bits from hash code
        int longIndexLo = low / 64; // calculates Long index using low-order bits
        int bitIndexLo = low % 64; // calculates bit index using low-order bits
        if ((filter[longIndexLo] & (1L << bitIndexLo)) != 0L) { // checks low-order bit index for flag
            int high = (h >> 16) & mask; // if flagged, calculates high-order bits from hash code
            int longIndexHi = high / 64; // calculates Long index using high-order bits
            int bitIndexHi = high % 64; // calculates bit index using high-order bits
            if ((filter[longIndexHi] & (1L << bitIndexHi)) != 0L) { // checks high-order bit index for flag
                return true; // true if both indices contain 1
            }
        }
        return false; // false if 1 index is zero, or both are zero
    }

    public int trueBits() {
        int numBits = 0;
        for (int i = 0; i < filter.length; i++) { // searches through all indices in filter
            numBits += Long.bitCount(filter[i]); // adds to counter when an index contains 1
        }
        return numBits; // returns total bit value
    }
}