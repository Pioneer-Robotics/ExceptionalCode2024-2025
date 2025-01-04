package org.firstinspires.ftc.teamcode.Helpers;

import org.opencv.core.Mat;

public class TrueAngle {
    double currentAngle ;
    double prevAngle;

    public TrueAngle(double initAngle) {
        this.currentAngle = initAngle;
        this.prevAngle = initAngle;
    }

    public double updateAngle(double inputAngle, double angleThres, double angleCorrect) {
        double deltaAngle = inputAngle - prevAngle;
        if (deltaAngle > angleThres) {
            deltaAngle -= angleCorrect; //was 2pi
        }
        else if (deltaAngle < -angleThres) {
            deltaAngle += angleCorrect;
        }
        currentAngle += deltaAngle;
        prevAngle = inputAngle;
        return currentAngle;
    }
}
