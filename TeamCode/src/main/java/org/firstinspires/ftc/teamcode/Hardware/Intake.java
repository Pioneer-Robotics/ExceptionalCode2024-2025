package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * Class for intake mechanism: Misumi slide and intake wheels
 */
public class Intake {

    public ServoClass misumiDriveL, misumiDriveR, misumiWristL, misumiWristR;
    boolean isExtended;

    public Intake() {
//        misumiDriveL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveL), Config.misumiDriveLOpen, Config.misumiDriveLClose);
        misumiDriveR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiDriveR), Config.misumiDriveROpen, Config.misumiDriveRClose);

        misumiWristL = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristL), Config.misumiWristLDown, Config.misumiWristLUp);
        misumiWristR = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.misumiWristR), Config.misumiWristRDown, Config.misumiWristRUp);

//        retractMisumiDrive();
        misumiDriveR.anyPos(Config.misumiDriveRInit);
        misumiWristUp();
//        midMisumiWrist();
        isExtended = false;
    }

    // Misumi Servos
    public void extendMisumiDrive() {
//        misumiDriveL.openServo();
        misumiDriveR.openServo();
        isExtended = true;
    }

    public void retractMisumiDrive() {
//        misumiDriveL.closeServo();
        misumiDriveR.closeServo();
        isExtended = false;
    }

    public void midMisumiDrive() {
//        misumiDriveL.anyPos(Config.misumiDriveLMid);
        misumiDriveR.anyPos(Config.misumiDriveRMid);
        isExtended = false;
    }

    /***
     * Wrist out
     */
    public void misumiWristDown() {
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
    public void misumiWristUp() {
        misumiWristL.closeServo();
        misumiWristR.closeServo();
    }

    public double getDrivePos() {
        return misumiDriveR.getPos();
    }

    public boolean isExtended() {
        return (Math.abs(getDrivePos() - Config.misumiDriveROpen) < 0.05);
    }

    public boolean isWristUp() {
        return ((Math.abs(misumiWristL.getPos() - Config.misumiWristLUp) > 0.1) &&
                (Math.abs(misumiWristR.getPos() - Config.misumiWristRUp) > 0.1));
    }
}