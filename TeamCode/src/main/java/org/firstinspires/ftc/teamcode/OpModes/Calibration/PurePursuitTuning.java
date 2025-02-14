package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.SplineCalc;
import org.firstinspires.ftc.teamcode.Helpers.Utils;
@Disabled
@Autonomous(name="Pure Pursuit Tuning", group="Calibration")
public class PurePursuitTuning extends LinearOpMode {
    // Uses FTC Dashboard to display robot position and target point
    enum State {
        DRIVE_FORWARD, // follow a path forward
        DRIVE_BACKWARD // follow a path backward
    }
    State state = State.DRIVE_FORWARD;
    boolean useVirtualRobot = true;

    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();

        Bot.init(this);

        waitForStart();

        while(opModeIsActive()) {
            TelemetryPacket packet = new TelemetryPacket(false);
            double[][] path = new double[][] {};
            double[][] turnPath = new double[][]{};
            switch (state) {
                case DRIVE_FORWARD:
                    double[] px = {0, 0, 100, 100};
                    double[] py = {0, 100, 0, 100};
                    path = SplineCalc.nDegBez(px, py, 25);
                    Bot.purePursuit.setTargetPath(path);
                    turnPath = SplineCalc.linearPath(new double[]{0, 0.25, 0.75, 1}, new double[]{0, Math.PI / 2, 0, 0}, 25);
                    Bot.purePursuit.setTurnPath(turnPath);
                    if (Bot.purePursuit.reachedTarget()) {
                        state = State.DRIVE_BACKWARD;
                    }
                    break;
                case DRIVE_BACKWARD:
                    double[] px2 = {100, 100, 0, 0};
                    double[] py2 = {100, 0, 100, 0};
                    path = SplineCalc  .nDegBez(px2, py2, 25);
                    Bot.purePursuit.setTargetPath(path);
                    turnPath = SplineCalc.linearPath(new double[]{0, 0.25, 0.75, 1}, new double[]{0, -Math.PI / 2, 0, 0}, 25);
                    Bot.purePursuit.setTurnPath(turnPath);
                    if (Bot.purePursuit.reachedTarget()) {
                        state = State.DRIVE_FORWARD;
                    }
                    break;
            }

            double[] pos = Bot.pinpoint.getPosition();
            double[] pos_adjusted = Bot.pinpoint.getPosition();

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
                pos_adjusted[0] += direction[0] * distance;
                pos_adjusted[1] += direction[1] * distance;
            }

            double[] target = Bot.purePursuit.getTargetPoint(Config.lookAhead, useVirtualRobot);
            Bot.purePursuit.update(Config.driveSpeed, useVirtualRobot);

            Bot.dashboardTelemetry.update();

            // Draw robot position, target point, and path
            double inchesPerCentimeter = 0.394;
            packet.fieldOverlay().
                    setScale(inchesPerCentimeter, inchesPerCentimeter).
                    setStroke("#FFFFFF").
                    setStrokeWidth(3).
                    strokePolyline(Utils.pathToXY(path)[1], Utils.pathToXY(path)[0]).
                    setStroke("#0000FF").
                    strokeCircle(pos[1], pos[0], 10).
                    setStroke("#0000FF").
                    strokeCircle(pos_adjusted[1], pos_adjusted[0], 10).
                    setStroke("#00FF00").
                    strokeCircle(target[1], target[0], 10);

            // Add telemetry data
            packet.put("speed", Math.sqrt(Math.pow(Bot.pinpoint.getVelocity()[0], 2) + Math.pow(Bot.pinpoint.getVelocity()[1], 2)));

            packet.put("robot x", pos[0]);
            packet.put("robot y", pos[1]);

            packet.put("target x", target[0]);
            packet.put("target y", target[1]);

            packet.put("error x", Math.sqrt(Math.pow(target[0] - pos[0], 2) + Math.pow(target[1] - pos[1], 2)));

            dashboard.sendTelemetryPacket(packet);
        }
    }
}
