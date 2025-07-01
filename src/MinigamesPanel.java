import javax.swing.*;
import java.awt.*;

public class MinigamesPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel minigameContainer;
    private Game game;

    public MinigamesPanel(Game game) {
        this.game = game;
        cardLayout = new CardLayout();
        minigameContainer = new JPanel(cardLayout);

        // Agregar paneles de minijuegos
        minigameContainer.add(new MinigameDragPanel(this), "Drag");
        minigameContainer.add(new MinigamePaintPanel(this), "Paint");
        minigameContainer.add(new MinigameCompletePanel(this), "Complete");
        minigameContainer.add(new MinigameHighlightPanel(this), "Highlight");

        setLayout(new BorderLayout());
        add(minigameContainer, BorderLayout.CENTER);

        showMinigame("Drag");
    }

    public void showMinigame(String minigameName) {
        cardLayout.show(minigameContainer, minigameName);
    }

    public Game getGame() {
        return game;
    }

    public void showResultsPanel() {
        // Asegúrate de que el puntaje final esté actualizado antes de mostrar el ResultsPanel
        cardLayout.show(this, "Results");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Minigames");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new MinigamesPanel(null)); // Pasar la referencia del juego real en lugar de null
        frame.setVisible(true);
    }
}