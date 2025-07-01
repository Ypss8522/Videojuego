import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MinigameHighlightPanel extends JPanel {
    private Image fondo;
    private int totalScore;
    private MinigamesPanel parentPanel;
    private JLabel[] letterLabels;
    private Map<Character, String> vowelSounds;
    private static final String[] WORDS = {"ELEFANTE", "UKELELE", "IGLESIA", "OSCURO", "ANILLO"};
    private int currentWordIndex = 0;
    private JButton nextButton;
    private JButton soundButton;
    private int wordScore;
    private int totalVowels;
    private int markedVowels;
    private JPanel contentPanel; // Panel para el contenido principal

    /**
 * MinigameHighlightPanel es un panel para un minijuego en el que los usuarios resaltan vocales
 * Haciendo click cobre las vocales. Incluye colores diferenciadores si te equivocas o aciertas, incluye botones validar respuestas.
 */

    public MinigameHighlightPanel(MinigamesPanel parentPanel) {
        this.parentPanel = parentPanel;

        fondo = new ImageIcon(getClass().getResource("/imagenes/menu.gif")).getImage();

        vowelSounds = new LinkedHashMap<>();
        vowelSounds.put('A', "sounds/a.wav");
        vowelSounds.put('E', "sounds/e.wav");
        vowelSounds.put('I', "sounds/i.wav");
        vowelSounds.put('O', "sounds/o.wav");
        vowelSounds.put('U', "sounds/u.wav");

        setLayout(new BorderLayout()); // Usar BorderLayout para organizar los componentes

        soundButton = new JButton("Reproducir sonido");
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("sounds/Cuarto-mini-juego.wav");
            }
        });

        JPanel soundPanel = new JPanel();
        soundPanel.setOpaque(false);
        soundPanel.add(soundButton);

        add(soundPanel, BorderLayout.NORTH);

        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        nextButton = new JButton(new ImageIcon(getClass().getResource("/imagenes/siguiente.png")));
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.addActionListener(e -> goToNextWord());

        displayCurrentWord();
    }

    private void displayCurrentWord() {
        contentPanel.removeAll();
        String word = WORDS[currentWordIndex];
        letterLabels = new JLabel[word.length()];
        wordScore = 0;
        totalVowels = 0;
        markedVowels = 0;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        for (int i = 0; i < word.length(); i++) {
            letterLabels[i] = createLetterLabel(word.charAt(i));
            if (isVowel(word.charAt(i))) {
                totalVowels++;
            }
            gbc.gridx = i;
            contentPanel.add(letterLabels[i], gbc);
        }

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(30, 10, 10, 10);
        contentPanel.add(nextButton, gbc);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JLabel createLetterLabel(char letter) {
        JLabel label = new JLabel(String.valueOf(letter), SwingConstants.CENTER);
        label.setFont(new Font("Cooper Black", Font.PLAIN, 70));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(150, 150));
        label.addMouseListener(new HighlightMouseAdapter(label, letter));
        return label;
    }

    private class HighlightMouseAdapter extends MouseAdapter {
        private JLabel label;
        private char letter;

        public HighlightMouseAdapter(JLabel label, char letter) {
            this.label = label;
            this.label.setOpaque(true);
            this.letter = letter;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (label.getBackground() == Color.WHITE) {
                if (isVowel(letter)) {
                    label.setBackground(Color.GREEN);
                    playSound(vowelSounds.get(letter));
                    markedVowels++;
                } else {
                    label.setBackground(Color.RED);
                }
                label.removeMouseListener(this);
            }
        }
    }

    private boolean isVowel(char c) {
        return "AEIOUaeiou".indexOf(c) != -1;
    }

    private void playSound(String soundFile) {
        try {
            File audioFile = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    private void goToNextWord() {
        if (markedVowels == totalVowels) {
            wordScore = 1;
        } else {
            wordScore = 0;
        }

        totalScore += wordScore;

        if (currentWordIndex < WORDS.length - 1) {
            currentWordIndex++;
            displayCurrentWord();
        } else {
            showResults();
        }
    }

    private void showResults() {
        ScoreManager.getInstance().increaseScore(totalScore);
        JOptionPane.showMessageDialog(this,
            "Resultados del Minijuego:\n" +
            "Puntaje del ejercicio: " + totalScore);
        currentWordIndex = 0;
        parentPanel.getGame().showPanel("Results");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}