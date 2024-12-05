package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.Helpers.TrueAngle;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;

public class TheseusTeleop extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        // Toggles
        Toggle northModeToggle = new Toggle(true);
        Toggle incSpeedToggle = new Toggle(false);
        Toggle decSpeedToggle = new Toggle(false);
        Toggle clawToggle = new Toggle(false);

        PID turnPid = new PID(Config.turnPID[0], Config.turnPID[1], Config.turnPID[2], 0, 0.1);
        TrueAngle trueAngle = new TrueAngle(0);
        double turnTarget = 0;

        // Initialize max speed
        double maxSpeed = 0.5;

        waitForStart();

        while (opModeIsActive()) {

            /*-------------------
             -     Gamepad 1    -
             --------------------*/

            // Driving
            double px = gamepad1.left_stick_x;
            double py = -gamepad1.left_stick_y;
            turnTarget += gamepad1.right_stick_x/15;
            double trueTheta = trueAngle.updateAngle(-Bot.imu.getRadians());

            Bot.mecanumBase.move(px, py, turnPid.calculate(trueTheta, turnTarget), maxSpeed);

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
                Bot.intake.openMisumiDrive();
            } if (gamepad1.dpad_down) {
                Bot.intake.closeMisumiDrive();
            }

            if (gamepad1.b) {
                Bot.intake.openWrist();
            } if (gamepad1.x) {
                Bot.intake.closeWrist();
            }

            // false = out, true = in
            if (gamepad1.y) {
                Bot.intake.setWheelDirection(false);
            } if (gamepad1.a) {
                Bot.intake.setWheelDirection(true);
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

            // Claw toggle
            clawToggle.toggle(gamepad2.b);
            if (clawToggle.justChanged()) {
                Bot.specimenArm.setClawPosBool(clawToggle.get());
            }



        }
    }
}
