import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * VowelLearningPanel crea una interfaz gráfica para que los niños aprendan las vocales.
 * Presenta botones para cada vocal que reproducen el sonido correspondiente al hacer clic, un fondo
 * y un botón "Siguiente" para avanzar en el juego.
 */

public class VowelLearningPanel extends JPanel {

    private Image fondo;
    private Map<String, String> sonidosVocales;

    public VowelLearningPanel(Game game) {
        // Añadir la imagen de fondo
        fondo = new ImageIcon(getClass().getResource("/imagenes/menu.gif")).getImage();

        //Almacenar ruta de los archivos de sonido
        sonidosVocales = new LinkedHashMap<>();
        sonidosVocales.put("A", "sounds/a.wav");
        sonidosVocales.put("E", "sounds/e.wav");
        sonidosVocales.put("I", "sounds/i.wav");
        sonidosVocales.put("O", "sounds/o.wav");
        sonidosVocales.put("U", "sounds/u.wav");

        setLayout(new BorderLayout());

        // Panel para el título
        JPanel panelTitle = new JPanel();
        panelTitle.setOpaque(false); // Hacer transparente el panel
        ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/vocalesTitulo.png"));
        JLabel titleLabel = new JLabel(icon);
        panelTitle.add(titleLabel);
        
        // Panel para las vocales
        JPanel panelVowels = new JPanel();
        panelVowels.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Ajustar espacio entre botones
        panelVowels.setOpaque(false); // Hacer transparente el panel
        for (String vocal : sonidosVocales.keySet()) {
            JButton vowelsBtn = new JButton(vocal);
            vowelsBtn.setFont(new Font("Cooper Black", Font.PLAIN, 100)); // Establecemos el decorado del texto
            vowelsBtn.setPreferredSize(new Dimension(200, 200)); // Ajustar el tamaño de los botones
            vowelsBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    playSound(sonidosVocales.get(vocal)); //Ejecución del archivo de sonido
                }
            });
            panelVowels.add(vowelsBtn);
        }

        // Panel intermedio para centrar el panelVocales verticalmente
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setOpaque(false); // Hacer transparente el panel
        panelCenter.add(Box.createVerticalGlue()); // Añadir espacio elástico arriba
        panelCenter.add(panelVowels); // Añadir panelVocales al panelCentro
        panelCenter.add(Box.createVerticalGlue()); // Añadir espacio elástico abajo

        // Botón Siguiente
        JButton siguienteBtn = new JButton("Siguiente");
        siguienteBtn.setFont(new Font("Cooper Black", Font.PLAIN, 30)); // Establecemos el decorado del texto
        siguienteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.showPanel("Evaluation");
            }
        });

        // Añadir paneles al panel principal
        add(panelTitle, BorderLayout.NORTH);
        add(panelCenter, BorderLayout.CENTER);
        add(siguienteBtn, BorderLayout.SOUTH);
    }

    private void playSound(String soundFile) { //Método para reproducir sonidos
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
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}