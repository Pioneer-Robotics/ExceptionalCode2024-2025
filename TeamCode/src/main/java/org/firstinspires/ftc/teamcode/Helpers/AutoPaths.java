package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class AutoPaths {
    public static void hangSpecimen(double currentX, double currentY, double offsetX) {
        // Go to the submersible to hang a specimen
        Bot.purePursuit.setTargetPath(new double[][] {{currentX, currentY}, {200, 30}, {Config.specHangX + offsetX, Config.specHangY}});
    }

    public static void park(double currentX, double currentY) {
        // Park in the observation zone
        double[][] path = {{currentX, currentY}, {Config.parkX, Config.parkY}};
        Bot.purePursuit.setTargetPath(path);
    }

    public static void collectSpecimen(double currentX, double currentY) {
        // Collect a specimen from the wall
        double[] pointsX = {currentX, 315, 247.6, 282.5};
        double[] pointsY = {currentY, 65.5, 25.5, 22};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }

    public static void pushSample1(double currentX, double currentY) {
        // Push the first (farthest left) sample into the observation zone
        // Should start from the submersible
        double[] pointsX = {currentX, 225, 310, 260, 300, 285};
        double[] pointsY = {currentY, 6.5, 20.5, 325.5, 160.5, 42.5};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }

    public static void pushSample2(double currentX, double currentY) {
        // Push the second (middle) sample into the observation zone
        // Should start at the observation zone area
        double[] pointsX = {currentX, 260, 320, 320};
        double[] pointsY = {currentY, 150.5, 210.5, 45.5};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }
}