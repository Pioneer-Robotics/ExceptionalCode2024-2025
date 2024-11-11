package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;


@TeleOp(name="Specimen Hang Test", group="Testing")
public class HardwareTestTeleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle clawToggle = new Toggle(false);

        // Initialize max speed
        double maxSpeed = 0.5;

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

            if (gamepad1.x) {
                Bot.imu.resetYaw();
            }

            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged()) {
                maxSpeed += 1.0;
            }
            if (decSpeedToggle.justChanged()) {
                maxSpeed -= 1.0;
            }

             // Specimen Arm
//            if (gamepad1.left_trigger > 0.1) {
//                Bot.specimenArm.move(-gamepad1.left_trigger);
//            } else if (gamepad1.right_trigger > 0.1) {
//                Bot.specimenArm.move(gamepad1.right_trigger);
//            } else {
//                Bot.specimenArm.move(0);
//            }

            clawToggle.toggle(gamepad1.dpad_right);
            if (clawToggle.justChanged()) {
                Bot.specimenArm.setClawPosBool(clawToggle.get());
            }

//            Bot.specimenArm.setWristPos(gamepad1.left_trigger);

            if (gamepad1.dpad_up) {
                Bot.specimenArm.moveToPos1(0.5);
            } else if (gamepad1.dpad_down) {
                Bot.specimenArm.moveToPos2(1.0);
            } else if (gamepad1.dpad_left) {
                Bot.specimenArm.moveToPos3(0.5);
            }

            // ---- GamePad 2 ----
            // None

            // Get data for telemetry

            double voltage = Bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                telemetry.addData("WARNING: Voltage Low", voltage);
            }

            // Telemetry and update
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("Arm Pos", Bot.specimenArm.getPositionTicks());
            telemetry.addData("Voltage", voltage);
            telemetry.update();
        }
    }
}