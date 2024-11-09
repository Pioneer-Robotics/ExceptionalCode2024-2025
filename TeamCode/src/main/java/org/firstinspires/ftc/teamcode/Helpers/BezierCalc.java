package org.firstinspires.ftc.teamcode.Helpers;



public class BezierCalc {

    public static double[][] nDegBez(double[] px, double[] py, int samples) throws Exception {
        if (px.length != py.length) {
            throw new Exception("X coords do not match Y coords");
        }
        double[][] points = new double[samples][2];
        int degree = px.length - 1;

        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            double pointX = 0;
            double pointY = 0;
            for (int j = 0; j <= degree; j++) {
                pointX += Utils.choose(degree, j) * (Math.pow(1 - t, degree - j)) * (Math.pow(t, j)) * px[j];
                pointY += Utils.choose(degree, j) * (Math.pow(1 - t, degree - j)) * (Math.pow(t, j)) * py[j];
            }
            points[i][0] = pointX;
            points[i][1] = pointY;
        }
        return (points);
    }


}
