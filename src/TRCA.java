public class TRCA {

    private static final int OFFSET = KeySchedule.OFFSET;

    private final String key;

    public TRCA(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key must be non-empty");
        }
        this.key = key;
    }

    public CipherMessage encrypt(String plaintext) {
        return encrypt(plaintext, CipherMessage.generateIV());
    }

    public CipherMessage encrypt(String plaintext, String iv) {
        iv = CipherMessage.normalizeIV(iv);
        String filtered = TriangleLayer.filterPrintable(plaintext);
        if (filtered.isEmpty()) {
            return new CipherMessage(iv, 0, "");
        }

        KeySchedule ks = new KeySchedule(key, iv);
        ShiftLayer shift = new ShiftLayer(ks);
        FirstTranspositionLayer perm1Layer = new FirstTranspositionLayer(ks);
        SecondTranspositionLayer perm2Layer = new SecondTranspositionLayer(ks);

        TriangleLayer.Padded padded = TriangleLayer.pad(filtered);
        int total = padded.text.length();
        int n = TriangleLayer.computeN(total);

        int[][] tri = new int[n + 1][n + 1];
        for (int i = 0; i < total; i++) {
            int row = TriangleLayer.getRow(i);
            int col = TriangleLayer.getCol(i);
            int sbox = KeySchedule.SBOX[padded.text.charAt(i) - OFFSET];
            tri[row][col] = shift.encrypt(sbox, row, col);
        }

        String afterPerm1 = perm1Layer.encrypt(tri, n);
        String finalBody  = perm2Layer.encrypt(afterPerm1);
        return new CipherMessage(iv, padded.pad, finalBody);
    }

    public String decrypt(String ciphertext) {
        CipherMessage message = CipherMessage.parse(ciphertext);
        if (!message.hasBody()) return "";

        KeySchedule ks = new KeySchedule(key, message.iv);
        ShiftLayer shift = new ShiftLayer(ks);
        FirstTranspositionLayer perm1Layer = new FirstTranspositionLayer(ks);
        SecondTranspositionLayer perm2Layer = new SecondTranspositionLayer(ks);

        int total = message.body.length();
        int n = TriangleLayer.computeN(total);

        String afterPerm2 = perm2Layer.decrypt(message.body);
        int[][] tri = perm1Layer.decrypt(afterPerm2, n);

        StringBuilder result = new StringBuilder(total);
        for (int i = 0; i < total; i++) {
            int row = TriangleLayer.getRow(i);
            int col = TriangleLayer.getCol(i);
            int p = shift.decrypt(tri[row][col], row, col);
            result.append((char) (KeySchedule.SBOX_INV[p] + OFFSET));
        }

        return TriangleLayer.unpad(result.toString(), message.pad);
    }
}
