import java.util.Arrays;

public class KeySchedule {

    public static final int SIZE   = 95;
    public static final int OFFSET = 32;

    public static final int[] SBOX = {
            66, 24, 93, 90, 19, 25,  2, 26, 85, 13, 88, 37,  5,  6, 82, 59,
             3, 45, 31, 53, 17, 23,  1, 39, 44, 74, 15, 29, 27, 51, 49, 63,
             7, 72, 47, 54, 36,  4, 48, 71, 10, 43, 57, 78, 35, 67, 65, 16,
            56, 55, 38, 81, 58, 34, 76, 87, 21, 69, 33, 52, 70, 22, 80, 73,
             0, 42, 68, 86, 60, 18, 61, 75, 46, 79, 40, 77, 41, 89, 28, 84,
            11, 91,  9, 64, 83, 14, 94, 32, 50,  8, 30, 20, 62, 92, 12
    };

    public static final int[] SBOX_INV = new int[SIZE];

    static {
        for (int i = 0; i < SIZE; i++) SBOX_INV[SBOX[i]] = i;
    }

    public final int keyA;
    public final int keyB;
    public final int keyC;
    public final int keyD;

    public final int[] perm1;
    public final int[] perm2;
    public final String iv;

    public KeySchedule(String key, String iv) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key must be non-empty");
        }
        this.iv = padIv(iv);

        int[] k = toNums(key);
        int[] v = toNums(this.iv);

        int sumK = 0;
        int weightedK = 0;
        int squaredK = 0;
        for (int i = 0; i < k.length; i++) {
            sumK += k[i];
            weightedK += (i + 1) * k[i];
            squaredK += k[i] * k[i];
        }

        int kA = (k[0] + v[0]) % SIZE;
        int kC = (weightedK + v[2]) % SIZE;

        this.keyA = (kA == 0) ? 1 : kA;
        this.keyB = (sumK + v[1]) % SIZE;
        this.keyC = (kC == 0) ? 1 : kC;
        this.keyD = (squaredK + v[3]) % SIZE;

        this.perm1 = buildPerm1(key);
        this.perm2 = buildPerm2(this.iv);
    }

    private static int[] toNums(String s) {
        int[] result = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = Math.max(0, s.charAt(i) - OFFSET);
        }
        return result;
    }

    private static String padIv(String iv) {
        if (iv == null) iv = "";
        StringBuilder sb = new StringBuilder(iv);
        while (sb.length() < 4) sb.append(' ');
        return sb.substring(0, 4);
    }

    private static int[] buildPerm1(String key) {
        Integer[] idx = new Integer[key.length()];
        for (int i = 0; i < idx.length; i++) idx[i] = i + 1;
        Arrays.sort(idx, (a, b) -> key.charAt(a - 1) - key.charAt(b - 1));
        int[] result = new int[key.length()];
        for (int i = 0; i < result.length; i++) result[i] = idx[i];
        return result;
    }

    private static int[] buildPerm2(String iv) {
        StringBuilder ivNorm = new StringBuilder();
        while (ivNorm.length() < 8) ivNorm.append(iv);
        String ivKey = ivNorm.substring(0, 8);

        Integer[] idx = new Integer[8];
        for (int i = 0; i < 8; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> ivKey.charAt(a) - ivKey.charAt(b));
        int[] result = new int[8];
        for (int i = 0; i < 8; i++) result[i] = idx[i];
        return result;
    }
}
