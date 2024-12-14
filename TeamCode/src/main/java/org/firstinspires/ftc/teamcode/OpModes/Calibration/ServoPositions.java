package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;


@TeleOp(name="Servo Position", group="Calibration")
public class ServoPositions extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        waitForStart();

        while(opModeIsActive()) {

            Bot.intake.misumiDriveL.anyPos(gamepad1.left_trigger);

            telemetry.addData("Servo Pos", Bot.intake.misumiDriveL.getPos());
            telemetry.update();
        }
    }
}
