/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cellcounter;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 *
 * @author Leandro
 */
public class ImageUtilities {
    
    private static final int[][] MASK_3x3 = {
        {1, 2, 1},
        {2, 4, 2},
        {1, 2, 1}
    };
    
    private static final int[][] MASK_5x5 = {
        {1,  4,  7,  4,  1},
        {4, 16, 26, 16,  4},
        {7, 26, 41, 26,  7},
        {4, 16, 26, 16,  4},
        {1,  4,  7,  4,  1}
    };

    private static final int[][] MASK_7x7 = {
        { 0,  0,  1,  2,  1,  0,  0},
        { 0,  3, 13, 22, 13,  3,  0},
        { 1, 13, 59, 97, 59, 13,  1},
        { 2, 22, 97,159, 97, 22,  2},
        { 1, 13, 59, 97, 59, 13,  1},
        { 0,  3, 13, 22, 13,  3,  0},
        { 0,  0,  1,  2,  1,  0,  0},
    };
//
//    private static final byte[][] MASK_9x9 = {
//        {-4, -3, -2, -1,  0,  1,  2,  3,  4},
//        {-5, -4, -3, -2,  0,  2,  3,  4,  5},
//        {-6, -5, -4, -3,  0,  3,  4,  5,  6},
//        {-7, -6, -5, -4,  0,  4,  5,  6,  7},
//        {-8, -7, -6, -5,  0,  5,  6,  7,  8},
//        {-7, -6, -5, -4,  0,  4,  5,  6,  7},
//        {-6, -5, -4, -3,  0,  3,  4,  5,  6},
//        {-5, -4, -3, -2,  0,  2,  3,  4,  5},
//        {-4, -3, -2, -1,  0,  1,  2,  3,  4}
//    };
    
    public static int[][] getMask(int size) throws Exception {
        switch (size) {
            case 3: return MASK_3x3;
            case 5: return MASK_5x5;
            case 7: return MASK_7x7;
//            case 9: return MASK_9x9;
            default: throw new Exception("Mask not defined.");
        }
    }
    
    public static BufferedImage cloneImage(BufferedImage imageIn) {
        int width = imageIn.getWidth();
        int height = imageIn.getHeight();
        int imageType = imageIn.getType();
        BufferedImage imageOut = new BufferedImage(width, height, imageType);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = imageIn.getRGB(x, y);
                imageOut.setRGB(x, y, rgb);
            }
        }
        return imageOut;
    }
    
    public static BufferedImage cloneImageGray(BufferedImage imageIn) {
        int width = imageIn.getWidth();
        int height = imageIn.getHeight();
        int imageType = imageIn.getType();
        BufferedImage imageOut = new BufferedImage(width, height, imageType);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(imageIn.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int itensity = (int) (0.3f * red + 0.59f * green + 0.11f * blue);
                color = new Color(itensity, itensity, itensity);
                imageOut.setRGB(x, y, color.getRGB());
            }
        }
        return imageOut;
    }

    public static void erosao(double[][] image, int maskSize) throws Exception {
        int width = image.length;
        int height = image[0].length;
        double[][] edge = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double gx = SobelUtilities.convolve(image, x, y, maskSize, false);
                double gy = SobelUtilities.convolve(image, x, y, maskSize, true);
                edge[x][y] = Math.sqrt(gx * gx + gy * gy);
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image[x][y] *= 1.0d - edge[x][y];
            }
        }
    }
    
    public static void expansao(double[][] image, int maskSize) throws Exception {
        int width = image.length;
        int height = image[0].length;
        double[][] edge = new double[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double gx = SobelUtilities.convolve(image, x, y, maskSize, false);
                double gy = SobelUtilities.convolve(image, x, y, maskSize, true);
                edge[x][y] = Math.sqrt(gx * gx + gy * gy);
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image[x][y] = 1.0d - (1.0d - image[x][y] * 1.0d - edge[x][y]);
            }
        }
    }
    
    public static BufferedImage getBufferedImage(double[][] image, double scale) {
        int width = image.length;
        int height = image[0].length;
        BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double i = 255.0d * scale * image[x][y];
                Color color = ImageUtilities.getColor(i, i, i);
                imageOut.setRGB(x, y, color.getRGB());
            }
        }
        return imageOut;
    }
    
    public static BufferedImage getBufferedImage2(double[][] image, double scale) {
        int width = image.length;
        int height = image[0].length;
        BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double i = scale * image[x][y];
                Color color = ImageUtilities.getColor(i, i, i);
                imageOut.setRGB(x, y, color.getRGB());
            }
        }
        return imageOut;
    }
    
    public static double distance(Color baseColor, Color mensuredColor) {
        int redBase = baseColor.getRed();
        int greenBase = baseColor.getGreen();
        int blueBase = baseColor.getBlue();
        int redMensure = mensuredColor.getRed();
        int greenMensure = mensuredColor.getGreen();
        int blueMensure = mensuredColor.getBlue();
        double redDelta = redMensure - redBase;
        double greenDelta = greenMensure - greenBase;
        double blueDelta = blueMensure - blueBase;
        return Math.sqrt(redDelta * redDelta + greenDelta * greenDelta + blueDelta * blueDelta);
    }
    
    public static Point getMaximunPoint(double[][] image) {
        int width = image.length;
        int height = image[0].length;
        double max = 0;
        Point point = null;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (image[x][y] > max) {
                    max = image[x][y];
                    point = new Point(x, y);
                }
            }
        }
        return point;
    }
    
    public static Color getColor(double red, double green, double blue) {
        int r = (int) Math.round(red);
        int g = (int) Math.round(green);
        int b = (int) Math.round(blue);
        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = 255;
        }
        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = 255;
        }
        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = 255;
        }
        return new Color(r, g, b);
    }
    
    public static Color getAverageColor(Collection<Color> colorCollection) {
        if (colorCollection.isEmpty()) {
            return null;
        } else {
            double red = 0.0d;
            double green = 0.0d;
            double blue = 0.0d;
            for (Color color : colorCollection) {
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
            }
            red /= colorCollection.size();
            green /= colorCollection.size();
            blue /= colorCollection.size();
            return getColor(red, green, blue);
        }
    }
    
    public static Polygon getCircle(double x, double y, double r) {
        Polygon circle = new Polygon();
        for (int theta = 0; theta < 360; theta += 15) {
            double alpha = Math.toRadians(theta);
            int i = (int) Math.round(r * Math.cos(alpha) + x);
            int j = (int) Math.round(r * Math.sin(alpha) + y);
            circle.addPoint(i, j);
        }
        return circle;
    }
    
    public static double blur3x3(double[][] image, int x, int y, int w, int h) {
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
    
    public static double blur(
            double[][] image,
            int x,
            int y,
            int maskSize
            ) throws Exception {
        
        int[][] mask = getMask(maskSize);
        int m = maskSize / 2;
        
        int kLimit = image.length;
        int lLimit = image[0].length;
        
        int iLimit = mask.length;
        int jLimit = mask[0].length;
        
        double itensity = 0;
        int total = 0;
        
        for (int j = 0; j < iLimit; j++) {
            for (int i = 0; i < jLimit; i++) {
                int k = x + i - m;
                int l = y + j - m;
                if (k >= 0 && k < kLimit && l >= 0 && l < lLimit) {
                    double color = image[k][l];
                    int weight = mask[i][j];
                    itensity += weight * color;
                    total += weight;
                }
            }
        }
        itensity /= total;
        return itensity;
    }
    
    public static Color getInsideColor(BufferedImage image, Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();
        int minX = (int) Math.floor(bounds.getMinX());
        int maxX = (int) Math.ceil(bounds.getMaxX());
        int minY = (int) Math.floor(bounds.getMinY());
        int maxY = (int) Math.ceil(bounds.getMaxY());
        int width = image.getWidth();
        int height = image.getHeight();
        if (minX < 0) {
            minX = 0;
        }
        if (maxX >= width) {
            maxX = width - 1;
        }
        if (minY < 0) {
            minY = 0;
        }
        if (maxY >= height) {
            maxY = height - 1;
        }
        int red = 0;
        int green = 0;
        int blue = 0;
        int count = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                if (shape.contains(x, y)) {
                    Color color = new Color(image.getRGB(x, y));
                    red += color.getRed();
                    green += color.getGreen();
                    blue += color.getBlue();
                    count++;
                }
            }
        }
        if (count == 0) {
            return null;
        } else {
            red /= count;
            green /= count;
            blue /= count;
            return new Color(red, green, blue);
        }
    }
}
