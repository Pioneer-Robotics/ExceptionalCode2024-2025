package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class IntakeClaw {
    private ServoClass clawServo, yawServo, rollServo;

    public IntakeClaw() {
        clawServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.clawServo), 0, 0.5);
        yawServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.diffyServo1), 0, 0.5);
        rollServo = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.diffyServo2), 0, 0.5);
        clawServo.closeServo();
        clawUp();
    }

    public void clawDown(double yaw) {
        yawServo.anyPos(yaw);
        rollServo.anyPos(0.5);
    }
    public void clawDown() {
        clawDown(0.5);
    }
    public void clawUp() {
        yawServo.anyPos(0.5);
        rollServo.anyPos(0);
    }

    public void clawPos90() { clawDown(0.75); }
    public void clawPos45() { clawDown(0.625); }
    public void clawPos0() { clawDown(0.5); }
    public void clawNeg45() { clawDown(0.375); }
    public void clawNeg90() { clawDown(0.25); }

    public void openClaw() { clawServo.openServo(); }
    public void closeClaw() { clawServo.closeServo(); }

    // Getters
    public double getYaw() { return(yawServo.getPos()); }
    public double getRoll() { return(rollServo.getPos()); }
    public double getClawPos() { return(clawServo.getPos()); }
}
