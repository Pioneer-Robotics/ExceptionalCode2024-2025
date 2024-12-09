package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;


@TeleOp(name="Specimen Arm Teleop")
public class Teleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle clawToggle = new Toggle(false);
        Toggle intakeClawToggle = new Toggle(false);
        Toggle intakeWristToggle = new Toggle(false);

        // Initialize max speed
        double maxSpeed = 0.5;

        waitForStart();

        while(opModeIsActive()) {
            // ---- GamePad 1 ----
            // Inputs for driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            // Move
            Bot.mecanumBase.move(px, py, turn, maxSpeed);

            // Toggle for field centric
            northModeToggle.toggle(gamepad1.a); // Toggle north mode
            Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode

            if (gamepad1.x) {
                Bot.imu.resetYaw();
            }

            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged()) {
                maxSpeed += 0.1;
            }
            if (decSpeedToggle.justChanged()) {
                maxSpeed -= 0.1;
            }

//            if (gamepad1.dpad_up) {
//                Bot.intake.openMisumiDrive();
//            } else if (gamepad1.dpad_down) {
//                Bot.intake.closeMisumiDrive();
//            }

            if (gamepad1.dpad_down) {
                Bot.intake.openWrist();
            } else if (gamepad1.dpad_up) {
                Bot.intake.closeWrist();
            }

            intakeClawToggle.toggle(gamepad1.b);
            if (intakeClawToggle.justChanged() && intakeClawToggle.get()) {
                Bot.intake.openClaw();
            } else if (intakeClawToggle.justChanged() && !intakeClawToggle.get()) {
                Bot.intake.closeClaw();
            }

            intakeWristToggle.toggle(gamepad1.y);
            if (intakeWristToggle.justChanged() && intakeWristToggle.get()) {
                Bot.intake.openIntakeWrist();
            } else if (intakeWristToggle.justChanged() && !intakeWristToggle.get()) {
                Bot.intake.closeIntakeWrist();
            }

            // ---- GamePad 2 ----
            // Preset arm positions
            if (gamepad2.dpad_up) {
                Bot.specimenArm.movePostHang(1.0);
            } else if (gamepad2.dpad_down) {
                Bot.specimenArm.movePrepHang(0.5);
            } else if (gamepad2.dpad_left) {
                Bot.specimenArm.moveToCollect(0.5);
            }

            // Claw toggle
            clawToggle.toggle(gamepad2.b);
            if (clawToggle.justChanged()) {
                Bot.specimenArm.setClawPosBool(clawToggle.get());
            }

            // Get data for telemetry

            double voltage = Bot.voltageHandler.getVoltage();
            if (voltage < 10) {
                Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
                telemetry.addData("WARNING: Voltage Low", voltage);
            } else {
                Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }

            // Telemetry and update
            double[] pos = Bot.deadwheel_odom.returnPose();
            double[] encoders = Bot.mecanumBase.getEncoders();
            telemetry.addData("Left Odometer", Bot.deadwheel_odom.getRawOdoLeft());
            telemetry.addData("Center Odometer", Bot.deadwheel_odom.getRawOdoCenter());
            telemetry.addData("Pos X", pos[0]);
            telemetry.addData("Pos Y", pos[1]);
            telemetry.addData("Pos Theta", Bot.imu.getRadians());
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("Arm Pos", Bot.specimenArm.getPositionTicks());
            telemetry.addData("Voltage", voltage);
            telemetry.update();
        }
    }
}
