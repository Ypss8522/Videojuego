import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private ImageIcon fondo;
    private Image titulo;

    public MenuPanel(Game game) {
        fondo = new ImageIcon(getClass().getResource("imagenes/menu.gif"));
        titulo = new ImageIcon(getClass().getResource("imagenes/aeiou.png")).getImage();

        int buttonWidth = 180;
        int buttonHeight = 80;
        int tituloWidth = 1000;
        int tituloHeight = 500;

        JButton startButton = new JButton(createResizedIcon("imagenes/iniciar.png", buttonWidth, buttonHeight));
        startButton.setBackground(new Color(0, 0, 0, 0));
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.showNameEntryDialog();  // Mostrar el JDialog para ingresar el nombre
            }
        });

        JButton creditsButton = new JButton(createResizedIcon("imagenes/creditos.png", buttonWidth, buttonHeight));
        creditsButton.setBackground(new Color(0, 0, 0, 0));
        creditsButton.setBorderPainted(false);
        creditsButton.setContentAreaFilled(false);
        creditsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.showPanel("Credits");
            }
        });

        JButton exitButton = new JButton(createResizedIcon("imagenes/salir.png", buttonWidth, buttonHeight));
        exitButton.setBackground(new Color(0, 0, 0, 0));
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JButton scoresButton = new JButton("Mostrar Puntuaciones");
        scoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScoresDialog();
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        ImageIcon resizedTituloIcon = createResizedIcon("imagenes/aeiou.png", tituloWidth, tituloHeight);
        JLabel image = new JLabel(resizedTituloIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(image, gbc);

        gbc.gridy = 1;
        add(scoresButton, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(startButton);
        buttonPanel.add(creditsButton);
        buttonPanel.add(exitButton);

        gbc.gridy = 2;
        add(buttonPanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    private ImageIcon createResizedIcon(String path, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(path));
        Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void showScoresDialog() {
        ScoresDialog scoresDialog = new ScoresDialog();
        scoresDialog.setVisible(true);
    }
}