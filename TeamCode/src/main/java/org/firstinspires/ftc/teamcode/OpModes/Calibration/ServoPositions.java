package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Hardware.ServoClass;


@TeleOp(name="Servo Position", group="Calibration")
public class ServoPositions extends LinearOpMode {
    public void runOpMode() {
        ServoClass servo = new ServoClass(hardwareMap.get(Servo.class, "testServo"),0,1);
//        Bot.init(this);
//        Bot.intakeClaw.rollServo.anyPos(0.5);


        waitForStart();

        while(opModeIsActive()) {

//            Bot.intakeClaw.yawServo.anyPos(gamepad1.left_trigger);
            servo.anyPos(gamepad1.left_trigger);


//            telemetry.addData("Servo Pos", Bot.intakeClaw.yawServo.getPos());
            telemetry.addData("Servo Pos", servo.getPos());
            telemetry.update();
        }
    }
}
