/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cellcounter;

/**
 *
 * @author Leandro
 */
public class SobelUtilities {
    
    private static final byte[][] MASK_3x3 = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };

    private static final byte[][] MASK_5x5 = {
        {-2, -1,  0,  1,  2},
        {-3, -2,  0,  2,  3},
        {-4, -3,  0,  3,  4},
        {-3, -2,  0,  2,  3},
        {-2, -1,  0,  1,  2}
    };

    private static final byte[][] MASK_7x7 = {
        {-3, -2, -1,  0,  1,  2,  3},
        {-4, -3, -2,  0,  2,  3,  4},
        {-5, -4, -3,  0,  3,  4,  5},
        {-6, -5, -4,  0,  4,  5,  6},
        {-5, -4, -3,  0,  3,  4,  5},
        {-4, -3, -2,  0,  2,  3,  4},
        {-3, -2, -1,  0,  1,  2,  3},
    };

    private static final byte[][] MASK_9x9 = {
        {-4, -3, -2, -1,  0,  1,  2,  3,  4},
        {-5, -4, -3, -2,  0,  2,  3,  4,  5},
        {-6, -5, -4, -3,  0,  3,  4,  5,  6},
        {-7, -6, -5, -4,  0,  4,  5,  6,  7},
        {-8, -7, -6, -5,  0,  5,  6,  7,  8},
        {-7, -6, -5, -4,  0,  4,  5,  6,  7},
        {-6, -5, -4, -3,  0,  3,  4,  5,  6},
        {-5, -4, -3, -2,  0,  2,  3,  4,  5},
        {-4, -3, -2, -1,  0,  1,  2,  3,  4}
    };
    
    private static byte[][] getMask(int size) throws Exception {
        switch (size) {
            case 3: return MASK_3x3;
            case 5: return MASK_5x5;
            case 7: return MASK_7x7;
            case 9: return MASK_9x9;
            default: throw new Exception("Mask not defined.");
        }
    }
    
    public static double convolve(
            double[][] image,
            int x,
            int y,
            int maskSize,
            boolean rotate
            ) throws Exception {
        
        byte[][] mask = getMask(maskSize);
        int m = maskSize / 2;
        
        int kLimit = image.length;
        int lLimit = image[0].length;
        
        int iLimit = mask.length;
        int jLimit = mask[0].length;
        
        double itensity = 0;
        
        for (int j = 0; j < iLimit; j++) {
            for (int i = 0; i < jLimit; i++) {
                int k = x + i - m;
                int l = y + j - m;
                if (k >= 0 && k < kLimit && l >= 0 && l < lLimit) {
                    double color = image[k][l];
                    byte weight = rotate ? mask[j][i] : mask[i][j];
                    itensity += weight * color;
                }
            }
        }
        
        switch (maskSize) {
            case 3: return itensity / 4.0d;
            case 5: return itensity / 23.0d;
            case 7: return itensity / 69.0d;
            case 9: return itensity / 154.0d;
            default: return itensity;
        }
    }
}
