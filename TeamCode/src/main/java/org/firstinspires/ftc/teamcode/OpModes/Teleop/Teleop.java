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

            // Specimen Arm
            // Manual movement
//            if (gamepad1.left_trigger > 0.1) {
//                Bot.specimenArm.move(-gamepad1.left_trigger);
//            } else if (gamepad1.right_trigger > 0.1) {
//                Bot.specimenArm.move(gamepad1.right_trigger);
//            } else {
//                Bot.specimenArm.move(0);
//            }

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
            telemetry.addData("Speed", maxSpeed);
            telemetry.addData("Arm Pos", Bot.specimenArm.getPositionTicks());
            telemetry.addData("Voltage", voltage);
            telemetry.update();
        }
    }
}
