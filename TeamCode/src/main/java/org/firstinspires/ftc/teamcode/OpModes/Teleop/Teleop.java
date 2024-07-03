package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

@TeleOp(name="Teleop", group="Teleop")
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        // Bot object
        Bot bot = new Bot(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);

        // Initialize max speed
        double maxSpeed = 0.5;

        waitForStart();

        while(opModeIsActive()) {
            // Inputs for driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double stickAngle = Math.atan2(py, px);
            double speed = Math.sqrt((px * px + py * py));

            // Move
            bot.mecanumBase.move(stickAngle, -gamepad1.right_stick_x*maxSpeed, speed*maxSpeed);

            // Toggle for field centric
            northModeToggle.toggle(gamepad1.a); // Toggle north mode
            bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

            // Speed toggles
            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged() && maxSpeed < 1) {
                maxSpeed += 0.1;
            }
            if (decSpeedToggle.justChanged() && maxSpeed > 0.2) {
                maxSpeed -= 0.1;
            }
            maxSpeed = (double) Math.round(maxSpeed * 10) / 10; // Account for floating point error

            // Slide motor
            if (gamepad1.x) {
                bot.slide.moveToPosition(0.5);
                bot.gripper.closeServo();
            } else if (gamepad1.y) {
                bot.slide.moveToPosition(1);
                bot.gripper.closeServo();
            } else if (gamepad1.b) {
                bot.slide.moveToPosition(0);
                bot.wrist.closeServo();
                bot.gripper.openServo();
            }

            // Servos
            bot.pixelDropLeft.selectBoolPos(Utils.floatToBool(gamepad1.left_trigger));
            bot.pixelDropRight.selectBoolPos(Utils.floatToBool(gamepad1.right_trigger));
            if (gamepad1.dpad_up && bot.slide.getPosition() > 0.2) {
                bot.wrist.openServo();
            }
            if (gamepad1.dpad_down) {
                bot.wrist.closeServo();
            }
            if (gamepad1.dpad_right && bot.slide.getPosition() > 0.2) {
                bot.gripper.openServo();
            }
            if (gamepad1.dpad_left) {
                bot.gripper.closeServo();
            }

            // Get data for telemetry
            double[] pos = bot.pose.returnPose();

            double voltage = bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                telemetry.addData("WARNING: Voltage Low: ", voltage);
            }

            // Telemetry and update
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("X", pos[0]);
            telemetry.addData("Y", pos[1]);
            telemetry.addData("Theta", pos[2]);
            telemetry.addData("Voltage", voltage);
            telemetry.update();
        }
    }
}