package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Polynomial {
    private final double[] coeffs;

    public Polynomial(double[] coeffs) {
        this.coeffs = coeffs;
    }

    public double eval(double x) {
        return eval(x, coeffs);
    }

    public double eval(double x, double[] coefficients) {
        double value = 0;
        for (int i = coefficients.length-1; i >= 0; --i) {
            value = coefficients[i] + (x*value);
        }
        return value;
    }

    public double[] derivative() {
        double[] der = new double[coeffs.length - 1];
        for (int i = 1; i < coeffs.length; i++) {
            der[i - 1] = coeffs[i] * i;
        }
        return der;
    }

    public double[] antiDerivative() {
        double[] antiDer = new double[coeffs.length + 1];
        for (int i = 0; i < coeffs.length; i++) {
            antiDer[i + 1] = coeffs[i] / (i + 1);
        }
        return antiDer;
    }

    public double derEval(double x) {
        double[] der = derivative();
        return eval(x, der);
    }

    public double finiteInt(double x1, double x2) {
        double[] antiDer = antiDerivative();
        return (eval(x2, antiDer) - eval(x1, antiDer));
    }

    // Getters
    public double[] getCoeffs() {
        return coeffs;
    }
}
