import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.nio.file.*;

public class Main extends JFrame {

    private final JTextArea  inputArea    = new JTextArea(6, 40);
    private final JTextArea  outputArea   = new JTextArea(6, 40);
    private final JTextField keyField     = new JTextField(20);
    private final JTextField ivField      = new JTextField(8);
    private final JTextField padField     = new JTextField(5);
    private final JTextArea  infoArea     = new JTextArea(7, 50);
    private final JLabel     statusLabel  = new JLabel("  Ready");

    private static final Color BG      = new Color(15,  10,  35);
    private static final Color PANEL   = new Color(25,  18,  55);
    private static final Color BLUE    = new Color(60,  140, 255);
    private static final Color VIOLET  = new Color(160, 80,  240);
    private static final Color GREEN   = new Color(50,  210, 120);
    private static final Color RED     = new Color(230, 60,  80);
    private static final Color YELLOW  = new Color(240, 200, 50);
    private static final Color TEXT    = new Color(220, 215, 245);
    private static final Color BORDER  = new Color(60,  45,  100);
    private static final Color MONO    = new Color(120, 220, 180);

    public Main() {
        super("TRCA Cipher — Triangle Cipher Algorithm v3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        buildUI();
        pack();
        setMinimumSize(new Dimension(860, 700));
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(14, 14, 14, 14));
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildStatus(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);
        JLabel title = new JLabel("TRCA", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(VIOLET);
        JLabel sub = new JLabel(
                "Triangle Cipher Algorithm  v3  |  Alphabet: 95 symbols",
                SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(120, 100, 180));
        sub.setBorder(new EmptyBorder(2, 0, 8, 0));
        p.add(title, BorderLayout.CENTER);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0; g.gridwidth=1; g.weightx=0;
        p.add(label("Key:"), g);
        g.gridx=1; g.gridwidth=4; g.weightx=1;
        styleField(keyField, BLUE);
        p.add(keyField, g);

        g.gridx=0; g.gridy=1; g.gridwidth=1; g.weightx=0;
        p.add(label("IV:"), g);
        g.gridx=1; g.gridwidth=1; g.weightx=0.5;
        styleField(ivField, YELLOW);
        p.add(ivField, g);

        g.gridx=2; g.gridwidth=1; g.weightx=0;
        JLabel ivHint = new JLabel("  leave empty = random when encrypting");
        ivHint.setForeground(new Color(120, 110, 170));
        ivHint.setFont(new Font("Arial", Font.ITALIC, 11));
        p.add(ivHint, g);

        g.gridx=3; g.gridwidth=1; g.weightx=0;
        p.add(label("PAD:"), g);
        g.gridx=4; g.gridwidth=1; g.weightx=0.3;
        styleField(padField, new Color(80, 200, 200));
        padField.setToolTipText("Fill only when decrypting");
        p.add(padField, g);

        g.gridx=0; g.gridy=2; g.gridwidth=5; g.weightx=1;
        p.add(sectionLabel("— Input —", new Color(100, 90, 160)), g);

        g.gridx=0; g.gridy=3; g.gridwidth=1; g.weightx=0; g.weighty=0;
        p.add(label("Text:"), g);
        g.gridx=1; g.gridwidth=4; g.weightx=1; g.weighty=1;
        g.fill=GridBagConstraints.BOTH;
        styleArea(inputArea, TEXT);
        p.add(new JScrollPane(inputArea), g);
        g.fill=GridBagConstraints.HORIZONTAL; g.weighty=0;

        JButton btnEnc   = btn("Encrypt",      GREEN);
        JButton btnDec   = btn("Decrypt",       VIOLET);
        JButton btnCopy  = btn("Copy output",   BLUE);
        JButton btnFile  = btn("Load file",     YELLOW);
        JButton btnSave  = btn("Save output",   new Color(50, 180, 130));
        JButton btnClear = btn("Clear all",     RED);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnPanel.setBackground(BG);
        for (JButton b : new JButton[]{btnEnc, btnDec, btnCopy, btnFile, btnSave, btnClear})
            btnPanel.add(b);

        g.gridx=0; g.gridy=4; g.gridwidth=5; g.weightx=1;
        p.add(btnPanel, g);

        g.gridx=0; g.gridy=5; g.gridwidth=5; g.weightx=1;
        p.add(sectionLabel("— Output —", new Color(60, 160, 120)), g);

        g.gridx=0; g.gridy=6; g.gridwidth=1; g.weightx=0;
        p.add(label("Result:"), g);
        g.gridx=1; g.gridwidth=4; g.weightx=1; g.weighty=1;
        g.fill=GridBagConstraints.BOTH;
        styleArea(outputArea, MONO);
        outputArea.setEditable(false);
        p.add(new JScrollPane(outputArea), g);
        g.fill=GridBagConstraints.HORIZONTAL; g.weighty=0;

        g.gridx=0; g.gridy=7; g.gridwidth=5; g.weightx=1; g.weighty=0.5;
        g.fill=GridBagConstraints.BOTH;
        styleArea(infoArea, new Color(140, 160, 230));
        infoArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        infoArea.setEditable(false);
        p.add(new JScrollPane(infoArea), g);

        btnEnc.addActionListener(e  -> doEncrypt());
        btnDec.addActionListener(e  -> doDecrypt());
        btnCopy.addActionListener(e -> copyOutput());
        btnFile.addActionListener(e -> loadFile());
        btnSave.addActionListener(e -> saveOutput());
        btnClear.addActionListener(e -> doClear());

        return p;
    }

    private JPanel buildStatus() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(4, 0, 0, 0));
        statusLabel.setForeground(new Color(130, 120, 190));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(statusLabel, BorderLayout.WEST);
        return p;
    }

    private void doEncrypt() {
        try {
            String key = keyField.getText().trim();
            if (key.isEmpty()) { setStatus("Error: enter a key", true); return; }

            String ivRaw = ivField.getText().trim();
            TRCA cipher = new TRCA(key);
            String plaintext = inputArea.getText();
            CipherMessage message;

            if (ivRaw.isEmpty()) {
                message = cipher.encrypt(plaintext);
            } else {
                message = cipher.encrypt(plaintext, ivRaw);
            }

            outputArea.setText(message.format());
            ivField.setText(message.iv);
            padField.setText(String.valueOf(message.pad));

            showKeyInfo(key, message.iv);
            setStatus("Encrypted: " + plaintext.length() + " chars → " + message.body.length() + " chars  |  IV: " + message.iv + "  PAD: " + message.pad, false);

        } catch (Exception ex) {
            outputArea.setText("ERROR: " + ex.getMessage());
            setStatus("Encryption error: " + ex.getMessage(), true);
        }
    }

    private void doDecrypt() {
        try {
            String key = keyField.getText().trim();
            if (key.isEmpty()) { setStatus("Error: enter a key", true); return; }

            String iv  = ivField.getText().trim();
            String pad = padField.getText().trim();
            String body = inputArea.getText().trim();

            if (iv.isEmpty())  { setStatus("Error: enter IV", true); return; }
            if (pad.isEmpty()) { setStatus("Error: enter PAD", true); return; }

            String ciphertext = "IV:" + iv + ":PAD:" + pad + ":" + body;

            TRCA cipher = new TRCA(key);
            String result = cipher.decrypt(ciphertext);

            outputArea.setText(result);
            showKeyInfo(key, iv);
            setStatus("Decrypted: " + result.length() + " characters", false);

        } catch (Exception ex) {
            outputArea.setText("ERROR: " + ex.getMessage());
            setStatus("Decryption error: " + ex.getMessage(), true);
        }
    }

    private void copyOutput() {
        String out = outputArea.getText();
        if (!out.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(out), null);
            setStatus("Copied to clipboard", false);
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Load file");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String content = new String(
                        Files.readAllBytes(fc.getSelectedFile().toPath()));
                inputArea.setText(content);
                setStatus("File loaded: " + fc.getSelectedFile().getName()
                        + "  (" + content.length() + " characters)", false);
            } catch (IOException ex) {
                setStatus("File read error: " + ex.getMessage(), true);
            }
        }
    }

    private void saveOutput() {
        String out = outputArea.getText();
        if (out.isEmpty()) { setStatus("Nothing to save", true); return; }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save result");
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.write(fc.getSelectedFile().toPath(), out.getBytes());
                setStatus("Saved: " + fc.getSelectedFile().getName(), false);
            } catch (IOException ex) {
                setStatus("Save error: " + ex.getMessage(), true);
            }
        }
    }

    private void doClear() {
        inputArea.setText("");
        outputArea.setText("");
        infoArea.setText("");
        keyField.setText("");
        ivField.setText("");
        padField.setText("");
        setStatus("Cleared", false);
    }

    private void showKeyInfo(String key, String iv) {
        KeySchedule ks = new KeySchedule(key, CipherMessage.normalizeIV(iv));
        StringBuilder sb = new StringBuilder();
        sb.append("=== Key schedule parameters ===\n");
        sb.append(String.format("Key: \"%s\"   IV: \"%s\"\n\n", key, ks.iv));
        sb.append("-- Subkeys (formulas mod 95) --\n");
        sb.append(String.format("keyA = (k0 + v0) mod 95          = %d\n", ks.keyA));
        sb.append(String.format("keyB = (sumK + v1) mod 95        = %d\n", ks.keyB));
        sb.append(String.format("keyC = (weightedK + v2) mod 95   = %d\n", ks.keyC));
        sb.append(String.format("keyD = (squaredK + v3) mod 95    = %d\n\n", ks.keyD));
        sb.append("-- Shift formula --\n");
        sb.append(String.format(
                "c = (p + row*%d + col*%d + %d + (row XOR col)*%d) mod 95\n\n",
                ks.keyC, ks.keyA, ks.keyB, ks.keyD));
        sb.append("-- perm1 (column order, from key) --\n[");
        for (int i = 0; i < ks.perm1.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(ks.perm1[i]);
        }
        sb.append("]\n\n-- perm2 (blocks of 8, from IV) --\n[");
        for (int i = 0; i < ks.perm2.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(ks.perm2[i]);
        }
        sb.append("]");
        infoArea.setText(sb.toString());
        infoArea.setCaretPosition(0);
    }

    private void setStatus(String msg, boolean error) {
        statusLabel.setText("  " + msg);
        statusLabel.setForeground(error ? RED : new Color(130, 120, 190));
    }

    private static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    private static JLabel sectionLabel(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(color);
        l.setFont(new Font("Arial", Font.BOLD, 11));
        l.setBorder(new EmptyBorder(4, 0, 2, 0));
        return l;
    }

    private static void styleField(JTextField f, Color accent) {
        f.setBackground(PANEL);
        f.setForeground(TEXT);
        f.setCaretColor(accent);
        f.setFont(new Font("Courier New", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(accent, 1, true),
                new EmptyBorder(3, 6, 3, 6)));
    }

    private static void styleArea(JTextArea a, Color fg) {
        a.setBackground(PANEL);
        a.setForeground(fg);
        a.setCaretColor(fg);
        a.setFont(new Font("Courier New", Font.PLAIN, 12));
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new LineBorder(BORDER, 1, true));
    }

    private static JButton btn(String text, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(new Color(fg.getRed()/4, fg.getGreen()/4, fg.getBlue()/4));
        b.setForeground(fg);
        b.setFont(new Font("Arial", Font.BOLD, 11));
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(fg.darker(), 1, true),
                new EmptyBorder(5, 12, 5, 12)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(fg.getRed()/3, fg.getGreen()/3, fg.getBlue()/3));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(fg.getRed()/4, fg.getGreen()/4, fg.getBlue()/4));
            }
        });
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new Main().setVisible(true);
        });
    }
}