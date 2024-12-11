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

    ServoClass misumiDriveL, misumiDriveR, misumiWristL, misumiWristR, intakeClaw, intakeWrist;
    boolean isExtended;

    public Intake() {
        misumiDriveL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveL), Config.misumiDriveLOpen, Config.misumiDriveLClose);
        misumiDriveR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveR), Config.misumiDriveROpen, Config.misumiDriveRClose);

        misumiWristL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristL), Config.misumiWristLOpen, Config.misumiWristLClose);
        misumiWristR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristR), Config.misumiWristROpen, Config.misumiWristRClose);

        intakeClaw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeClaw), Config.intakeClawOpen, Config.intakeClawClose);
        intakeWrist = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeWrist), Config.intakeWristOpen, Config.intakeWristClose);

        closeMisumiDrive();
        midWrist();
        closeClaw();
        isExtended = false;
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

    public void openClaw() {
        intakeClaw.openServo();
    }

    public void closeClaw() {
        intakeClaw.closeServo();
    }

    public void openIntakeWrist() {
        intakeWrist.openServo();
    }

    public void closeIntakeWrist() {
        intakeWrist.closeServo();
    }

        public boolean isExtended() {
        return isExtended;
    }
}