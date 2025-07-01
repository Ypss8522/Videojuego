import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.geom.RoundRectangle2D;

/**
 * CharacterSelectionPanel crea una interfaz gráfica para que los niños seleccionen un personaje.
 * Presenta botones para cada personaje, una imagen de fondo, y un mensaje de selección de personajes (en la terminal).
 */

public class CharacterSelectionPanel extends JPanel {
    private Image fondo;

    public CharacterSelectionPanel(Game game) {
        // Añado la imagen de fondo
        fondo = new ImageIcon(getClass().getResource("/imagenes/menu.gif")).getImage();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(20, 20, 20, 20); // Espaciado alrededor de los componentes

        // Etiqueta de mensaje
        JLabel messageLabel = new JLabel("                Elija un personaje");
        messageLabel.setFont(new Font("Cooper Black", Font.BOLD, 50)); // Ajusta la fuente del mensaje
        messageLabel.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Cubre tres columnas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(messageLabel, gbc);

        // Botones de personajes
        gbc.gridwidth = 1; // Restablecer a una columna por botón
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1;

        // Botón de personaje 1
        gbc.gridx = 0;
        this.add(createCharacterButton(game, "imagenes/stitch.png", "Stitch"), gbc);

        // Botón de personaje 2
        gbc.gridx = 1;
        this.add(createCharacterButton(game, "imagenes/pooh.png", "Pooh"), gbc);

        // Botón de personaje 3
        gbc.gridx = 2;
        this.add(createCharacterButton(game, "imagenes/dalmata.png", "Dalmata"), gbc);
    }

    private JPanel createCharacterButton(Game game, String imagePath, String characterName) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);

        JButton button = new JButton(new ImageIcon(image)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                super.paintComponent(g);
            }

            @Override
            public void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        button.setPreferredSize(new java.awt.Dimension(260, 260));
        button.setBackground(new Color(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.selectCharacter(characterName, imagePath);
            }
        });

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(button);
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new java.awt.Dimension(270, 270));
        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}

