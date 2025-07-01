import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * MinigamePaintPanel es un panel para un minijuego en el que los usuarios dibujan las vocales
 * haciendo click en los recuadros logrando asi pintar cada recuadro. Incluye botones para reproducir la informacion y validar respuestas.
 */

public class MinigamePaintPanel extends JPanel {
    private static final int[] GRID_ROWS = {5, 5, 5, 5, 5};
    private static final int[] GRID_COLS = {4, 3, 3, 4, 4};
    private static final int CELL_SIZE = 100;
    private int level = 0;
    private boolean[][] paintedCells;
    private MinigamesPanel parentPanel;
    private JButton nextLevelButton;
    private JLabel feedbackLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private Image backgroundImage;

    private Set<Point> level1Points = new HashSet<>() {{
        add(new Point(0, 1));
        add(new Point(0, 2));
        add(new Point(1, 0));
        add(new Point(1, 3));
        add(new Point(2, 0));
        add(new Point(2, 3));
        add(new Point(3, 0));
        add(new Point(3, 1));
        add(new Point(3, 2));
        add(new Point(3, 3));
        add(new Point(4, 0));
        add(new Point(4, 3));
    }};

    private Set<Point> level2Points = new HashSet<>() {{
        add(new Point(0, 0));
        add(new Point(0, 1));
        add(new Point(0, 2));
        add(new Point(1, 0));
        add(new Point(2, 0));
        add(new Point(2, 1));
        add(new Point(2, 2));
        add(new Point(3, 0));
        add(new Point(4, 0));
        add(new Point(4, 1));
        add(new Point(4, 2));
    }};

    private Set<Point> level3Points = new HashSet<>() {{
        add(new Point(0, 0));
        add(new Point(0, 1));
        add(new Point(0, 2));
        add(new Point(1, 1));
        add(new Point(2, 1));
        add(new Point(3, 1));
        add(new Point(4, 0));
        add(new Point(4, 1));
        add(new Point(4, 2));
    }};

    private Set<Point> level4Points = new HashSet<>() {{
        add(new Point(0, 0));
        add(new Point(0, 1));
        add(new Point(0, 2));
        add(new Point(0, 3));
        add(new Point(1, 0));
        add(new Point(1, 3));
        add(new Point(2, 0));
        add(new Point(2, 3));
        add(new Point(3, 0));
        add(new Point(3, 3));
        add(new Point(4, 0));
        add(new Point(4, 1));
        add(new Point(4, 2));
        add(new Point(4, 3));
    }};

    private Set<Point> level5Points = new HashSet<>() {{
        add(new Point(0, 0));
        add(new Point(0, 3));
        add(new Point(1, 0));
        add(new Point(1, 3));
        add(new Point(2, 0));
        add(new Point(2, 3));
        add(new Point(3, 0));
        add(new Point(3, 3));
        add(new Point(4, 0));
        add(new Point(4, 1));
        add(new Point(4, 2));
        add(new Point(4, 3));
    }};

    public MinigamePaintPanel(MinigamesPanel parentPanel) {
        this.parentPanel = parentPanel;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GRID_COLS[level] * CELL_SIZE + 200, GRID_ROWS[level] * CELL_SIZE + 200));
        setBackground(Color.WHITE);
        paintedCells = new boolean[GRID_ROWS[level]][GRID_COLS[level]];

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/imagenes/menu.gif")).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton soundButton = new JButton("Reproducir Sonido");
        soundButton.addActionListener(e -> playSound("sounds/Segundo-mini-juego.wav"));

        nextLevelButton = new JButton("Siguiente Nivel");
        nextLevelButton.setFont(new Font("Cooper Black", Font.BOLD, 18));
        nextLevelButton.addActionListener(e -> {
            if (isLevelCorrect()) {
                feedbackLabel.setText("¡Correcto!");
                JOptionPane.showMessageDialog(MinigamePaintPanel.this, "¡Correcto!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                ScoreManager.getInstance().increaseScore(1);
            } else {
                feedbackLabel.setText("Te equivocaste.");
                JOptionPane.showMessageDialog(MinigamePaintPanel.this, "Te equivocaste.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (level < 4) {
                level++;
                resetGrid();
            } else {
                showEvaluation();
            }
        });

        feedbackLabel = new JLabel("Haz clic en la cuadrícula para pintar las celdas.");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Cooper Black", Font.PLAIN, 16));

        titleLabel = new JLabel("Segundo minijuego: \"Colorea las vocales\"");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Cooper Black", Font.BOLD, 24));

        subtitleLabel = new JLabel("Pinta los cuadrados vacíos para dibujar las vocales");
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Cooper Black", Font.PLAIN, 18));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setOpaque(false);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextLevelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(titleLabel);
        controlPanel.add(subtitleLabel);
        controlPanel.add(feedbackLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlPanel.add(soundButton, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.CENTER);
        add(nextLevelButton, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int offsetX = (getWidth() - GRID_COLS[level] * CELL_SIZE) / 2;
                int offsetY = (getHeight() - GRID_ROWS[level] * CELL_SIZE) / 2;
                int row = (e.getY() - offsetY) / CELL_SIZE;
                int col = (e.getX() - offsetX) / CELL_SIZE;
                if (row >= 0 && row < GRID_ROWS[level] && col >= 0 && col < GRID_COLS[level] && !paintedCells[row][col]) {
                    paintedCells[row][col] = true;
                    repaint();
                }
            }
        });
    }

    private boolean isLevelCorrect() {
        Set<Point> currentLevelPoints = getCurrentLevelPoints();
        for (Point p : currentLevelPoints) {
            if (!paintedCells[p.x][p.y]) {
                return false;
            }
        }
        for (int row = 0; row < GRID_ROWS[level]; row++) {
            for (int col = 0; col < GRID_COLS[level]; col++) {
                if (paintedCells[row][col] && !currentLevelPoints.contains(new Point(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    private Set<Point> getCurrentLevelPoints() {
        switch (level) {
            case 0:
                return level1Points;
            case 1:
                return level2Points;
            case 2:
                return level3Points;
            case 3:
                return level4Points;
            case 4:
                return level5Points;
            default:
                return new HashSet<>();
        }
    }

    private void resetGrid() {
        paintedCells = new boolean[GRID_ROWS[level]][GRID_COLS[level]];
        setPreferredSize(new Dimension(GRID_COLS[level] * CELL_SIZE + 200, GRID_ROWS[level] * CELL_SIZE + 200));
        revalidate();
        repaint();
        feedbackLabel.setText("Haz clic en la cuadrícula para pintar las celdas.");
    }

    private void showEvaluation() {
        // Logic to calculate the final score for the game
        JOptionPane.showMessageDialog(this,
                "Evaluación terminada.",
                "Evaluación",
                JOptionPane.INFORMATION_MESSAGE);
        parentPanel.showMinigame("Complete");
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        int offsetX = (getWidth() - GRID_COLS[level] * CELL_SIZE) / 2;
        int offsetY = (getHeight() - GRID_ROWS[level] * CELL_SIZE) / 2;
        for (int row = 0; row < GRID_ROWS[level]; row++) {
            for (int col = 0; col < GRID_COLS[level]; col++) {
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;
                g.setColor(Color.BLACK);
                g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                if (paintedCells[row][col]) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x + 1, y + 1, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }
}