/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cellcounter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Leandro
 */
public class CenterImageView extends org.jdesktop.swingx.JXImageView {

    private int axisX = 0;
    private int axisY = 0;
    private int imageCenterX = 0;
    private int imageCenterY = 0;
    private int diameter = 18;
    private Color axisColor = Color.RED;
    private Color circleColor = Color.BLUE;
    private Color polygonColor = Color.GREEN;
    
    /**
     * Creates new form ImagePanel
     */
    public CenterImageView() {
        initComponents();
        axisX = getWidth() / 2;
        axisY = getHeight() / 2;
    }
    
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (getImage() != null) {
            graphics.setColor(axisColor);
            graphics.drawLine(axisX, 0, axisX, getHeight());
            graphics.drawLine(0, axisY, getWidth(), axisY);
            graphics.setColor(circleColor);
            graphics.drawOval(axisX-diameter/2, axisY-diameter/2, diameter, diameter);
        }
    }
    
    public Point getNextGreenPixel() {
        BufferedImage bufferedImage = getBufferedImage();
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(bufferedImage.getRGB(x, y));
                if (color.equals(Color.GREEN)) {
                    bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
    
    public Point getNextRedPixel() {
        BufferedImage bufferedImage = getBufferedImage();
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(bufferedImage.getRGB(x, y));
                if (color.equals(Color.RED)) {
                    bufferedImage.setRGB(x, y, Color.WHITE.getRGB());
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
    
    public void setAxisColor(Color color) {
        axisColor = color;
        repaint();
    }
    
    public void setCircleColor(Color color) {
        circleColor = color;
        repaint();
    }
    
    public void addPolygon(Polygon polygon) {
        BufferedImage image = getBufferedImage();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(polygonColor);
        graphics.drawPolygon(polygon);
    }
    
    public Color getInsideColor() {
        return getInsideColor(
                imageCenterX,
                imageCenterY,
                diameter
                );
    }
    
    public Color getInsideColor(int x, int y, double d) {
        double r = 0.5d * d;
        Ellipse2D ellipse = new Ellipse2D.Double(x - r, y - r, d, d);
        BufferedImage image = getBufferedImage();
        return ImageUtilities.getInsideColor(image, ellipse);
    }
    
    public Color getPixelColor(int x, int y) {
        BufferedImage image = getBufferedImage();
        return new Color(image.getRGB(x, y));
    }
    
    public void setPixelColor(Color color, int x, int y) {
        BufferedImage image = getBufferedImage();
        image.setRGB(x, y, color.getRGB());
    }
    
    public Color getPixelColor(int x, int y, int maskSize) throws Exception {
        int[][] mask = ImageUtilities.getMask(maskSize);
        int m = mask.length;
        int n = maskSize / 2;
        BufferedImage image = getBufferedImage();
        int width = image.getWidth();
        int height = image.getHeight();
        double red = 0.0d;
        double green = 0.0d;
        double blue = 0.0d;
        int sum = 0;
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < m; i++) {
                int k = i + x - n;
                int l = j + y - n;
                if (k >= 0 && l >= 0 && k < width && l < height) {
                    int s = mask[i][j];
                    Color color = new Color(image.getRGB(k, l));
                    red += s * color.getRed();
                    green += s * color.getGreen();
                    blue += s * color.getBlue();
                    sum += s;
                }
            }
        }
        red /= sum;
        green /= sum;
        blue /= sum;
        return ImageUtilities.getColor(red, green, blue);
    }
    
    @Override
    public void setImage(Image image) {
        super.setImage(image);
        if (image != null) {
            BufferedImage bufferedImage = getBufferedImage();
            imageCenterX = bufferedImage.getWidth() / 2;
            imageCenterY = bufferedImage.getHeight() / 2;
        }
    }
    
    public BufferedImage getBufferedImage() {
        return (BufferedImage) getImage();
    }
    
    public Point getCenterPoint() {
        return new Point(imageCenterX, imageCenterY);
    }
    
    public int getDiameter() {
        return diameter;
    }
    
    public int getImageWidth() {
        return getBufferedImage().getWidth();
    }
    
    public int getImageHeight() {
        return getBufferedImage().getHeight();
    }
    
    public void moveHorizontaly(int value) {
        Point2D location = getImageLocation();
        int x = (int) Math.round(location.getX()) - value;
        int y = (int) Math.round(location.getY());
        location.setLocation(x, y);
        setImageLocation(location);
    }
    
    public void moveVerticaly(int value) {
        Point2D location = getImageLocation();
        int x = (int) Math.round(location.getX());
        int y = (int) Math.round(location.getY()) - value;
        location.setLocation(x, y);
        setImageLocation(location);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                formPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        axisX = getWidth() / 2;
        axisY = getHeight() / 2;
        repaint();
    }//GEN-LAST:event_formComponentResized

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        resizeCircle(evt.getWheelRotation());
    }//GEN-LAST:event_formMouseWheelMoved

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
        if (evt.getPropertyName().equals("imageLocation")) {
            Point2D imageLocation = super.getImageLocation();
            BufferedImage image = getBufferedImage();
            if (imageLocation != null && image != null) {
                imageCenterX = (int) Math.floor(0.5d * (getWidth() + image.getWidth() - 2.0d * imageLocation.getX()));
                imageCenterY = (int) Math.floor(0.5d * (getHeight() + image.getHeight() - 2.0d * imageLocation.getY()));
//                System.out.println(new Point(imageCenterX, imageCenterY));
            }
        }
    }//GEN-LAST:event_formPropertyChange

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        repaint();
    }//GEN-LAST:event_formComponentShown

    public void resizeCircle(int value) {
        diameter -= value;
        repaint();
    }
    
    public void setCircle(int value) {
        diameter = value;
        repaint();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
