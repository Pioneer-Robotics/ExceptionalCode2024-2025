package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class AutoPaths {
    public static void hangSpecimen(double currentX, double currentY, double offsetX, double offsetY) {
        // Go to the submersible to hang a specimen
        Bot.purePursuit.setTargetPath(new double[][] {{currentX, currentY}, {225, 35}, {Config.specHangX + offsetX, Config.specHangY + offsetY}});
    }

    public static void hangSpecimenUp(double currentX, double currentY, double offsetX, double offsetY) {
        // Go to the submersible to hang a specimen
        Bot.purePursuit.setTargetPath(new double[][] {{currentX, currentY}, {200, 30}, {Config.specHangX + offsetX, Config.specHangY + offsetY}});
    }

    public static void park(double currentX, double currentY) {
        // Park in the observation zone
        double[] pointsX = {currentX, 230, Config.parkX};
        double[] pointsY = {currentY, 40, Config.parkY};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }

    public static void collectSpecimen(double currentX, double currentY, boolean fromSubmersible) {
        // Collect a specimen from the wall
        double[] pointsX, pointsY;
        if (fromSubmersible) {
            pointsX = new double[] {currentX, 270, 280, 280};
            pointsY = new double[]{currentY, 35, 100, 21.75};
        } else {
            pointsX = new double[]{currentX, 265, 280};
            pointsY = new double[]{currentY, 100, 21.75};
        }
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }

    public static void pushSample1(double currentX, double currentY) {
        // Push the first (farthest left) sample into the observation zone
        // Should start from the submersible
        double[] pointsX = {currentX, 220, 275, 275, 300, 285};
        double[] pointsY = {currentY, 7.5, 50, 280, 145, 60};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
        Bot.purePursuit.setTurnPath(new double[][]{{0, 0}, {0.2, 0}, {1, Math.PI / 2}});
    }

    public static void pushSample2(double currentX, double currentY) {
        // Push the second (middle) sample into the observation zone
        // Should start at the observation zone area
        double[] pointsX = {currentX, 255, 315, 310};
        double[] pointsY = {currentY, 135, 200, 60};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
        Bot.purePursuit.setTurnPath(new double[][]{{0, Math.PI / 2}, {1, Math.PI / 2}});
    }

    public static void pushSample3(double currentX, double currentY) {
        // Push the second (middle) sample into the observation zone
        // Should start at the observation zone area
        double[] pointsX = {currentX, 275, 330, 330};
        double[] pointsY = {currentY, 135, 200, 55};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
    }
}
