package org.firstinspires.ftc.teamcode.OpModes.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;

@TeleOp(name="Hardware testing")
public class HardwareTesting extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        Bot.init(this);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) {
                Bot.intake.openMisumiDrive();
            }
            if (gamepad1.b) {
                Bot.intake.closeMisumiDrive();
            }

            telemetry.addData("Servo Left Pos", Bot.intake.getMisumiDriveL());
            telemetry.addData("Servo Right Pos", Bot.intake.getMisumiDriveR());
            telemetry.addData("Slide height", Bot.slideArm.getArmPosition());
        }
    }
}
