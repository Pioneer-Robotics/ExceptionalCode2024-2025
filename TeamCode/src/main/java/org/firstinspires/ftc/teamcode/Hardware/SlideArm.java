package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.opencv.core.Mat;

public class SlideArm {
    private final DcMotorEx motor1, motor2;

    public SlideArm() {
        motor1 = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor1);
        motor2 = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor2);

        //Motor 1 is non OCG box arm
        //Motor 2 is OCG box arm
        motor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void moveToPositionTicks(int ticks, double speed) {
        motor1.setTargetPosition(-ticks);
        motor2.setTargetPosition(ticks);
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor1.setVelocity(-Config.maxSlideTicksPerSecond * speed);
        motor2.setVelocity(-Config.maxSlideTicksPerSecond * speed);
    }

    public void moveToPositionTicks(int ticks) {
        moveToPositionTicks(ticks, Config.defaultSlideSpeed);
    }

    public void moveDown(double speed) {
        moveToPositionTicks(Config.slideDown, speed);
        if (getArmPosition() < (Config.slideDown + 50.0)) { motorOff(); }
        //TODO: Need to update arm position method
    }

    public void moveMid(double speed) {
        if (!motor1.isMotorEnabled()) { motorOn(); }
        moveToPositionTicks(Config.slideLowBasket, speed);
    }

    public void moveUp(double speed) {
        if (!motor1.isMotorEnabled()) { motorOn(); }
        moveToPositionTicks(Config.slideHighBasket, speed);
    }


    public void motorOff() {
        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor1.setMotorDisable();
        motor2.setMotorDisable();
    }
    public void motorOn() {
        motor1.setMotorEnable();
        motor2.setMotorEnable();
    }

    public void move(double power) {
        if (!motor1.isMotorEnabled() || !motor2.isMotorEnabled()) { motorOn(); }
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor1.setVelocity(-power * Config.maxSlideTicksPerSecond);
        motor2.setVelocity(-power * Config.maxSlideTicksPerSecond);
    }

    // Getters
    public double getArmPosition() {
        return(motor1.getCurrentPosition());
    }
    public DcMotorEx getMotor1() {
        return(motor1);
    }
    public DcMotorEx getMotor2() {
        return(motor2);
    }

    public boolean reachedPosition() {
        return Math.abs(motor1.getTargetPosition() - motor1.getCurrentPosition()) < 10;
    };
}
