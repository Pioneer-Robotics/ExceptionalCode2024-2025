package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class IntakeClaw {
    public ServoClass clawServo, rollServo, yawServo;
    private final double yawServoMid = (Config.intakeYawLeft + Config.intakeYawRight) / 2;
    private final double yawServo45 = (yawServoMid + Config.intakeYawRight) / 2;
    private final double yawServoNeg45 = (yawServoMid + Config.intakeYawLeft) / 2;
    private boolean clawState;

    public IntakeClaw() {
        clawServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeClawServo), Config.intakeClawOpen, Config.intakeClawClose);
        rollServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeRollServo), Config.intakeRollUp, Config.intakeRollDown);
        yawServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.intakeYawServo), Config.intakeYawLeft, Config.intakeYawRight);
        clawServo.closeServo();
        clawPos0();
        clawUp();
    }

    public void clawDown() {
        rollServo.anyPos(Config.intakeRollDown);
        yawServo.anyPos(yawServoMid);
    }

    public void clawUp() {
        rollServo.anyPos(Config.intakeRollUp);
        yawServo.anyPos(yawServoMid);
    }

    public void yawAnyPosAngle(double angle){yawServo.anyPos((yawServoMid+Config.intakeYawRight)/(90/angle));}

    public void yawAnyNegAngle(double angle){yawServo.anyPos((yawServoMid+Config.intakeYawLeft)/(90/angle));}



    public void clawPos90() {
        yawServo.anyPos(Config.intakeYawRight);
    }

    public void clawPos45() {
        yawServo.anyPos(yawServo45);
    }

    public void clawPos0() {
        yawServo.anyPos(yawServoMid);
    }

    public void clawNeg45() {
        yawServo.anyPos(yawServoNeg45);
    }

    public void clawNeg90() {
        yawServo.anyPos(Config.intakeYawLeft);
    }

    public void openClaw() { clawServo.openServo(); clawState = true;}
    public void closeClaw() { clawServo.closeServo(); clawState = false;}

    // Getters
    public double getYaw() { return(rollServo.getPos()); }
    public double getRoll() { return(yawServo.getPos()); }
    public double getClawPos() { return(clawServo.getPos()); }
    public boolean isClawOpen() { return (Math.abs(getClawPos() - Config.intakeClawOpen) < 0.1); }
    public boolean getClawState(){return clawState;}
}
