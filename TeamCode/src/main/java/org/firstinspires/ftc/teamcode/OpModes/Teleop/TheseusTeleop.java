package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.Helpers.TrueAngle;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;

@TeleOp(name="Theseus Teleop")
public class TheseusTeleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle clawToggle = new Toggle(false);
        Toggle ocgBoxToggle = new Toggle(false);

//        PID turnPid = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2], 0, 0.1);
//        TrueAngle trueAngle = new TrueAngle(0);
//        double turnTarget = 0;

        // Initialize max speed
        double maxSpeed = 0.5;

        waitForStart();

        while (opModeIsActive()) {

            /*-------------------
             -     Gamepad 1    -
             --------------------*/

            // Driving
            //TODO: ADD NORTH MODE
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
//            turnTarget += gamepad1.right_stick_x/15;
//            double trueTheta = trueAngle.updateAngle(-Bot.imu.getRadians());

            northModeToggle.toggle(gamepad1.right_trigger > 0.9 && gamepad1.left_trigger > 0.9);

            Bot.mecanumBase.setNorthMode(northModeToggle.get());
            Bot.mecanumBase.move(px, py, gamepad1.right_stick_x, maxSpeed);

            // Speed toggle
            incSpeedToggle.toggle(gamepad1.right_bumper);
            decSpeedToggle.toggle(gamepad1.left_bumper);
            if (incSpeedToggle.justChanged()) {
                maxSpeed += 0.1;
            }
            if (decSpeedToggle.justChanged()) {
                maxSpeed -= 0.1;
            }

            // Intake
            if (gamepad1.dpad_up) {
                Bot.intake.openWrist();
                Bot.intake.openMisumiDrive();
            } if (gamepad1.dpad_down) {
                Bot.intake.closeMisumiDrive();
                Bot.intake.midWrist();
            }

            if (gamepad1.b) {
                Bot.intake.openWrist();
            } if (gamepad1.x) {
                Bot.intake.closeWrist();
            }

//            if (gamepad1.y) {
//                Bot.intake.spinWheels(1);
//            }
            if (gamepad1.a) {
                Bot.intake.spinWheels(-1);
            } else if (Bot.intake.isExtended()) {
                Bot.intake.spinWheels(1);
            } else {
                Bot.intake.stopWheels();
            }

            /*-------------------
             -     Gamepad 2    -
             --------------------*/

            // Preset specimen arm positions
            if (gamepad2.dpad_up) {
                Bot.specimenArm.movePostHang(1.0);
            } else if (gamepad2.dpad_down) {
                Bot.specimenArm.movePrepHang(0.5);
            } else if (gamepad2.dpad_left) {
                Bot.specimenArm.moveToCollect(0.5);
            }
            // OCG Arm/Box

            // Slide arm positions
            if (gamepad2.y) {
                Bot.slideArm.moveToPositionTicks(Config.slideHighBasket, 0.8);
            } else if (gamepad2.a) {
                Bot.slideArm.moveToPositionTicks(Config.slideDown, 0.8);
            } else if (gamepad2.x) {
                Bot.slideArm.moveToPositionTicks(Config.slideLowBasket, 0.8);
            }

            // Box state
            ocgBoxToggle.toggle(gamepad2.dpad_right);
            if (ocgBoxToggle.justChanged()) {
                Bot.slideArm.setOCGBox(ocgBoxToggle.get());
            }

//            if (gamepad2.dpad_right) {
//                Bot.slideArm.ocgUp();
//            }

            // Claw toggle
            clawToggle.toggle(gamepad2.b);
            if (clawToggle.justChanged()) {
                Bot.specimenArm.setClawPosBool(clawToggle.get());
            }

            telemetry.addData("OCG Toggle", ocgBoxToggle.get());
            telemetry.update();
        }
    }
}
