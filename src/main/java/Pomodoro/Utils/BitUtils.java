package Pomodoro.Utils;

public class BitUtils {
    /**
     * @param bits bits
     * @param pattern bits you want included
     * @return <code>True</code> if all of the pattern bits are present
     * */
    public static boolean contains(int bits, int pattern){
        return (bits & pattern) == pattern;
    }
    /**
     * @param bits bits
     * @param pattern bits you don't want included
     * @return <code>True</code> if none of the pattern bits are present
     * */
    public static boolean notContains(int bits, int pattern){
        return !((bits & pattern) > 0);
    }

    /**
     * Can be used for bounding/ restricting possible bit values,
     * pattern subtraction or filtering unique values.
     * <pre>
     *     e.g.
     *      bits            = 01010001
     *      possible_bits   = 00011111
     *      result          = 01000000 which is > 0
     * </pre>
     * @param bits bits
     * @param pattern allowed bits
     * @return bits are 1 on the left had side
     *      0 0 false
     *      0 1 false
     *      1 0 true
     *      1 1 false
     * */
    public static int lsXOR(int bits, int pattern){
        return (bits ^ (bits & pattern));
    }
}
