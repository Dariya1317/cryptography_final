public class ShiftLayer {

    private static final int SIZE = KeySchedule.SIZE;

    private final int keyA;
    private final int keyB;
    private final int keyC;
    private final int keyD;

    public ShiftLayer(KeySchedule ks) {
        this.keyA = ks.keyA;
        this.keyB = ks.keyB;
        this.keyC = ks.keyC;
        this.keyD = ks.keyD;
    }

    public int encrypt(int p, int row, int col) {
        return mod(p + row * keyC + col * keyA + keyB + (row ^ col) * keyD);
    }

    public int decrypt(int c, int row, int col) {
        return mod(c - row * keyC - col * keyA - keyB - (row ^ col) * keyD);
    }

    private static int mod(int x) {
        return ((x % SIZE) + SIZE) % SIZE;
    }
}
