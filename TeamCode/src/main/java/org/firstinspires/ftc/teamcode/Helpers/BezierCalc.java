package org.firstinspires.ftc.teamcode.Helpers;



public class BezierCalc {

    public double[][] linearBez(double[] p0, double[] p1, int samples) {
        double[][] points = new double[samples][2];
        for(int i=0;i<samples;i++) {
            double t = (double) i /(samples-1);
            points[i][0] = (1-t)*p0[0] + t*p1[0];
            points[i][1] = (1-t)*p0[1] + t*p1[1];
        }
        return(points);
    }

    public double[][] quadBez(double[] p0, double[] p1, double[] p2, int samples) {
        double[][] points = new double[samples][2];
        for(int i=0;i<samples;i++) {
            double t = (double) i /(samples-1);
            points[i][0] = Math.pow((1-t),2)*p0[0] + 2*(1-t)*t*p1[0] + Math.pow(t,2)*p2[0];
            points[i][1] = Math.pow((1-t),2)*p0[1] + 2*(1-t)*t*p1[1] + Math.pow(t,2)*p2[1];
        }
        return(points);
    }
    public double[][] cubicBez(double[] p0, double[] p1, double[] p2, double[] p3, int samples) {
        double[][] points = new double[samples][2];
        for(int i=0;i<samples;i++) {
            double t = (double) i /(samples-1);
            points[i][0] = Math.pow((1-t),3)*p0[0] + 3*Math.pow((1-t),2)*t*p1[0] + 3*(1-t)*Math.pow(t,2)*p2[0] + Math.pow(t,3)*p3[0];
            points[i][1] = Math.pow((1-t),3)*p0[1] + 3*Math.pow((1-t),2)*t*p1[1] + 3*(1-t)*Math.pow(t,2)*p2[1] + Math.pow(t,3)*p3[1];
        }
        return(points);
    }

    public double[][] nDegBez(double[] px, double[] py, int samples) throws Exception {
        if (px.length != py.length) {
            throw new Exception("X coords do not math Y coords");
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
