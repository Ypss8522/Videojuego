import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Game {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String personajeEscojido;
    private String playerName;
    private JDialog nameEntryDialog;

    public Game() {
        JFrame mainFrame = new JFrame("Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new MenuPanel(this), "Menu");
        mainPanel.add(new CharacterSelectionPanel(this), "CharacterSelection");
        mainPanel.add(new VowelLearningPanel(this), "VowelLearning");
        mainPanel.add(new EvaluationPanel(this), "Evaluation");
        mainPanel.add(new MinigamesPanel(this), "Minigame");
        mainPanel.add(new ResultsPanel(this), "Results");
        mainPanel.add(new CreditsPanel(this), "Credits"); // Agregar el CreditsPanel

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        try {
            File audioFile = new File("sounds/bg.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -28.0f;
            gainControl.setValue(volume);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showNameEntryDialog() {
        if (nameEntryDialog == null) {
            setupNameEntryDialog();
        }
        nameEntryDialog.setVisible(true);
    }

    private void setupNameEntryDialog() {
        nameEntryDialog = new JDialog((JFrame) null, "Enter Name", true);
        nameEntryDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        NameEntryPanel nameEntryPanel = new NameEntryPanel(this);
        nameEntryPanel.setDialog(nameEntryDialog);
        nameEntryDialog.add(nameEntryPanel);
        nameEntryDialog.pack();
        nameEntryDialog.setLocationRelativeTo(null);
    }

    public void selectCharacter(String characterName, String ruta) {
        this.personajeEscojido = ruta;
        System.out.println("Personaje seleccionado: " + characterName);
        showPanel("VowelLearning");
    }

    public String getSelectedCharacter() {
        return personajeEscojido;
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static void main(String[] args) {
        new Game();
    }
}