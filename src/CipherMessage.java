import java.util.Random;

public final class CipherMessage {

    private static final Random RANDOM = new Random();

    public final String iv;
    public final int pad;
    public final String body;

    public CipherMessage(String iv, int pad, String body) {
        this.iv = normalizeIV(iv);
        this.pad = Math.max(0, pad);
        this.body = body == null ? "" : body;
    }

    public static CipherMessage parse(String packaged) {
        if (packaged == null) {
            throw new IllegalArgumentException("Ciphertext is null");
        }
        String[] parts = packaged.split(":", 5);
        if (parts.length < 5 || !"IV".equals(parts[0]) || !"PAD".equals(parts[2])) {
            throw new IllegalArgumentException(
                    "Invalid format. Expected: IV:<iv>:PAD:<pad>:<body>");
        }
        int padValue;
        try {
            padValue = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("PAD must be an integer: " + parts[3]);
        }
        return new CipherMessage(parts[1], padValue, parts[4]);
    }

    public String format() {
        return "IV:" + iv + ":PAD:" + pad + ":" + body;
    }

    public boolean hasBody() {
        return !body.isEmpty();
    }

    public static String generateIV() {
        StringBuilder iv = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            iv.append((char) ('A' + RANDOM.nextInt(26)));
        }
        return iv.toString();
    }

    public static String normalizeIV(String iv) {
        if (iv == null) iv = "";
        StringBuilder sb = new StringBuilder(iv);
        while (sb.length() < 4) sb.append(' ');
        return sb.substring(0, 4);
    }
}
