import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * La clase MinigameDragPanel es un panel de minijuego que permite arrastrar y soltar vocales en una cuadrícula.
 * Incluye la funcionalidad para reproducir sonidos asociados a cada vocal y validar el orden
 * en que las vocales son colocadas en la cuadrícula.
 * 
 * El panel contiene:
 * - Un área de arrastre (dragPanel) donde se colocan las vocales.
 * - Una cuadrícula (gridPanel) donde se arrastran las vocales.
 * - Un botón para reproducir el sonido del primer ejercicio.
 * - Un botón para validar el orden de las vocales en la cuadrícula.
 */

public class MinigameDragPanel extends JPanel {
    private MinigamesPanel parentPanel; // Referencia al panel de minijuegos
    private JPanel gridPanel; // Panel que contiene la cuadrícula de vocales
    private JPanel dragPanel; // Panel que contiene las vocales que se pueden arrastrar
    private Map<String, JLabel> gridLabels; // Etiquetas de la cuadrícula
    private Image fondo; // Imagen de fondo
    private Map<String, String> vowelSounds; // Mapa de sonidos para las vocales

    public MinigameDragPanel(MinigamesPanel parentPanel) {
        this.parentPanel = parentPanel;

        // Cargar la imagen de fondo
        fondo = new ImageIcon(getClass().getResource("/imagenes/menu.gif")).getImage();

        // Inicializar los sonidos de las vocales
        vowelSounds = new LinkedHashMap<>();
        vowelSounds.put("A", "sounds/a.wav");
        vowelSounds.put("E", "sounds/e.wav");
        vowelSounds.put("I", "sounds/i.wav");
        vowelSounds.put("O", "sounds/o.wav");
        vowelSounds.put("U", "sounds/u.wav");

        setLayout(new BorderLayout());

        // Crear un botón para reproducir el sonido del primer ejercicio
        JButton soundButton = new JButton("Primer ejercicio");
        soundButton.setFont(new Font("Cooper Black", Font.BOLD, 20));
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("sounds/Primer-mini-juego.wav");
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        topPanel.add(soundButton);

        // Contenedor vertical para evitar el estiramiento vertical de los paneles
        JPanel verticalContainer = new JPanel();
        verticalContainer.setLayout(new BoxLayout(verticalContainer, BoxLayout.Y_AXIS));
        verticalContainer.setOpaque(false);

        // Panel para envolver el panel de las vocales
        JPanel dragPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20)); // Más espacio arriba
        dragPanelContainer.setOpaque(false);

        // Panel para envolver el panel de la cuadrícula
        JPanel gridPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridPanelContainer.setOpaque(false);

        // Panel para las vocales arrastrables
        dragPanel = new JPanel();
        dragPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        dragPanel.setOpaque(false);
        dragPanel.setPreferredSize(new Dimension(1200, 400));

        // Panel para la cuadrícula de colocación de vocales
        gridPanel = new JPanel(new GridLayout(1, 5, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setPreferredSize(new Dimension(1200, 300));

        gridLabels = new HashMap<>();
        Dimension labelSize = new Dimension(100, 200);
        Font labelFont = new Font("Cooper Black", Font.BOLD, 60);

        // Crear etiquetas para la cuadrícula
        for (int i = 0; i < 5; i++) {
            JLabel gridLabel = new JLabel("", SwingConstants.CENTER);
            gridLabel.setFont(labelFont);
            gridLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 6)); // Borde más grueso
            gridLabel.setPreferredSize(labelSize);
            gridLabel.setOpaque(true);
            gridLabel.setBackground(new Color(230, 230, 230));
            gridLabel.setTransferHandler(new TransferHandler("text"));
            gridLabels.put("label" + i, gridLabel);
            gridPanel.add(gridLabel);
        }

        // Crear etiquetas para las vocales arrastrables y añadir eventos de arrastre
        java.util.List<String> vowels = Arrays.asList("A", "E", "I", "O", "U");
        Collections.shuffle(vowels);

        for (String vowel : vowels) {
            JLabel dragLabel = new JLabel(vowel, SwingConstants.CENTER);
            dragLabel.setFont(new Font("Cooper Black", Font.BOLD, 60));
            dragLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 6));
            dragLabel.setOpaque(true);
            dragLabel.setBackground(Color.WHITE);
            dragLabel.setPreferredSize(new Dimension(150, 150));
            dragLabel.setTransferHandler(new TransferHandler("text"));
            dragLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JComponent c = (JComponent) e.getSource();
                    TransferHandler handler = c.getTransferHandler();
                    handler.exportAsDrag(c, e, TransferHandler.COPY);
                    playSound(vowelSounds.get(vowel)); // Reproducir sonido al arrastrar
                }
            });
            dragPanel.add(dragLabel);
        }

        // Crear botón para validar el orden de las vocales en la cuadrícula
        JButton validateButton = new JButton(new ImageIcon(getClass().getResource("/imagenes/siguiente.png")));
        validateButton.setBorderPainted(false);
        validateButton.setContentAreaFilled(false);
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateOrder();
            }
        });

        dragPanelContainer.add(dragPanel);
        gridPanelContainer.add(gridPanel);

        verticalContainer.add(Box.createVerticalGlue());
        verticalContainer.add(dragPanelContainer);
        verticalContainer.add(gridPanelContainer);
        verticalContainer.add(Box.createVerticalGlue());

        add(topPanel, BorderLayout.NORTH); // Agregar el panel superior con el botón de sonido
        add(verticalContainer, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        buttonPanel.add(validateButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Valida el orden de las vocales en la cuadrícula comparándolas con el orden correcto.
     * Actualiza el puntaje y muestra una ventana de diálogo con la puntuación.
     */

    private void validateOrder() {
        String[] correctOrder = {"A", "E", "I", "O", "U"};
        int score = 0;
    
        for (int i = 0; i < correctOrder.length; i++) {
            JLabel label = gridLabels.get("label" + i);
            if (!label.getText().isEmpty() && label.getText().equals(correctOrder[i])) {
                score++;
            }
        }

        ScoreManager.getInstance().increaseScore(score);
    
        JOptionPane.showMessageDialog(this, "Puntuación: " + score + " puntos.");
        parentPanel.showMinigame("Paint");
    }

    // Reproduce un archivo de sonido.

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
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}