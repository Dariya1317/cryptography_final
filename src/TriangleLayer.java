public final class TriangleLayer {

    public static final int MIN_CHAR = 32;
    public static final int MAX_CHAR = 126;

    private TriangleLayer() {}

    public static int computeN(int len) {
        int n = 1;
        while (n * (n + 1) / 2 < len) n++;
        return n;
    }

    public static int getRow(int i) {
        int row = 1;
        while (row * (row + 1) / 2 <= i) row++;
        return row;
    }

    public static int getCol(int i) {
        int row = getRow(i);
        return i - (row - 1) * row / 2 + 1;
    }

    public static String filterPrintable(String plaintext) {
        if (plaintext == null) return "";
        StringBuilder filtered = new StringBuilder(plaintext.length());
        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            if (c >= MIN_CHAR && c <= MAX_CHAR) filtered.append(c);
        }
        return filtered.toString();
    }

    public static Padded pad(String text) {
        int n = computeN(text.length());
        int total = n * (n + 1) / 2;
        int padCount = total - text.length();
        String padded = text + " ".repeat(padCount);
        return new Padded(padded, padCount);
    }

    public static String unpad(String paddedText, int pad) {
        if (pad < 0 || pad > paddedText.length()) {
            throw new IllegalArgumentException(
                    "Invalid pad " + pad + " for text of length " + paddedText.length());
        }
        return paddedText.substring(0, paddedText.length() - pad);
    }

    public static final class Padded {
        public final String text;
        public final int pad;

        public Padded(String text, int pad) {
            this.text = text;
            this.pad = pad;
        }
    }
}
