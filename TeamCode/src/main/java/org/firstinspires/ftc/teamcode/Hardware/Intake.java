package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.ConfigNew;

/**
 * Class for intake mechanism: Misumi slide and intake wheels
 */
public class Intake {

    ServoClass misumiDriveL, misumiDriveR, misumiWristL, misumiWristR;
    CRServo intakeWheelL, intakeWheelR;

    public Intake() {
        misumiDriveL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, ConfigNew.misumiDriveL), ConfigNew.misumiDriveLOpen, ConfigNew.misumiDriveLClose);
        misumiDriveR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, ConfigNew.misumiDriveR), ConfigNew.misumiDriveROpen, ConfigNew.misumiDriveRClose);

        misumiWristL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, ConfigNew.misumiWristL), ConfigNew.misumiWristLOpen, ConfigNew.misumiWristLClose);
        misumiWristR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, ConfigNew.misumiWristR), ConfigNew.misumiWristROpen, ConfigNew.misumiWristRClose);

        intakeWheelL = Bot.opMode.hardwareMap.get(CRServo.class, ConfigNew.intakeWheelL);
        intakeWheelR = Bot.opMode.hardwareMap.get(CRServo.class, ConfigNew.intakeWheelR);
        intakeWheelL.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeWheelR.setDirection(DcMotorSimple.Direction.FORWARD);

        closeMisumiDrive();
    }


    // Misumi Servos
    public void openMisumiDrive() {
        misumiDriveL.openServo();
        misumiDriveR.openServo();
    }

    public void closeMisumiDrive() {
        misumiDriveL.closeServo();
        misumiDriveR.closeServo();
    }

    public void openWrist() {
        misumiWristL.openServo();
        misumiWristR.openServo();
    }

    public void closeWrist() {
        misumiWristL.closeServo();
        misumiWristR.closeServo();
    }

    // Intake Wheels
    public void setWheelDirection(DcMotorSimple.Direction direction) {
        intakeWheelL.setDirection(direction);
        intakeWheelR.setDirection(direction);
    }

    public void toggleWheelDirection() {
        DcMotorSimple.Direction direction = intakeWheelL.getDirection();

        if (direction == DcMotorSimple.Direction.FORWARD) {
            setWheelDirection(DcMotorSimple.Direction.REVERSE);
        }
        else if (direction == DcMotorSimple.Direction.REVERSE) {
            setWheelDirection(DcMotorSimple.Direction.FORWARD);
        }
    }

    public void spinWheels(double power) {
        intakeWheelL.setPower(power);
        intakeWheelR.setPower(power);
    }

    public void spinWheels() {
        spinWheels(ConfigNew.intakeWheelPower);
    }

    public void stopWheels() {
        spinWheels(0);
    }

    public double getMisumiDriveL() {return misumiDriveL.getPos();}
    public double getMisumiDriveR() {return misumiDriveR.getPos();}
    public double getMisumiWristL() {return misumiWristL.getPos();}
    public double getMisumiWristR() {return misumiWristR.getPos();}
}
