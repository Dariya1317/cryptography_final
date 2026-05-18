public class SecondTranspositionLayer {

    private static final int BLOCK_SIZE = 8;

    private final int[] perm2;

    public SecondTranspositionLayer(KeySchedule ks) {
        this.perm2 = ks.perm2;
    }

    public String encrypt(String text) {
        return process(text, false);
    }

    public String decrypt(String text) {
        return process(text, true);
    }

    private String process(String text, boolean inverse) {
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i += BLOCK_SIZE) {
            int len = Math.min(BLOCK_SIZE, text.length() - i);
            sb.append(permuteBlock(text, i, len, inverse));
        }
        return sb.toString();
    }

    private String permuteBlock(String text, int offset, int len, boolean inverse) {
        int[] perm = truncatedPerm(len);
        if (inverse) perm = invert(perm);

        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            result[i] = text.charAt(offset + perm[i]);
        }
        return new String(result);
    }

    private int[] truncatedPerm(int len) {
        int[] result = new int[len];
        int idx = 0;
        for (int p : perm2) if (p < len) result[idx++] = p;
        return result;
    }

    private static int[] invert(int[] perm) {
        int[] inv = new int[perm.length];
        for (int i = 0; i < perm.length; i++) inv[perm[i]] = i;
        return inv;
    }
}
