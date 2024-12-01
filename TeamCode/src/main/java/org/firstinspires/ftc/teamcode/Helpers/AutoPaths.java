package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class AutoPaths {

    double initHangX = Config.specHangX;
    double initHangY = Config.specHangY;
    // After grabbing specimen from fence, go to submersible

    public void hangSpecimenLinear(double currentX, double currentY, double offsetX, double offsetY) {
        double[] pointsX = {currentX, 0, -initHangX + offsetX};
        double[] pointsY = {currentY, 0, initHangY + offsetY};
        double[][] path = BezierCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
        Bot.specimenArm.movePrepHang(0.5);
    }
}
