package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.ServoClass;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

@TeleOp(name="Servo Position", group="Calibration")
public class ServoPositions extends LinearOpMode {
    public void runOpMode() {

        ServoClass[] servos = new ServoClass[]{
                new ServoClass(hardwareMap.get(Servo.class, Config.misumiDriveR), Config.misumiDriveROpen, Config.misumiDriveRClose),
                new ServoClass(hardwareMap.get(Servo.class, Config.intakeYawServo), Config.intakeYawLeft, Config.intakeYawRight),
                new ServoClass(hardwareMap.get(Servo.class, Config.intakeRollServo), Config.intakeRollUp, Config.intakeRollDown),
                new ServoClass(hardwareMap.get(Servo.class, Config.intakeClawServo), Config.intakeClawOpen, Config.intakeClawClose),
                new ServoClass(hardwareMap.get(Servo.class, Config.clawServo), Config.clawOpen, Config.clawClose),
                new ServoClass(hardwareMap.get(Servo.class, Config.ocgPitchServo), Config.ocgBoxPitchUp, Config.ocgBoxPitchDown),
                new ServoClass(hardwareMap.get(Servo.class, Config.specimenWristServo), Config.specWristCollect, Config.specWristHang),
                new ServoClass(hardwareMap.get(Servo.class, Config.misumiWristR), Config.misumiWristRUp, Config.misumiWristRDown),
                new ServoClass(hardwareMap.get(Servo.class, Config.misumiWristL), Config.misumiWristLUp, Config.misumiWristLDown),
        };

        ServoClass servo = servos[0];

        Toggle servoToggle = new Toggle(false);

        int index = 0;

        while (opModeInInit()) {
            servoToggle.toggle(gamepad1.circle);

            if (servoToggle.justChanged() && servoToggle.get()) {
                index += 1;
                index %= servos.length;
                servo = servos[index];
            }

            telemetry.addData("Servo Index", index);
            telemetry.update();
        }

        while(opModeIsActive()) {
            if (gamepad1.left_bumper) {
                servo.closeServo();
            } else if (gamepad1.right_bumper) {
                servo.openServo();
            } else {
                servo.anyPos(gamepad1.right_trigger);
            }

            telemetry.addData("Servo Pos", servo.getPos());
            telemetry.addData("Servo Index", index);
            telemetry.update();
        }
    }
}
