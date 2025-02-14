package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;

@Disabled
@TeleOp(name = "Hang Test")
public class HangTest extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.left_trigger > 0.1) {
                Bot.slideArm.move(gamepad1.left_trigger);
            } else if (gamepad1.right_trigger > 0.1) {
                Bot.slideArm.move(-gamepad1.right_trigger);
            } else {
                Bot.slideArm.move(0);
            }
            telemetry.addData("Right Trigger", gamepad1.right_trigger);
            telemetry.addData("Left Trigger", gamepad1.left_trigger);
            telemetry.update();
        }
    }
}
