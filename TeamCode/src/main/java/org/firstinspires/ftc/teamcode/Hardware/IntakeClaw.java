package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class IntakeClaw {
    private final ServoClass clawServo, yawServo, rollServo;
    private final double rollServoMid = (Config.intakeRollLeft - Config.intakeRollRight) / 2;
    private final double rollServo45 = (rollServoMid - Config.intakeRollRight) / 2;
    private final double rollServoNeg45 = (rollServoMid - Config.intakeRollLeft) / 2;


    public IntakeClaw() {
        clawServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeClawServo), Config.intakeClawOpen, Config.intakeClawClose);
        yawServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeYawServo), Config.intakeYawUp, Config.intakeYawDown);
        rollServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeRollServo), Config.intakeRollLeft, Config.intakeRollRight);
        clawServo.closeServo();
        clawUp();
    }

    public void clawDown() {
        yawServo.openServo();
        rollServo.anyPos(rollServoMid);
    }

    public void clawUp() {
        yawServo.closeServo();
        rollServo.anyPos(rollServoMid);
    }

    public void clawPos90() {
        rollServo.anyPos(Config.intakeRollRight);
    }

    public void clawPos45() {
        rollServo.anyPos(rollServo45);
    }

    public void clawPos0() {
        rollServo.anyPos(rollServoMid);
    }

    public void clawNeg45() {
        rollServo.anyPos(rollServoNeg45);
    }

    public void clawNeg90() {
        rollServo.anyPos(Config.intakeRollLeft);
    }

    public void openClaw() { clawServo.openServo(); }
    public void closeClaw() { clawServo.closeServo(); }

    // Getters
    public double getYaw() { return(yawServo.getPos()); }
    public double getRoll() { return(rollServo.getPos()); }
    public double getClawPos() { return(clawServo.getPos()); }
}
