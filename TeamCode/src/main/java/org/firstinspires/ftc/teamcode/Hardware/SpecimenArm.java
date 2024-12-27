package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class SpecimenArm {
    private final DcMotorEx motor;
    ServoClass claw;
    int position = 0;

    public SpecimenArm() {
        motor = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.specimenArmMotor);
        motor.setTargetPositionTolerance(5);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Servos
        claw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.clawServo), Config.clawOpen, Config.clawClose);

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
        position = 0;
    }

    public void movePostHang(double speed) {
        moveToPos(Config.specimenArmPostHang, speed);
        position = 1;
    }

    public void moveToCollect(double speed) {
        moveToPos(Config.specimenArmCollect, speed);
        position = 2;
    }

    public int getPosition() {
        return position;
    }

    public void movePrepHangUp(double speed) {
        moveToPos(Config.specimenArmPrepHangUp, speed);
    }

    public void movePostHangUp(double speed) {
        moveToPos(Config.specimenArmPostHangUp, speed);
    }

    public void moveToIdle() { moveToPos(-15); }

    // Servo control
    public void openClaw() { claw.openServo(); }
    public void closeClaw() { claw.closeServo(); }

    public void setClawPosBool(boolean pos) { claw.selectBoolPos(pos); }

    // Getters
    public int getPositionTicks() {
        return motor.getCurrentPosition();
    }
    public boolean reachedPosition() { return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < Config.specimenArmTolerance; }

    public void homeArm() {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(-0.15);
        Bot.mecanumBase.stop();
        while (motor.getCurrent(CurrentUnit.MILLIAMPS) < 1000) {}
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public DcMotorEx getMotor() {
        return motor;
    }
}
