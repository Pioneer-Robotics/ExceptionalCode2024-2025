package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.Hardware.ServoClass;

@TeleOp(name="Servo Test")
public class ServoOff extends LinearOpMode {
    public void runOpMode() {
        CRServo servo1 = hardwareMap.get(CRServo.class, "intakeLeft");
        CRServo servo2 = hardwareMap.get(CRServo.class, "intakeRight");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpad_up) {
                servo1.setPower(-1);
                servo2.setPower(1);
            } else if (gamepad1.dpad_down) {
                servo1.setPower(1);
                servo2.setPower(-1);
            } else {
                servo1.setPower(0);
                servo2.setPower(0);
            }
        }
    }
}
