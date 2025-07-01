import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResultsPanel extends JPanel {
    private Game game;
    private static final int MAX_SCORE = 20;
    private JLabel scoreDetails;
    private Image fondo;

    public ResultsPanel(Game game) {
        this.game = game;
        fondo = new ImageIcon(getClass().getResource("/imagenes/menu.gif")).getImage();

        setLayout(new BorderLayout());
        setOpaque(false);

        // Title
        JLabel scoreLabel = new JLabel("Tu examen ha terminado", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Cooper Black", Font.BOLD, 80));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(60, 0, 30, 0));
        add(scoreLabel, BorderLayout.NORTH);

        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);
        scorePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        scoreDetails = new JLabel("", SwingConstants.CENTER);
        scoreDetails.setFont(new Font("Cooper Black", Font.PLAIN, 80));
        scoreDetails.setForeground(Color.BLACK);
        scorePanel.add(scoreDetails, gbc);

        add(scorePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(10, 10, 60, 10);
        buttonGbc.anchor = GridBagConstraints.CENTER;
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;

        JButton showScoreButton = new JButton("Mostrar Puntaje");
        showScoreButton.setFont(new Font("Cooper Black", Font.PLAIN, 40));
        showScoreButton.setBackground(Color.BLACK);
        showScoreButton.setForeground(Color.WHITE);
        showScoreButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        showScoreButton.setFocusPainted(false);
        showScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScore();
                insertScoreIntoDatabase();
            }
        });
        buttonPanel.add(showScoreButton, buttonGbc);

        JButton backButton = new JButton("Volver al Menú");
        backButton.setFont(new Font("Cooper Black", Font.PLAIN, 40));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> game.showPanel("Menu"));
        buttonGbc.gridy = 1;
        buttonPanel.add(backButton, buttonGbc);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateScore() {
        int finalScore = ScoreManager.getInstance().getScore();
        String playerName = game.getPlayerName();
        scoreDetails.setText(String.format("Puntaje final de %s: %d/%d", playerName, finalScore, MAX_SCORE));
        revalidate();
        repaint();
    }

    private void insertScoreIntoDatabase() {
        int finalScore = ScoreManager.getInstance().getScore();
        String playerName = game.getPlayerName();
        Connection connection = DatabaseConnection.getInstance().getConnection();

        String sql = "INSERT INTO puntuacion (nombre, puntaje) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerName);
            statement.setInt(2, finalScore);
            statement.executeUpdate();
            System.out.println("Puntaje insertado en la base de datos: " + finalScore);
        } catch (SQLException e) {
            e.printStackTrace();
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