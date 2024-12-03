package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class MecanumBase {
    private final DcMotorEx LF, LB, RF, RB;
    private boolean northMode = false;
    public MecanumBase() {
        // Set up motors
        RF = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorRF);
        LF = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorLF);
        RB = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorRB);
        LB = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorLB);

        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);
        RF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Set the zero power behavior of the motors.
     * @param behavior ZeroPowerBehavior
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        RF.setZeroPowerBehavior(behavior);
        LF.setZeroPowerBehavior(behavior);
        RB.setZeroPowerBehavior(behavior);
        LB.setZeroPowerBehavior(behavior);
    }

    /**
     *
     * @param x x vector [-1,1]
     * @param y y vector [-1,1]
     * @param turn turn vector [-1,1]
     * @param speed Master multiplier
     */
    public void move(double x, double y, double turn, double speed) {
        if (northMode) {
            // Rotate x and y
            double theta = -Bot.imu.getRadians();
            double tempX = x * Math.cos(theta) - y * Math.sin(theta);
            double tempY = x * Math.sin(theta) + y * Math.cos(theta);
            x = tempX;
            y = tempY;
        }
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
        double powerRF = (y - x - turn) / denominator;
        double powerLF = (y + x + turn) / denominator;
        double powerRB = (y + x - turn) / denominator;
        double powerLB = (y - x + turn) / denominator;

        RF.setVelocity(powerRF * Config.maxDriveTicksPerSecond * speed);
        LF.setVelocity(powerLF * Config.maxDriveTicksPerSecond * speed);
        RB.setVelocity(powerRB * Config.maxDriveTicksPerSecond * speed);
        LB.setVelocity(powerLB * Config.maxDriveTicksPerSecond * speed);
    }

    public void stop() {
        RF.setVelocity(0);
        LF.setVelocity(0);
        RB.setVelocity(0);
        LB.setVelocity(0);
    }

    public void setNorthMode(boolean northMode) {
        this.northMode = northMode;
    }
}
