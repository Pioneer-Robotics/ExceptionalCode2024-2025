package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class SpecimenArm {
    private final DcMotorEx motor;
    private double defaultSpeed;
    ServoClass wrist, claw;

    public SpecimenArm() {
        motor = Bot.opMode.hardwareMap.get(DcMotorEx.class, "specimenMotor");
        motor.setTargetPositionTolerance(5);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        defaultSpeed = 0.5;

        // Servos
        wrist = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, "wristServo"), Config.wristOpen, Config.wristClose);
        claw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, "clawServo"), Config.clawOpen, Config.clawClose);

        wrist.closeServo();
        claw.closeServo();
    }
    public void move(double speed) {
        motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(speed);
//        if (speed > 0 && motor.getCurrentPosition() < -10) {
//            motor.setPower(speed);
//        } else if (speed < 0 && motor.getCurrentPosition() > -3000) {
//            motor.setPower(speed);
//        } else {
//            motor.setPower(0);
//        }
    }

    public void moveToPos(int positionTicks, double speed) {
        motor.setTargetPosition(positionTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Config.maxSlideTicksPerSecond * speed);
    }

    public void movePrepHang(double speed) {
        moveToPos(850, speed);
        wrist.closeServo();
    }

    public void moveHangDown(double speed) {
        moveToPos(575, speed);
        wrist.closeServo();
    }

    public void moveToCollect(double speed) {
        moveToPos(1850, speed);
        wrist.openServo();
    }

    public void moveToPos(int positionTicks) {
        moveToPos(positionTicks, defaultSpeed);
    }

    public void openClaw() { claw.openServo(); }
    public void closeClaw() { claw.closeServo(); }
    public void openWrist() { wrist.openServo(); }
    public void closeWrist() { wrist.closeServo(); }

    public void setWristPosBool(boolean pos) { wrist.selectBoolPos(pos); }
    public void setClawPosBool(boolean pos) { claw.selectBoolPos(pos); }
    public void setWristPos(double pos) { wrist.anyPos(pos); }

    public int getPositionTicks() {
        return motor.getCurrentPosition();
    }
    public boolean reachedPosition() { return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < Config.specimenArmTolerance; }
}
