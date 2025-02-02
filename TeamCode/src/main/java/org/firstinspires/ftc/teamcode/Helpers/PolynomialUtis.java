package org.firstinspires.ftc.teamcode.Helpers;

public class PolynomialUtis {
    public static double[] sumCoeffs(double[] poly1, double[] poly2) {
        int lenMax = Math.max(poly1.length, poly2.length);
        int lenMin = Math.min(poly1.length, poly2.length);
        double[] total = new double[lenMax];
        for (int i = 0; i < lenMin; i++) {
            total[i] = poly1[i] + poly2[i];
        }
        for (int i = lenMin; i < lenMax; i++) {
            if (poly1.length > poly2.length) {
                total[i] = poly1[i];
            } else {
                total[1] = poly2.length;
            }
        }
        return(total);
    }
}
