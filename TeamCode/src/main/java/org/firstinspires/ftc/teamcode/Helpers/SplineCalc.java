package org.firstinspires.ftc.teamcode.Helpers;


public class SplineCalc {

    /**
     *
     * @param px Array of all x coordinates
     * @param py Array of all y coordinates
     * @param samples Number of samples to take
     * @return Array of points ([samples][2])
     */
    public static double[][] nDegBez(double[] px, double[] py, int samples) {
        double[][] points = new double[samples][2];
        int degree = px.length - 1;

        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            double pointX = 0;
            double pointY = 0;
            for (int j = 0; j <= degree; j++) {
                double calculation = Utils.choose(degree, j) * (Math.pow(1 - t, degree - j)) * (Math.pow(t, j));
                pointX += calculation * px[j];
                pointY += calculation * py[j];
            }
            points[i][0] = pointX;
            points[i][1] = pointY;
        }
        return (points);
    }

    /**
     *
     * @param p0 Point 0: array of x coord, y coord, x vector, y vector
     * @param p1 Point 1: array of x coord, y coord, x vector, y vector
     * @param samples Number of samples to take
     * @return Array of points ([samples][2])
     */
    public static double[][] cubicHermite(double[] p0, double[] p1, int samples) {
        double[][] points = new double[samples][2];

        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            double pointX = (2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1) * p0[0] +
                    (Math.pow(t, 3) - 2*Math.pow(t, 2) + t) * p0[2] +
                    (-2*Math.pow(t, 3) + 3*Math.pow(t, 2)) * p1[0] +
                    (Math.pow(t, 3) - Math.pow(t, 2)) * p1[2];

            double pointY = (2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1) * p0[1] +
                    (Math.pow(t, 3) - 2*Math.pow(t, 2) + t) * p0[3] +
                    (-2*Math.pow(t, 3) + 3*Math.pow(t, 2)) * p1[1] +
                    (Math.pow(t, 3) - Math.pow(t, 2)) * p1[3];

            points[i][0] = pointX;
            points[i][1] = pointY;
        }
        return(points);
    }

    /**
     * Linear interpolation between sets of points
     *
     * @param p0      list of x (or t) points
     * @param p1      list of y (or θ) points
     * @param samples number of samples to take
     *                (for turn paths it must be the same as the x y path)
     * @return Array of points ([samples][2])
     */
    public static double[][] linearPath(double[] p0, double[] p1, int samples) {
        double[][] points = new double[samples][2];
        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            // Find set of points that t is between
            int index = 0;
            while (t > p0[index + 1] && index < p0.length - 1) {
                index++;
            }
            // Get fraction between points based on t
            double fraction = (t - p0[index]) / (p0[index + 1] - p0[index]);
            // Interpolate between points
            points[i][0] = Utils.lerp(p0[index], p0[index + 1], fraction);
            points[i][1] = Utils.lerp(p1[index], p1[index + 1], fraction);
        }
        points[1] = points[0];
        points[points.length-2] = points[points.length-1];
        return points;
    }
}
