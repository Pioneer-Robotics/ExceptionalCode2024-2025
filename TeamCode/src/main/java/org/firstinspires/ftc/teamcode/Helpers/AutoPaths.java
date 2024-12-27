package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class AutoPaths {
    public static void hangSpecimen(double currentX, double currentY, double offsetX, double offsetY) {
        // Go to the submersible to hang a specimen
        Bot.purePursuit.setTargetPath(new double[][] {{currentX, currentY}, {200, 30}, {Config.specHangX + offsetX, Config.specHangY + offsetY}});
    }

    public static void park(double currentX, double currentY) {
        // Park in the observation zone
        double[][] path = {{currentX, currentY}, {Config.parkX, Config.parkY}};
        Bot.purePursuit.setTargetPath(path);
    }

    public static void collectSpecimen(double currentX, double currentY, boolean fromSubmersible) {
        // Collect a specimen from the wall
        double[] pointsX, pointsY;
        if (fromSubmersible) {
            pointsX = new double[] {currentX, 270, 280, 280};
            pointsY = new double[] {currentY, 35, 100, 20};
        } else {
            pointsX = new double[] {currentX, 280, 280};
            pointsY = new double[] {currentY, 100, 20};
        }
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }

    public static void pushSample1(double currentX, double currentY) {
        // Push the first (farthest left) sample into the observation zone
        // Should start from the submersible
        double[] pointsX = {currentX, 230, 305, 275, 305, 285};
        double[] pointsY = {currentY, 7.5, 35, 320, 145, 42.5};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }

    public static void pushSample2(double currentX, double currentY) {
        // Push the second (middle) sample into the observation zone
        // Should start at the observation zone area
        double[] pointsX = {currentX, 265, 330, 322};
        double[] pointsY = {currentY, 135.5, 200, 45.5};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }
}