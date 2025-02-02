package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;


@TeleOp(name="Servo Position", group="Calibration")
public class ServoPositions extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        Bot.specimenArm.specimenWrist.anyPos(0.5);

        waitForStart();

        while(opModeIsActive()) {

            Bot.specimenArm.specimenWrist.anyPos(gamepad1.left_trigger);


            telemetry.addData("Servo Pos", Bot.specimenArm.specimenWrist.getPos());
            telemetry.update();
        }
    }
}
