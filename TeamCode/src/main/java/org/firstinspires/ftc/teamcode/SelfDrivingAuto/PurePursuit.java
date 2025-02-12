package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

import java.util.Arrays;

public class PurePursuit {
    private final PID xPID, yPID, turnPID;
    private double[][] path; // {{x1, y1}, {x2, y2}, ...}
    private double[] turnPath; // {θ1, θ2, ...}
    private int intersectionIndex = 0;
    private double turnMultiplier = 1;

    public double currentSpeedForUnitTest = 0;

    public PurePursuit(double kP, double kI, double kD) {
        xPID = new PID(kP, kI, kD);
        yPID = new PID(kP, kI, kD);
        turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2]);
    }

    public PurePursuit(double kP, double kI, double kD, double initialError) {
        xPID = new PID(kP, kI, kD, initialError);
        yPID = new PID(kP, kI, kD, initialError);
        turnPID = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2], initialError);
    }

    public void setTargetPath(double[][] path) {
        this.path = path;
        // Reset turn path to all zeros
        turnPath = new double[path.length];
        for (int i = 0; i < path.length; i++) {
            turnPath[i] = 0;
        }
        // Reset turn multiplier
        this.turnMultiplier = 1;
    }

    /**
     * Must be the same length as the path
     * @param turnPath double[][] - {{t1, θ1}, {t2, θ2}, ...}
     */
    public void setTurnPath(double[][] turnPath) {
        // {{t1, θ1}, {t2, θ2}, ...} to {θ1, θ2, ...}
        double[] turnPathArray = new double[turnPath.length];
        for (int i = 0; i < turnPath.length; i++) {
            turnPathArray[i] = turnPath[i][1];
            Bot.dashboardTelemetry.addData("pointy " + i, turnPath[i][1]);
        }
        Bot.dashboardTelemetry.update();
        this.turnPath = turnPathArray;
    }

    /**
     * Must be the same length as path
     *
     * @param turnPath double[] - {θ1, θ2, ...}
     */
    public void setTurnPath(double[] turnPath) {
        this.turnPath = turnPath;
    }

    /**
     * Get the target point on the path based on the current position
     * @param lookAhead double - the distance to look ahead
     * @return double[] - the target point
     */
    public double[] getTargetPoint(double lookAhead, boolean useVirtualRobot) {
        // Get the current position
        double[] pos = Bot.pinpoint.getPosition();

        if (useVirtualRobot) {
            // Move the virtual robot ahead of the actual robot
            double[] velocity = Bot.pinpoint.getVelocity();
            double speed = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
            // Normalize the velocity vector
            double[] direction = {velocity[0] / speed, velocity[1] / speed};
            // Calculate the distance to move ahead
            // Based on current speed of the robot
            double distance = Config.overshootDistance(speed);
            // Move the virtual robot
            pos[0] += direction[0] * distance;
            pos[1] += direction[1] * distance;
        }

        // Loop through the path to find the target point
        double[] lastIntersection = null; // Follow the intersection point closest to the end of the path
        for (int i = 0; i < path.length - 1; i++) {
            double[] p1 = path[i];
            double[] p2 = path[i + 1];

            // Check for intersection between the line and the look ahead circle
            double[] intersection = getIntersection(pos, lookAhead, p1, p2);
            if (intersection != null) {
                // If there is an intersection, update the last intersection point
                lastIntersection = intersection;
                intersectionIndex = i;
            }
        }
        if (lastIntersection != null) return lastIntersection;
        return path[0]; // If no intersection is found, return the first point in the path
    }

    private double[] getIntersection(double[] pos, double lookAhead, double[] p1, double[] p2) {
        double x1 = p1[0];
        double y1 = p1[1];
        double x2 = p2[0];
        double y2 = p2[1];
        double currentX = pos[0];
        double currentY = pos[1];

        // If the end of the path is within the look ahead distance, return the end of the path
        if (Math.sqrt(Math.pow(x2 - currentX, 2) + Math.pow(y2 - currentY, 2)) <= lookAhead) {
            return p2;
        }

        // Algorithm from https://wiki.purduesigbots.com/software/control-algorithms/basic-pure-pursuit
        // Subtract currentX and currentY from [x1, y1] and [x2, y2] to offset the system to origin
        double x1_offset = x1 - currentX;
        double y1_offset = y1 - currentY;
        double x2_offset = x2 - currentX;
        double y2_offset = y2 - currentY;

        // Calculate the discriminant
        double dx = x2_offset - x1_offset;
        double dy = y2_offset - y1_offset;
        double dr = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double D = x1_offset * y2_offset - x2_offset * y1_offset;
        double discriminant = Math.pow(lookAhead, 2) * Math.pow(dr, 2) - Math.pow(D, 2);

        // If the discriminant is >= 0, there is an intersection
        if (discriminant >= 0) {
            // Calculate the intersection points
            double sol_x1 = (D * dy + Math.signum(dy) * dx * Math.sqrt(discriminant)) / Math.pow(dr, 2);
            double sol_x2 = (D * dy - Math.signum(dy) * dx * Math.sqrt(discriminant)) / Math.pow(dr, 2);
            double sol_y1 = (-D * dx + Math.abs(dy) * Math.sqrt(discriminant)) / Math.pow(dr, 2);
            double sol_y2 = (-D * dx - Math.abs(dy) * Math.sqrt(discriminant)) / Math.pow(dr, 2);

            // Offset the system back to the original position
            double[] intersection1 = {sol_x1 + currentX, sol_y1 + currentY};
            double[] intersection2 = {sol_x2 + currentX, sol_y2 + currentY};

            // Check if the intersection points are on the line segment
            double minX = Math.min(x1, x2);
            double maxX = Math.max(x1, x2);
            double minY = Math.min(y1, y2);
            double maxY = Math.max(y1, y2);

            boolean intersecting1 = minX <= intersection1[0] && intersection1[0] <= maxX && minY <= intersection1[1] && intersection1[1] <= maxY;
            boolean intersecting2 = minX <= intersection2[0] && intersection2[0] <= maxX && minY <= intersection2[1] && intersection2[1] <= maxY;

            // If both intersection points are on the line segment find which one is closer to the end of the path
            if (intersecting1 && intersecting2) {
                // Distance between the intersection points and the end of the path
                double distance1 = Math.sqrt(Math.pow(x2 - intersection1[0], 2) + Math.pow(y2 - intersection1[1], 2));
                double distance2 = Math.sqrt(Math.pow(x2 - intersection2[0], 2) + Math.pow(y2 - intersection2[1], 2));

                // Return the intersection point that is closer to the end of the path
                if (distance1 < distance2) return intersection1;
                else return intersection2;
            }
            // If only one intersection point is on the line segment return that point
            else if (intersecting1) return intersection1;
            else if (intersecting2) return intersection2;
        }
        // If there is no intersection return null
        return null;
    }

    public boolean reachedTarget(double tolerance, double turnTolerance) {
        double[] pos = Bot.pinpoint.getPosition();
        double[] targetPoint = path[path.length - 1]; // Last point in the path
        double turnTarget = turnPath[turnPath.length - 1];
        double dx = Math.abs(targetPoint[0] - pos[0]);
        double dy = Math.abs(targetPoint[1] - pos[1]);
        double dTheta = Math.abs(turnTarget - Bot.pinpoint.getHeading());
        return (Math.sqrt(dx*dx + dy*dy) < tolerance) && (dTheta < turnTolerance) ;
    }

    public boolean reachedTarget(double tolerance) {
        return reachedTarget(tolerance, Config.turnTolerance);
    }

    public boolean reachedTarget() {
        return reachedTarget(Config.driveTolerance, Config.turnTolerance);
    }

    public boolean reachedTargetXY(double xTolerance, double yTolerance) {
        double[] pos = Bot.pinpoint.getPosition();
        double[] targetPoint = path[path.length - 1]; // Last point in the path
        double dx = Math.abs(targetPoint[0] - pos[0]);
        double dy = Math.abs(targetPoint[1] - pos[1]);
        return (dx < xTolerance) && (dy < yTolerance);
    }

    public double getDistance() {
        // Get distance to target point
        double[] pos = Bot.pinpoint.getPosition();
        double[] targetPoint = path[path.length - 1]; // Last point in the path
        double dx = Math.abs(targetPoint[0] - pos[0]);
        double dy = Math.abs(targetPoint[1] - pos[1]);
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getDistanceX() {
        double[] pos = Bot.pinpoint.getPosition();
        double[] targetPoint = path[path.length - 1]; // Last point in the path
        double dx = Math.abs(targetPoint[0] - pos[0]);
        return Math.sqrt(dx);
    }

    public double getDistanceY() {
        double[] pos = Bot.pinpoint.getPosition();
        double[] targetPoint = path[path.length - 1]; // Last point in the path
        double dy = Math.abs(targetPoint[1] - pos[1]);
        return Math.sqrt(dy);
    }

    public void update(double speed, boolean useVirtualRobot) {
        // Get target point
        double[] targetPoint = getTargetPoint(calculateLookAhead(Config.lookAhead), useVirtualRobot);
        double turnTarget = turnPath == null ? -1 : turnPath[intersectionIndex];
        Bot.dashboardTelemetry.addData("Turn target", turnTarget);
        Bot.dashboardTelemetry.update();
        // Get current position and calculate the movement
        double[] pos = Bot.pinpoint.getPosition();
        double moveX = xPID.calculate(pos[0], targetPoint[0]);
        double moveY = yPID.calculate(pos[1], targetPoint[1]);
        double moveTheta = turnPID.calculate(Bot.pinpoint.getHeading(), turnTarget) * turnMultiplier;
//        Bot.opMode.telemetry.addData("Path", Arrays.deepToString(path));
//        Bot.opMode.telemetry.addData("PidX out", moveX);
//        Bot.opMode.telemetry.addData("PidY out", moveY);
//        Bot.opMode.telemetry.addData("pos x", pos[0]);
//        Bot.opMode.telemetry.addData("pos y", pos[1]);
//        Bot.opMode.telemetry.addData("target x", targetPoint[0]);
//        Bot.opMode.telemetry.addData("target y", targetPoint[1]);

        // save values for unit test
        currentSpeedForUnitTest = speed;

        // Move the robot
        Bot.mecanumBase.setNorthMode(true);
        Bot.mecanumBase.move(moveX, moveY, moveTheta, speed);
    }

    public void setTurnMultiplier(double turnMultiplier) {
        this.turnMultiplier = turnMultiplier;
    }

    public void update(double speed) {
        update(speed, false);
    }
    public void update() { update(Config.driveSpeed, false); }

    public double calculateLookAhead(double defaultLookAhead) {
        return defaultLookAhead;
    }

    public void stop() {
        Bot.mecanumBase.stop();
    }
}
