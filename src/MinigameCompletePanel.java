import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * MinigameCompletePanel es un panel para un minijuego en el que los usuarios completan palabras
 * rellenando las vocales faltantes. Incluye botones para seleccionar vocales y validar respuestas.
 */
public class MinigameCompletePanel extends JPanel {
    private Image fondo; // Imagen de fondo del panel
    private JLabel exerciseArea; // Área para mostrar el ejercicio actual
    private Map<String, String> exercises; // Mapa de ejercicios con palabras incompletas y respuestas correctas
    private Map<String, String> vowelSounds; // Mapa de sonidos asociados a las vocales
    private int exerciseIndex = 0; // Índice del ejercicio actual
    private int correctAnswers = 0; // Contador de respuestas correctas
    private int incorrectAnswers = 0; // Contador de respuestas incorrectas
    private MinigamesPanel parentPanel; // Referencia al panel de minijuegos

    /**
     * Constructor del panel de minijuego.
     */
    public MinigameCompletePanel(MinigamesPanel parentPanel) {
        this.parentPanel = parentPanel; // Asignar la referencia

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

        // Inicializar los ejercicios
        exercises = getRandomExercises();

        // Crear y configurar el área para mostrar los ejercicios
        exerciseArea = new JLabel();
        exerciseArea.setBorder(BorderFactory.createEmptyBorder(50, 40, 0, 0));
        exerciseArea.setFont(new Font("Cooper Black", Font.PLAIN, 100));
        exerciseArea.setText(getCurrentExercise());
        exerciseArea.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel para el área de ejercicios y los botones
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false); // Hacer transparente el panel
        contentPanel.add(exerciseArea, BorderLayout.NORTH);

        // Panel para los botones de respuestas
        JPanel answersPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        answersPanel.setBorder(BorderFactory.createEmptyBorder(80, 40, 80, 40));
        String[] vowels = {"A", "E", "I", "O", "U"};
        Font buttonFont = new Font("Cooper Black", Font.BOLD, 60);

        // Crear botones para cada vocal
        for (String vowel : vowels) {
            JButton vowelButton = new JButton(vowel);
            vowelButton.setFont(buttonFont);
            vowelButton.setPreferredSize(new Dimension(100, 100));
            vowelButton.setBackground(Color.WHITE);
            vowelButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
            vowelButton.setFocusPainted(false);
            vowelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playSound(vowelSounds.get(vowel)); // Reproducir sonido al seleccionar una vocal
                    checkAnswer(vowel); // Verificar la respuesta del usuario
                }
            });
            answersPanel.add(vowelButton);
        }
        answersPanel.setOpaque(false); // Hacer transparente el panel de respuestas

        // Añadir el panel de respuestas al panel de contenido
        contentPanel.add(answersPanel, BorderLayout.CENTER);

        // Botón para reproducir el sonido del minijuego
        JButton soundButton = new JButton("Reproducir sonido");
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("sounds/Tercer-mini-juego.wav"); // Reproducir sonido del minijuego
            }
        });
        JPanel soundPanel = new JPanel();
        soundPanel.setOpaque(false);
        soundPanel.add(soundButton);

        // Añadir el panel de sonido y el panel de contenido al panel principal
        add(soundPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Obtiene un conjunto de ejercicios aleatorios para el minijuego.
     *
     * @return Mapa de ejercicios con palabras incompletas y respuestas correctas.
     */
    private Map<String, String> getRandomExercises() {
        Map<String, String> allExercises = new LinkedHashMap<>();
        allExercises.put("_N_C_RN__", "UNICORNIO");
        allExercises.put("S_NT_MI_NT_", "SENTIMIENTO");
        allExercises.put("_GU_N_", "IGUANA");
        allExercises.put("COR_Z_N", "CORAZON");
        allExercises.put("C_L_GI_", "COLEGIO");
        allExercises.put("_SCU_L_", "ESCUELA");
        allExercises.put("S_P_", "SAPO");
        allExercises.put("F_L_N_", "FELINO");
        allExercises.put("T_R_A", "TAREA");
        allExercises.put("D_R_ZN_", "DURAZNO");
        allExercises.put("P_N_T_N", "PANETON");
        allExercises.put("_STR_LL_", "ESTRELLA");
        allExercises.put("V_L_", "VELA");
        allExercises.put("C_LC_O", "CALCIO");
        allExercises.put("GR_CI_", "GRACIA");

        // Seleccionar 5 ejercicios aleatorios de los 15 disponibles
        java.util.List<String> keys = new ArrayList<>(allExercises.keySet());
        Collections.shuffle(keys);
        Map<String, String> selectedExercises = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            String key = keys.get(i);
            selectedExercises.put(key, allExercises.get(key));
        }

        return selectedExercises;
    }

    /**
     * Obtiene el ejercicio actual que se está mostrando.
     *
     * @return Ejercicio actual.
     */
    private String getCurrentExercise() {
        if (exercises.isEmpty()) {
            return "No exercises available.";
        }
        return (String) exercises.keySet().toArray()[exerciseIndex];
    }

    /**
     * Verifica la respuesta del usuario para una vocal seleccionada.
     *
     * @param vowel Vocal seleccionada por el usuario.
     */
    private void checkAnswer(String vowel) {
        String exerciseText = exerciseArea.getText();
        String correctAnswer = exercises.get(getCurrentExercise());
        boolean correct = false;

        // Verificar si la vocal seleccionada es correcta
        for (int i = 0; i < exerciseText.length(); i++) {
            if (exerciseText.charAt(i) == '_' && correctAnswer.charAt(i) == vowel.charAt(0)) {
                exerciseText = exerciseText.substring(0, i) + vowel + exerciseText.substring(i + 1);
                correct = true;
            }
        }

        if (correct) {
            exerciseArea.setText(exerciseText);
            if (!exerciseText.contains("_")) {
                JOptionPane.showMessageDialog(this, "¡Correcto! Vamos al siguiente.");
                correctAnswers++; // Incrementar respuestas correctas
                nextExercise();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Incorrecto. Pasando al siguiente.");
            incorrectAnswers++; // Incrementar respuestas incorrectas
            nextExercise();
        }
    }

    /**
     * Avanza al siguiente ejercicio o muestra los resultados si se han completado todos.
     */
    private void nextExercise() {
        exerciseIndex++;
        if (exerciseIndex < exercises.size()) {
            exerciseArea.setText(getCurrentExercise());
        } else {
            showResults();
        }
    }

    /**
     * Reproduce un archivo de sonido especificado.
     */
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

    /**
     * Muestra los resultados del minijuego y reinicia los contadores y ejercicios.
     */
    private void showResults() {
        ScoreManager.getInstance().increaseScore(correctAnswers);

        int totalExercises = correctAnswers + incorrectAnswers;
        double successRate = (totalExercises == 0) ? 0 : (correctAnswers / (double) totalExercises) * 100;

        JOptionPane.showMessageDialog(this, 
            "Resultados del Minijuego:\n" +
            "Aciertos: " + correctAnswers + "\n" +
            "Errores: " + incorrectAnswers + "\n" +
            "Porcentaje de efectividad: " + String.format("%.2f", successRate) + "%");

        // Reiniciar los contadores y ejercicios
        correctAnswers = 0;
        incorrectAnswers = 0;
        exerciseIndex = 0;
        exerciseArea.setText(getCurrentExercise());
        parentPanel.showMinigame("Highlight");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondo != null) {
            // Dibujar la imagen de fondo sólo si está disponible
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
