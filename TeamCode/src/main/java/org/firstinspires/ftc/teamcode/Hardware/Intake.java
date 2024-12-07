package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * Class for intake mechanism: Misumi slide and intake wheels
 */
public class Intake {

    ServoClass misumiDriveL, misumiDriveR, misumiWristL, misumiWristR;
    CRServo intakeWheelL, intakeWheelR;
    boolean isExtended = false;

    public Intake() {
        misumiDriveL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveL), Config.misumiDriveLOpen, Config.misumiDriveLClose);
        misumiDriveR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveR), Config.misumiDriveROpen, Config.misumiDriveRClose);

        misumiWristL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristL), Config.misumiWristLOpen, Config.misumiWristLClose);
        misumiWristR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristR), Config.misumiWristROpen, Config.misumiWristRClose);

        intakeWheelL = Bot.opMode.hardwareMap.get(CRServo.class, Config.intakeWheelL);
        intakeWheelR = Bot.opMode.hardwareMap.get(CRServo.class, Config.intakeWheelR);
        intakeWheelL.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeWheelR.setDirection(DcMotorSimple.Direction.FORWARD);

        closeMisumiDrive();
        midWrist();
    }


    // Misumi Servos
    public void openMisumiDrive() {
        misumiDriveL.openServo();
        misumiDriveR.openServo();
        isExtended = true;
    }

    public void closeMisumiDrive() {
        misumiDriveL.closeServo();
        misumiDriveR.closeServo();
        isExtended = false;
    }

    public void openWrist() {
        misumiWristL.openServo();
        misumiWristR.openServo();
    }

    public void midWrist() {
        misumiWristL.anyPos(Config.misumiWristLMid);
        misumiWristR.anyPos(Config.misumiWristRMid);
    }

    public void closeWrist() {
        misumiWristL.closeServo();
        misumiWristR.closeServo();
    }

    // Intake Wheels

    public void spinWheels(double power) {
        intakeWheelL.setPower(-power);
        intakeWheelR.setPower(power);
    }

    public void spinWheels() {
        spinWheels(Config.intakeWheelPower);
    }

    public void stopWheels() {
        spinWheels(0);
    }

    public boolean isExtended() { return isExtended; }
}
