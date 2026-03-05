import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RadioButtonPetApp extends JFrame {

    private final PetCanvas petCanvas = new PetCanvas();
    private final Map<String, JRadioButton> petButtons = new LinkedHashMap<>();

    public RadioButtonPetApp() {
        super("RadioButtonDemo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new javax.swing.BoxLayout(radioPanel, javax.swing.BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        String[] pets = {"Bird", "Cat", "Dog", "Rabbit", "Pig"};

        for (String pet : pets) {
            JRadioButton rb = new JRadioButton(pet);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 20));
            rb.addActionListener(e -> {
                petCanvas.setSelectedPet(pet);
                JOptionPane.showMessageDialog(this, "You selected: " + pet, "Selection",
                        JOptionPane.INFORMATION_MESSAGE);
            });
            petButtons.put(pet, rb);
            group.add(rb);
            radioPanel.add(rb);
        }

        leftPanel.add(radioPanel);
        add(leftPanel, BorderLayout.WEST);

        petCanvas.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(petCanvas, BorderLayout.CENTER);

        petButtons.get("Pig").setSelected(true);
        petCanvas.setSelectedPet("Pig");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static class PetCanvas extends JPanel {

        private final Map<String, BufferedImage> petImages = new LinkedHashMap<>();
        private String selectedPet = "Pig";

        PetCanvas() {
            setPreferredSize(new Dimension(460, 320));
            setBackground(Color.WHITE);
            loadImages();
        }

        void setSelectedPet(String pet) {
            this.selectedPet = pet;
            repaint();
        }

        private void loadImages() {
            Map<String, String> imageNames = new LinkedHashMap<>();
            imageNames.put("Bird", "bird.jpg");
            imageNames.put("Cat", "cat.jpg");
            imageNames.put("Dog", "dog.jpg");
            imageNames.put("Rabbit", "rabbit.jpg");
            imageNames.put("Pig", "pig.jpg");

            for (Map.Entry<String, String> entry : imageNames.entrySet()) {
                File imageFile = resolveImageFile(entry.getValue());
                if (imageFile != null) {
                    try {
                        petImages.put(entry.getKey(), ImageIO.read(imageFile));
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        private File resolveImageFile(String fileName) {
            File[] candidates = {
                new File("images", fileName),
                new File("..", "images" + File.separator + fileName),
                new File(System.getProperty("user.dir") + File.separator + "images", fileName)
            };

            for (File file : candidates) {
                if (file.exists() && file.isFile()) {
                    return file;
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int frameX = 70;
            int frameY = 20;
            int frameW = 320;
            int frameH = 250;

            g2.setColor(new Color(245, 245, 245));
            g2.fillRoundRect(frameX, frameY, frameW, frameH, 24, 24);

            BufferedImage image = petImages.get(selectedPet);
            if (image != null) {
                int padding = 12;
                int availableW = frameW - (padding * 2);
                int availableH = frameH - (padding * 2);

                double scale = Math.min((double) availableW / image.getWidth(), (double) availableH / image.getHeight());
                int drawW = (int) (image.getWidth() * scale);
                int drawH = (int) (image.getHeight() * scale);

                int drawX = frameX + (frameW - drawW) / 2;
                int drawY = frameY + (frameH - drawH) / 2;

                g2.drawImage(image, drawX, drawY, drawW, drawH, null);
            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                g2.drawString("Image not found for: " + selectedPet, frameX + 24, frameY + 130);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new RadioButtonPetApp();
        });
    }
}
