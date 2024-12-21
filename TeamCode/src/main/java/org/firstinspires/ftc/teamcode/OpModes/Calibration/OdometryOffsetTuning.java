package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

@Autonomous(name = "Odometry Tuning", group = "Calibration")
public class OdometryOffsetTuning extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        waitForStart();
        while (opModeIsActive()) {
            // Inputs for driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            // Move
            Bot.mecanumBase.move(px, py, turn, 0.5);

            telemetry.addData("Pos Theta", Bot.imu.getRadians());
            telemetry.addData("Odo Left Raw", Bot.deadwheel_odom.getRawOdoLeft());
            telemetry.addData("Odo Center Raw", Bot.deadwheel_odom.getRawOdoCenter());
            telemetry.update();
        }
    }
}
