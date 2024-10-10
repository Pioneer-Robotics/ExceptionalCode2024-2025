package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Commands;
import org.firstinspires.ftc.teamcode.Helpers.Konami;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Helpers.BezierCalc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


@TeleOp(name="TestingTeleop", group="Teleop")
public class TestingTeleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle collectorToggle = new Toggle(false);

        // Initialize max speed
        double maxSpeed = 0.5;

        //Other
        boolean calmDown = false;

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        waitForStart();

        while(opModeIsActive()) {
            // ---- GamePad 1 ----
            // Inputs for driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.sqrt((px * px + py * py));

            // Move
            Bot.mecanumBase.move(stickAngle, -gamepad1.right_stick_x * maxSpeed, speed * maxSpeed);

            // Toggle for field centric
            northModeToggle.toggle(gamepad1.a); // Toggle north mode
            Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

            // Speed toggles
            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged()) {
                maxSpeed = Utils.increment(maxSpeed, 0.1, 1);
            }
            if (decSpeedToggle.justChanged()) {
                maxSpeed = Utils.decrement(maxSpeed, 0.1, 0.2);
            }

            // Pixel drop
            Bot.pixelDropLeft.selectBoolPos(Utils.floatToBool(gamepad1.left_trigger));
            Bot.pixelDropRight.selectBoolPos(Utils.floatToBool(gamepad1.right_trigger));

            // Collector
            collectorToggle.toggle(gamepad1.dpad_down);
            Bot.collector.setRunning(collectorToggle.get() && !gamepad1.dpad_up);

            if (gamepad1.dpad_left) {
                Bot.collector.up();
            }
            if (gamepad1.dpad_right) {
                Bot.collector.down();
            }
            if (gamepad1.dpad_up) {
                Bot.collector.reverse();
            }

            //Other
            if (gamepad1.left_stick_button || gamepad1.right_stick_button){
                calmDown = true;
            }

            if (gamepad1.x) {
                Bot.imu.resetYaw();
            }

            // ---- GamePad 2 ----
            // Slide motor
            if (gamepad2.dpad_right) {
                Commands.armMid();
            } else if (gamepad2.dpad_up) {
                Commands.armUp();
            } else if (gamepad2.dpad_down) {
                Commands.armDown();
            }

            // Slide servos
            if (gamepad2.b && Bot.slide.getPosition() > 0.2) {
                Bot.wrist.openServo();
            }
            if (gamepad2.a) {
                Bot.wrist.closeServo();
            }

            if (gamepad2.right_bumper && Bot.slide.getPosition() > 0.2) {
                Bot.gripper.openServo();
            }
            if (gamepad2.left_bumper) {
                Bot.gripper.closeServo();
            }

            // Get data for telemetry
            double[] pos = Bot.pose.getPose();

            double voltage = Bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                telemetry.addData("WARNING: Voltage Low", voltage);
                Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
            } else {
                Bot.led.lightsOff();
            }

            // Telemetry and update
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("X", pos[0]);
            telemetry.addData("Y", pos[1]);
            telemetry.addData("Theta", pos[2]);
            telemetry.addData("Collector Toggle", collectorToggle.get());
            telemetry.addData("Voltage", voltage);
            telemetry.update();

            dashboardTelemetry.addData("OTOS X", Bot.pose.getRawOTOS()[0]);
            dashboardTelemetry.addData("OTOS Y", Bot.pose.getRawOTOS()[1]);
            dashboardTelemetry.addData("OTOS Z", Bot.pose.getRawOTOS()[2]);
            dashboardTelemetry.addData("Odometry X", Bot.pose.getRawOdometer()[0]);
            dashboardTelemetry.addData("Odometry Y", Bot.pose.getRawOdometer()[1]);
            dashboardTelemetry.addData("Odometry Z", Bot.pose.getRawOdometer()[2]);
            dashboardTelemetry.addData("Final X", Bot.pose.getPose()[0]);
            dashboardTelemetry.addData("Final Y", Bot.pose.getPose()[1]);
            dashboardTelemetry.addData("Final Z", Bot.pose.getPose()[2]);
            dashboardTelemetry.update();
        }
    }
}
