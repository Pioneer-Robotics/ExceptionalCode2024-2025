package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class SpecimenArm {
    private final DcMotorEx motor;
    ServoClass wrist, claw;

    public SpecimenArm() {
        motor = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.specimenArmMotor);
        motor.setTargetPositionTolerance(5);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Servos
        wrist = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.wristServo), Config.wristOpen, Config.wristClose);
        claw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.clawServo), Config.clawOpen, Config.clawClose);

        wrist.closeServo();
        claw.closeServo();
    }
    public void move(double speed) {
        motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(speed);
        // Set motor limits
        // TODO: Implement motor limits
    }

    public void moveToPos(int positionTicks, double speed) {
        motor.setTargetPosition(positionTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Config.maxSlideTicksPerSecond * speed);
    }

    public void moveToPos(int positionTicks) {
        moveToPos(positionTicks, Config.defaultSpecimenArmSpeed);
    }

    // Preset movements
    public void movePrepHang(double speed) {
        moveToPos(Config.specimenArmPrepHang, speed);
        wrist.closeServo();
    }

    public void moveHangDown(double speed) {
        moveToPos(Config.specimenArmPostHang, speed);
        wrist.closeServo();
    }

    public void moveToCollect(double speed) {
        moveToPos(Config.specimenArmCollect, speed);
        wrist.openServo();
    }

    // Servo control
    public void openClaw() { claw.openServo(); }
    public void closeClaw() { claw.closeServo(); }
    public void openWrist() { wrist.openServo(); }
    public void closeWrist() { wrist.closeServo(); }

    public void setWristPosBool(boolean pos) { wrist.selectBoolPos(pos); }
    public void setClawPosBool(boolean pos) { claw.selectBoolPos(pos); }

    // Getters
    public int getPositionTicks() {
        return motor.getCurrentPosition();
    }
    public boolean reachedPosition() { return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < Config.specimenArmTolerance; }
}
