package org.firstinspires.ftc.teamcode.OpModes.Testing;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

        /*
        color_dict_HSV = {'black': [[180, 255, 30], [0, 0, 0]],
              'white': [[180, 18, 255], [0, 0, 231]],
              'red1': [[180, 255, 255], [159, 50, 70]],
              'red2': [[9, 255, 255], [0, 50, 70]],
              'green': [[89, 255, 255], [36, 50, 70]],
              'blue': [[128, 255, 255], [90, 50, 70]],
              'yellow': [[35, 255, 255], [25, 50, 70]],
              'purple': [[158, 255, 255], [129, 50, 70]],
              'orange': [[24, 255, 255], [10, 50, 70]],
              'gray': [[180, 18, 230], [0, 0, 40]]}
         */

public class BlockDetector extends OpenCvPipeline {
    Telemetry telemetry;
    TeamColor teamColor;

    Mat mat = new Mat();
    private boolean isRedFound = false;
    private boolean isBlueFound = false;
    private boolean isYellowFound = false;

    private double redAngle = 0;
    private double blueAngle = 0;
    private double yellowAngle = 0;

    private double notFoundReturnValue = 1000;
    public enum TeamColor {
        BLUE,
        RED
    }

    static double PERCENT_COLOR_THRESHOLD = 0.4;
    static final Rect SMALLER_AREA_OF_IMAGE = new Rect( // 320/240 total image - adjust as necessary
            new Point(100, 35),
            new Point(160, 75));

    public BlockDetector(Telemetry t, TeamColor teamColor) {
        telemetry = t;
        this.teamColor = teamColor;
    }

    @Override
    public Mat processFrame(Mat _input) {
        Mat input = _input;

        boolean useSmallerArea = false;
        if (useSmallerArea) { // may need a smaller area of the image
            input = input.submat(SMALLER_AREA_OF_IMAGE);
        }

        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV); // RGB

        switch (teamColor) {
            case BLUE:
                processBlue(input);
                break;
            case RED:
                processRed(input);
                break;
        };
        processYellow(input);

        telemetry.update();

        // Cleanup
        input.release();

        return mat;
    }

    // Private Helpers -
    private void processYellow(Mat input) {
        Mat hsv = new Mat();

        Mat yellowMask = new Mat();
        Scalar lowerYellow = new Scalar(20, 100, 100);
        Scalar upperYellow = new Scalar(30, 255, 255);
//        Scalar lowerYellow = new Scalar(25, 50, 70);
//        Scalar upperYellow = new Scalar(35, 255, 255);
        Core.inRange(hsv, lowerYellow, upperYellow, yellowMask);

        // look for percentage of the color
        double rawYellowValue = Core.sumElems(input).val[0];
        double yellowValue = rawYellowValue / 255;
        telemetry.addData("yellow raw value", (int) rawYellowValue);
        telemetry.addData("yellow percentage", Math.round(yellowValue * 100) + "%");
        isYellowFound = yellowValue > PERCENT_COLOR_THRESHOLD;
        telemetry.addData("isYellowFound: ", isYellowFound);

        // Find Angle
        double _yellowAngle = findRectangleAngle(input, yellowMask, new Scalar(0, 255, 255));
        if (_yellowAngle == notFoundReturnValue) {
            isYellowFound = false;
            telemetry.addData("yellow Angle NOT FOUND: ", isYellowFound);
        } else {
            yellowAngle = _yellowAngle;
            telemetry.addData("yellowAngle: ", yellowAngle);
        }

        // Cleanup
        hsv.release();
        yellowMask.release();
    }

    private  void processBlue(Mat input) {
        Mat hsv = new Mat();

        Mat blueMask = new Mat();
        Scalar lowerBlue = new Scalar(100, 100, 100);
        Scalar upperBlue = new Scalar(130, 255, 255);
//        Scalar lowerBlue = new Scalar(90, 50, 70);
//        Scalar upperBlue = new Scalar(128, 255, 255);
        Core.inRange(hsv, lowerBlue, upperBlue, blueMask);

        // look for percentage of the color
        double rawBlueValue = Core.sumElems(input).val[0];
        double blueValue = rawBlueValue / 255;
        telemetry.addData("Blue raw value", (int) rawBlueValue);
        telemetry.addData("Blue percentage", Math.round(blueValue * 100) + "%");
        isBlueFound = blueValue > PERCENT_COLOR_THRESHOLD;
        telemetry.addData("isBlueFound: ", isBlueFound);

        // Find Angle
        double _blueAngle = findRectangleAngle(input, blueMask, new Scalar(0, 0, 255));
        if (_blueAngle == notFoundReturnValue) {
            isBlueFound = false;
            telemetry.addData("blue Angle NOT FOUND: ", isBlueFound);
        } else {
            blueAngle = _blueAngle;
            telemetry.addData("blueAngle: ", blueAngle);
        }

        // Cleanup
        hsv.release();
        blueMask.release();
    }


    private void processRed(Mat input) {
        Mat hsv = new Mat();

        // Find Red
        // Define red color ranges (red often wraps around 180 in HSV)
        Scalar lowerRed1 = new Scalar(0, 100, 100);
        Scalar upperRed1 = new Scalar(10, 255, 255);
        Scalar lowerRed2 = new Scalar(170, 100, 100);
        Scalar upperRed2 = new Scalar(180, 255, 255);
//        Scalar lowRed1HSV = new Scalar(155, 50, 70);
//        Scalar highRed1HSV = new Scalar(180, 255, 255);
//        Scalar lowRed2HSV = new Scalar(0, 50, 70);
//        Scalar highRed2HSV = new Scalar(9, 255, 255);

        Mat mask1 = new Mat();
        Mat mask2 = new Mat();
        Core.inRange(hsv, lowerRed1, upperRed1, mask1);
        Core.inRange(hsv, lowerRed2, upperRed2, mask2);

        // Combine the two masks
        Mat redMask = new Mat();
        Core.addWeighted(mask1, 1.0, mask2, 1.0, 0.0, redMask);

        double rawRedValue = Core.sumElems(redMask).val[0];
        double redValue = rawRedValue / 255;
        telemetry.addData("Red raw value", (int) rawRedValue);
        telemetry.addData("Red percentage", Math.round(redValue * 100) + "%");
        boolean redFound = redValue > PERCENT_COLOR_THRESHOLD;
        telemetry.addData("redFound: ", redFound);

        // Find Angle
        double _redAngle = findRectangleAngle(input, redMask, new Scalar(0, 255, 0));
        if (_redAngle == notFoundReturnValue) {
            isRedFound = false;
            telemetry.addData("red Angle NOT FOUND: ", redAngle);
        } else {
            redAngle = _redAngle;
            telemetry.addData("redAngle: ", redAngle);
        }

        // Cleanup
        hsv.release();
        mask1.release();
        mask2.release();
        redMask.release();
    }

    private double findRectangleAngle(Mat input, Mat mask, Scalar color) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        double largestArea = 0;
        RotatedRect largestRect = null;

        for (MatOfPoint contour : contours) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());
            RotatedRect rect = Imgproc.minAreaRect(contour2f);
            double area = rect.size.area();
            if (area > largestArea) {
                largestArea = area;
                largestRect = rect;
            }
            contour2f.release();
        }

        if (largestRect != null) {
            double angle = largestRect.angle;
            // Adjust angle for rectangles that are more vertical
            if (largestRect.size.width < largestRect.size.height) {
                angle += 90;
            }

            // Draw rectangle and angle
            Point[] vertices = new Point[4];
            largestRect.points(vertices);
            for (int i = 0; i < 4; i++) {
                Imgproc.line(input, vertices[i], vertices[(i+1)%4], color, 2);
            }
            Imgproc.putText(input, String.format("Angle: %.1f", angle), vertices[0], Imgproc.FONT_HERSHEY_SIMPLEX, 0.6, color, 2);

            // Cleanup
            hierarchy.release();

            return angle;
        }

        // Cleanup
        hierarchy.release();

        return notFoundReturnValue;
    }

    // Public API -
    public boolean isNoneFound() {
        return !isRedFound && !isBlueFound && !isYellowFound;
    }
    public boolean isTeamColorFound() {
        switch (teamColor) {
            case BLUE: return isBlueFound;
            case RED: return isRedFound;
        };
        return false;
    }
    public boolean isYellowFound() {
        return isYellowFound;
    }

    public double getTeamColorAngle() {
        switch (teamColor) {
            case BLUE: return blueAngle;
            case RED: return redAngle;
        };
        return redAngle; // default to red
    }

    public double getYellowAngle() {
        return yellowAngle;
    }

}