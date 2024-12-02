package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

@Autonomous(name="Pure Pursuit Tuning", group="Calibration")
public class TurnPIDTuning extends LinearOpMode {
    // Uses FTC Dashboard to display robot position and target point
    public void runOpMode() {
        Bot.init(this);
        Bot.purePursuit.setTargetPath(new double[][] {{0,0}});

        waitForStart();

        while(opModeIsActive()) {
            Bot.purePursuit.update();
            telemetry.addData("Angle", Bot.imu.getRadians());
            telemetry.addData("X", Bot.optical_odom.getX());
            telemetry.addData("Y", Bot.optical_odom.getY());
            telemetry.addData("Actual X", Bot.deadwheel_odom.getX());
            telemetry.addData("Actual Y", Bot.deadwheel_odom.getY());
            telemetry.update();
        }
    }
}
