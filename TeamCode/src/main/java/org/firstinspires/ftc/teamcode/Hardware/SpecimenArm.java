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
        defaultSpeed = 0.75;

        // Servos
        double wristOpen = 0.0;
        double wristClose = 1.0;
        wrist = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, "wristServo"), wristOpen, wristClose);
        double clawOpen = 0.5;
        double clawClose = 1.0;
        claw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, "clawServo"), clawOpen, clawClose);
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

    public void moveToPos1(double speed) {
        motor.setTargetPosition(-700);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Config.maxSlideTicksPerSecond * speed);
        wrist.closeServo();
    }

    public void moveToPos2(double speed) {
        motor.setTargetPosition(-450);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Config.maxSlideTicksPerSecond * speed);
        wrist.closeServo();
    }

    public void moveToPos3(double speed) {
        motor.setTargetPosition(-1650);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Config.maxSlideTicksPerSecond * speed);
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
}
