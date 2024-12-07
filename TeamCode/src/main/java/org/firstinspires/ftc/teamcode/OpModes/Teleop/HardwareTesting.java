package org.firstinspires.ftc.teamcode.OpModes.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Hardware.SlideArm;

@TeleOp(name="Hardware testing")
public class HardwareTesting extends LinearOpMode {
    SlideArm slideArm;
    public void runOpMode() throws InterruptedException {
        Bot.init(this);
//        slideArm = new SlideArm();

        waitForStart();

        while (opModeIsActive()) {

            // OCG Arm/Box

            // Slide arm positions
//            if (gamepad2.y) {
//                Bot.slideArm.moveUp(0.2);
//            } else if (gamepad2.a) {
//                Bot.slideArm.moveDown(0.2);
//            } else if (gamepad2.x) {
//                Bot.slideArm.moveMid(0.2);
//            } else {
//                Bot.slideArm.moveDown(0.2);
//            }

//            telemetry.addData("Servo Left Pos", Bot.intake.getMisumiDriveL());
//            telemetry.addData("Servo Right Pos", Bot.intake.getMisumiDriveR());
//            telemetry.addData("Slide height", Bot.slideArm.getArmPosition());
            if (gamepad1.dpad_up) {
                Bot.intake.openMisumiDrive();
                Bot.intake.openWrist();
            } if (gamepad1.dpad_down) {
                Bot.intake.closeMisumiDrive();
                Bot.intake.midWrist();
            }

            if (gamepad1.y) {
                Bot.intake.spinWheels(1);
            } else if (gamepad1.a) {
                Bot.intake.spinWheels(-1);
            } else {
                Bot.intake.stopWheels();
            }
            telemetry.addData("OCG Arm Ticks", Bot.slideArm.getArmPosition());
            if (gamepad1.b) {
                Bot.intake.openWrist();
            } if (gamepad1.x) {
                Bot.intake.closeWrist();
            }
            telemetry.update();
        }
    }
}
