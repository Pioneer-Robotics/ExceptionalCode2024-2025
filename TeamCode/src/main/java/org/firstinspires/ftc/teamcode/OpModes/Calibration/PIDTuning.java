package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Utils;
@Disabled
@Autonomous(name="PID Tuning", group="Calibration")
public class PIDTuning extends LinearOpMode {
    // Uses FTC Dashboard to display robot position and target point
    enum State {
        DRIVE_FORWARD,
        DRIVE_BACKWARD
    }
    State state = State.DRIVE_FORWARD;
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();

        Bot.init(this);

        waitForStart();

        while(opModeIsActive()) {
            TelemetryPacket packet = new TelemetryPacket(false);
            double[][] path = new double[][] {};
            switch (state) {
                case DRIVE_FORWARD:
                    path = new double[][]{{0, 0}, {0, 100}};
                    Bot.purePursuit.setTargetPath(path);
                    if (Bot.purePursuit.reachedTarget()) {
                        state = State.DRIVE_BACKWARD;
                    }
                    break;
                case DRIVE_BACKWARD:
                    path = new double[][]{{0, 100}, {0, 0}};
                    Bot.purePursuit.setTargetPath(path);
                    if (Bot.purePursuit.reachedTarget()) {
                        state = State.DRIVE_FORWARD;
                    }
                    break;
            }
            Bot.purePursuit.update(Config.driveSpeed);

            double[] pos = Bot.optical_odom.getPose();
            double[] target = Bot.purePursuit.getTargetPoint(Config.lookAhead, false);

            // Draw robot position, target point, and path
            double inchesPerCentimeter = 0.394;
            packet.fieldOverlay().
                    setScale(inchesPerCentimeter, inchesPerCentimeter).
                    setStrokeWidth(3).
                    strokePolyline(Utils.pathToXY(path)[1], Utils.pathToXY(path)[0]).
                    setStroke("#0000FF").
                    strokeCircle(pos[1], pos[0], 10).
                    setStroke("#00FF00").
                    strokeCircle(target[1], target[0], 10);

            // Add telemetry data
            packet.put("robot x", pos[0]);
            packet.put("robot y", pos[1]);

            packet.put("target x", target[0]);
            packet.put("target y", target[1]);

            packet.put("error x", Math.sqrt(Math.pow(target[0]-pos[0],2) + Math.pow(target[1]-pos[1],2)));

            dashboard.sendTelemetryPacket(packet);
        }
    }
}
