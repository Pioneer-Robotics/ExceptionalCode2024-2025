package org.firstinspires.ftc.teamcode.Helpers;

import org.opencv.core.Mat;

public class TrueAngle {
    double currentAngle ;
    double prevAngle;

    public TrueAngle(double initAngle) {
        this.currentAngle = initAngle;
        this.prevAngle = initAngle;
    }

    public double updateAngle(double inputAngle) {
        double deltaAngle = inputAngle - prevAngle;
        if (deltaAngle > Math.PI) {
            deltaAngle -= 2 * Math.PI;
        }
        else if (deltaAngle < -Math.PI) {
            deltaAngle += 2 * Math.PI;
        }
        currentAngle += deltaAngle;
        prevAngle = currentAngle;
        return currentAngle;
    }
}
