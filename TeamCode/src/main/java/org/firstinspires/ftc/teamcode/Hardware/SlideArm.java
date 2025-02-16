package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;

public class SlideArm {
    private final DcMotorEx motor1, motor2;
    private final PID pid;
    private int targetPosition = 0;
    private double kP = Config.slidePIDF[0];
    private double kI = Config.slidePIDF[1];
    private double kD = Config.slidePIDF[2];
    private double kF = Config.slidePIDF[3];

    public SlideArm() {
        motor1 = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor1);
        motor2 = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor2);

        //Motor 1 is non OCG box arm
        //Motor 2 is OCG box arm
        motor1.setDirection(DcMotorEx.Direction.REVERSE);
        motor2.setDirection(DcMotorEx.Direction.REVERSE);
        motor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        pid = new PID(kP, kI, kD);
    }

    public void setPIDFCoefficients(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;

        pid.setPIDCoefficients(kP, kI, kD);
    }

    public void setTargetPosition(int ticks, double speed) {
        targetPosition = ticks;
    }

    public void setTargetPosition(int ticks) {
        setTargetPosition(ticks, Config.defaultSlideSpeed);
    }

    public void update() {
        double currentPosition = getArmPosition();
        double power = pid.calculate(currentPosition, targetPosition);
        if (targetPosition > Config.slideDown + 50 || currentPosition > Config.slideDown + 50) {
            move(power);
        } else {
            motorOff();
        }
    }

    public void moveDown(double speed) {
        setTargetPosition(Config.slideDown, speed);
    }

    public void moveMid(double speed) {
        setTargetPosition(Config.slideLowBasket, speed);
    }

    public void moveUp(double speed) {
        setTargetPosition(Config.slideHighBasket, speed);
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
        power += kF;
        motor1.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor1.setVelocity(power * Config.maxSlideTicksPerSecond);
        motor2.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor2.setVelocity(power * Config.maxSlideTicksPerSecond);
    }

    // Getters
    public double getArmPosition() {
        return Math.round((float) (motor1.getCurrentPosition() + motor2.getCurrentPosition()) / 2);
    }
    public DcMotorEx getMotor1() {
        return(motor1);
    }
    public DcMotorEx getMotor2() {
        return(motor2);
    }

    public void resetEncoders() {
        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
    }
}
