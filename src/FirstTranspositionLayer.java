public class FirstTranspositionLayer {

    private static final int OFFSET = KeySchedule.OFFSET;

    private final int[] perm1;

    public FirstTranspositionLayer(KeySchedule ks) {
        this.perm1 = ks.perm1;
    }

    public String encrypt(int[][] tri, int n) {
        int[] order = extendedOrder(n);
        StringBuilder body = new StringBuilder();
        for (int colJ : order) {
            for (int row = colJ; row <= n; row++) {
                body.append((char) (tri[row][colJ] + OFFSET));
            }
        }
        return body.toString();
    }

    public int[][] decrypt(String body, int n) {
        int[] order = extendedOrder(n);
        int[][] tri = new int[n + 1][n + 1];
        int pos = 0;
        for (int colJ : order) {
            int len = n - colJ + 1;
            for (int k = 0; k < len; k++) {
                tri[colJ + k][colJ] = body.charAt(pos++) - OFFSET;
            }
        }
        return tri;
    }

    private int[] extendedOrder(int n) {
        boolean[] seen = new boolean[n + 1];
        int[] result = new int[n];
        int idx = 0;
        for (int p : perm1) {
            if (p >= 1 && p <= n && !seen[p]) {
                seen[p] = true;
                result[idx++] = p;
            }
        }
        for (int j = 1; j <= n; j++) {
            if (!seen[j]) result[idx++] = j;
        }
        return result;
    }
}
