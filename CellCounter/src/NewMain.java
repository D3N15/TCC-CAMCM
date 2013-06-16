/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cellcounter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Leandro
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File("./images/unifesp/salina/2-6vm/ESTdma.png"));
        int width = image.getWidth();
        int height = image.getHeight();
        
        double[][] image1 = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                image1[x][y] = 1 - (0.299d * r + 0.587 * g + 0.114 * b) / 256;
            }
        }
        
        ImageIO.write(ImageUtilities.getBufferedImage2(image1, 256.0d), "PNG", new File("./images/unifesp/salina/2-6vm/ESTdma.0.png"));
        
        double[][] image2 = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image2[x][y] = blur(image1, x, y, width, height);
            }
        }
        
        ImageIO.write(ImageUtilities.getBufferedImage2(image2, 256.0d), "PNG", new File("./images/unifesp/salina/2-6vm/ESTdma.1.png"));
    }
    
    private static double blur(double[][] image, int x, int y, int w, int h) {
        int total = 0;
        double value = 0;
        if (x-1 >= 0 && x-1 < w) {
            if (y-1 >= 0 && y-1 < h) {
                value += image[x-1][y-1];
                total++;
            }
            value += 2 * image[x-1][y];
            total += 2;
            if (y+1 >= 0 && y+1 < h) {
                value += image[x-1][y+1];
                total++;
            }
        }
        if (y-1 >= 0 && y-1 < h) {
            value += 2 * image[x][y-1];
            total += 2;
        }
        value += 4 * image[x][y];
        total += 4;
        if (y+1 >= 0 && y+1 < h) {
            value += 2 * image[x][y+1];
            total += 2;
        }
        if (x+1 >= 0 && x+1 < w) {
            if (y-1 >= 0 && y-1 < h) {
                value += image[x+1][y-1];
                total++;
            }
            value += 2 * image[x+1][y];
            total += 2;
            if (y+1 >= 0 && y+1 < h) {
                value += image[x+1][y+1];
                total++;
            }
        }
        value /= total;
        return value;
    }
}
