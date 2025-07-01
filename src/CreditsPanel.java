import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreditsPanel extends JPanel {

    private ImageIcon fondo;

    public CreditsPanel(Game game) {
        fondo = new ImageIcon(getClass().getResource("imagenes/menu.gif"));

        // Crear el panel de fondo beige para el área de texto
        JPanel textBackgroundPanel = new JPanel();
        textBackgroundPanel.setOpaque(true);
        textBackgroundPanel.setBackground(new Color(245, 245, 220)); // Beige

        // Crear el área de texto con los créditos usando JTextPane
        JTextPane creditsText = new JTextPane();
        creditsText.setEditable(false);
        creditsText.setOpaque(false);

        // Establecer el estilo del texto
        StyledDocument doc = creditsText.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(center, 24);
        StyleConstants.setForeground(center, Color.BLACK);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Añadir el contenido del texto
        try {
            doc.insertString(0, "Desarrolladores:\n\n"
                    + "-Alvarez Llave Fabian Andre:\nEncargado del manejo de la lógica y desarrollo de los mini juegos.\n\n"
                    + "-Cafer Carpio Nagin Jazmin:\nEncargada de la selección de personajes y el panel del menú del juego.\n\n"
                    + "-Condori Caira Antony Beltran:\nEncargado del desarrollo del aprendizaje visualización de imágenes y sonidos del proyecto.\n\n"
                    + "-Mamani Céspedes Jhonatan Benjamin:\nEncargado del manejo de la lógica, desarrollo de los mini juegos y la base de datos.\n\n"
                    + "-Quiñonez Delgado Aaron Fernando:\nEncargado del desarrollo del aprendizaje visualización de imágenes y sonidos del proyecto.\n", center);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Añadir el área de texto al panel de fondo
        textBackgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado alrededor del texto
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        textBackgroundPanel.add(creditsText, gbc);

        // Botón de retroceder
        JButton backButton = new JButton("Retroceder");
        backButton.setFont(new Font("Arial", Font.BOLD, 30)); // Tamaño de fuente ajustado para pantalla completa
        backButton.setForeground(Color.BLACK);
        backButton.setOpaque(false);
        backButton.setBorderPainted(true);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.showPanel("Menu");
            }
        });

        // Configuración del layout principal
        setLayout(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 10, 10, 10); // Espaciado alrededor de los componentes

        // Configuración del panel de fondo beige
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.weightx = 1;
        gbcMain.weighty = 1;
        gbcMain.anchor = GridBagConstraints.CENTER;
        add(textBackgroundPanel, gbcMain);

        // Configuración del botón de retroceder
        gbcMain.gridy = 1;
        gbcMain.weighty = 0;
        add(backButton, gbcMain);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
