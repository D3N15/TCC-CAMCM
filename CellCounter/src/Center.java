/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cellcounter;

import java.awt.geom.Point2D;

/**
 *
 * @author Leandro
 */
public class Center extends Point2D.Double implements Comparable<Center> {
    
    private double intensity;

    public Center(double x, double y, double intensity) {
        this.x = x;
        this.y = y;
        this.intensity = intensity;
    }

    @Override
    public int compareTo(Center other) {
        if (this.intensity > other.intensity) {
            return -1;
        } else if (this.intensity < other.intensity) {
            return 1;
        } else {
            return 1;
        }
    }
}
