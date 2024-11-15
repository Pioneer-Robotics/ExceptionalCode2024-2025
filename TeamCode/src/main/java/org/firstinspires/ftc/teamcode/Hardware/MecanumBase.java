package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class MecanumBase {
    private final DcMotorEx LF, LB, RF, RB;
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
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
        double powerRF = (y - x - turn) / denominator;
        double powerLF = (y + x + turn) / denominator;
        double powerRB = (y + x - turn) / denominator;
        double powerLB = (y - x + turn) / denominator;

        RF.setVelocity(powerRF * Config.maxDriveTicksPerSecond);
        LF.setVelocity(powerLF * Config.maxDriveTicksPerSecond);
        RB.setVelocity(powerRB * Config.maxDriveTicksPerSecond);
        LB.setVelocity(powerLB * Config.maxDriveTicksPerSecond);
    }
}
