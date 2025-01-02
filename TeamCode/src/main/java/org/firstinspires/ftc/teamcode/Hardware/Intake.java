package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * Class for intake mechanism: Misumi slide and intake wheels
 */
public class Intake {

    public ServoClass misumiDriveL, misumiDriveR, misumiWristL, misumiWristR, intakeClaw, intakeWrist;
    boolean isExtended;

    public Intake() {
        misumiDriveL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveL), Config.misumiDriveLOpen, Config.misumiDriveLClose);
        misumiDriveR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveR), Config.misumiDriveROpen, Config.misumiDriveRClose);

        misumiWristL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristL), Config.misumiWristLOpen, Config.misumiWristLClose);
        misumiWristR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristR), Config.misumiWristROpen, Config.misumiWristRClose);

        intakeClaw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeClaw), Config.intakeClawOpen, Config.intakeClawClose);
        intakeWrist = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeWrist), Config.intakeWristOpen, Config.intakeWristClose);

        misumiDriveL.anyPos(Config.misumiDriveLClose-0.1);
        misumiWristR.anyPos(Config.misumiWristRInit);
        misumiWristL.anyPos(Config.misumiWristLInit);
        closeIntakeWrist();
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

    public void midMisumiDrive() {
        misumiDriveL.anyPos(Config.misumiDriveLMid);
        misumiDriveR.anyPos(Config.misumiDriveRMid);
        isExtended = false;
    }

    /***
     * Wrist out
     */
    public void openMisumiWrist() {
        misumiWristL.openServo();
        misumiWristR.openServo();
    }

    public void midMisumiWrist() {
        misumiWristL.anyPos(Config.misumiWristLMid);
        misumiWristR.anyPos(Config.misumiWristRMid);
    }

    /***
     * Wrist in
     */
    public void closeMisumiWrist() {
        misumiWristL.closeServo();
        misumiWristR.closeServo();
    }

    public void openClaw() {
        intakeClaw.openServo();
    }

    public void closeClaw() {
        intakeClaw.closeServo();
    }

    /***
     * Turns claw towards OCG box
     */
    public void openIntakeWrist() {
        intakeWrist.openServo();
    }

    /***
     * Turns claw down towards sample/ground
     */
    public void closeIntakeWrist() {
        intakeWrist.closeServo();
    }

        public boolean isExtended() {
        return isExtended;
    }
}