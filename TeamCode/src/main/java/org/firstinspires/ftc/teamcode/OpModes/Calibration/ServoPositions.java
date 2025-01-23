package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;


@TeleOp(name="Servo Position", group="Calibration")
public class ServoPositions extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        waitForStart();

        while(opModeIsActive()) {

            Bot.intake.misumiWristR.anyPos(gamepad1.left_trigger);

            telemetry.addData("Servo Pos", Bot.intake.misumiWristR.getPos());
            telemetry.update();
        }
    }
}
