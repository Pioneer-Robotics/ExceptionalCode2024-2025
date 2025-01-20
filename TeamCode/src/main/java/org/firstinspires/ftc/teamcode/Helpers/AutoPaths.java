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
            pointsX = new double[]{currentX, 270, 280, 282.5};
            pointsY = new double[]{currentY, 35, 100, 21.35};
        } else {
            pointsX = new double[]{currentX, 250, 282.5};
            pointsY = new double[]{currentY, 100, 21.35};
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
        // Turn between t=0.2 and t=0.4
        double[][] turnPath = SplineCalc.linearPath(new double[]{0, 0.2, 1}, new double[]{0, Math.PI / 2, Math.PI / 2}, 25);
        Bot.purePursuit.setTurnPath(turnPath);
    }

    public static void pushSample2(double currentX, double currentY) {
        // Push the second (middle) sample into the observation zone
        // Should start at the observation zone area
        double[] pointsX = {currentX, 255, 317.5, 312.5};
        double[] pointsY = {currentY, 135, 200, 60};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
        double[][] turnPath = SplineCalc.linearPath(new double[]{0, 1}, new double[]{Math.PI / 2, Math.PI / 2}, 25);
        Bot.purePursuit.setTurnPath(turnPath);
    }

    public static void pushSample3(double currentX, double currentY) {
        // Push the third (right) sample into the observation zone
        // Should start at the observation zone area
        double[] pointsX = {currentX, 275, 335, 333};
        double[] pointsY = {currentY, 135, 200, 55};
        double[][] path = SplineCalc.nDegBez(pointsX, pointsY, 25);
        Bot.purePursuit.setTargetPath(path);
        double[][] turnPath = SplineCalc.linearPath(new double[]{0, 1}, new double[]{Math.PI / 2, Math.PI / 2}, 25);
        Bot.purePursuit.setTurnPath(turnPath);
    }
}
