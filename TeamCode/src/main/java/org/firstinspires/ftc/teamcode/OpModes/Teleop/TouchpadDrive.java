package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.MecanumBase;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

@Disabled
@TeleOp(name="Touchpad Drive")
public class TouchpadDrive extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        Bot.mecanumBase.setNorthMode(true);

        waitForStart();

        while(opModeIsActive()) {
            double x = 0;
            double y = 0;
            double x2 = 0;
            double y2 = 0;

            if (gamepad1.touchpad_finger_1 && !gamepad1.touchpad_finger_2) {
                x = gamepad1.touchpad_finger_1_x;
                y = gamepad1.touchpad_finger_1_y;
            }

            if (gamepad1.touchpad_finger_2) {
                x2 = gamepad1.touchpad_finger_2_x;
                y2 = gamepad1.touchpad_finger_2_y;
            }

            double turn = 0;
            if (x2 != 0 || y2 != 0) {
                double dx = x2-x;
                double dy = y2-y;
                turn = Math.atan2(dx,dy);
            }

            Bot.mecanumBase.move(x, y, turn, 0.5);
            gamepad1.setLedColor(gamepad1.left_trigger,0,gamepad1.right_trigger,-1);

            telemetry.addData("turn", turn);
            telemetry.addData("x", x);
            telemetry.addData("y", y);
            telemetry.addData("x2", x2);
            telemetry.addData("y2", y2);
            telemetry.update();
        }
    }
}
