package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UtilImg {

    public static void main(String[] args) {

        Image image = loadImage("square0.png");

        // Use a label to display the image
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static BufferedImage loadImage(String filename) {
        InputStream inputStream = UtilImg.class.getClassLoader().getResourceAsStream(filename);
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return image;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}
