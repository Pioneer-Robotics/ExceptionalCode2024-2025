package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeDrive.FakeDcMotorEx;

public class MecanumBase {
    private final DcMotorEx LF, LB, RF, RB;
    private boolean northMode = false;
    private final boolean isUnitTest;

    public MecanumBase(boolean isUnitTest) {
        this.isUnitTest = isUnitTest;
        if (isUnitTest) {
            // Set up motors
            RF = new FakeDcMotorEx();
            LF = new FakeDcMotorEx();
            RB = new FakeDcMotorEx();
            LB = new FakeDcMotorEx();
        } else {
            // Set up motors
            RF = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorRF);
            LF = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorLF);
            RB = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorRB);
            LB = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.motorLB);
        }

        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);
        RF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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
            double theta = Bot.pinpoint.getHeading();
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

        RF.setVelocity(powerRF * Config.maxDriveTicksPerSecond * speed + (Math.signum(powerRF) * Config.fRF * Config.maxDriveTicksPerSecond));
        LF.setVelocity(powerLF * Config.maxDriveTicksPerSecond * speed + (Math.signum(powerLF) * Config.fLF * Config.maxDriveTicksPerSecond));
        RB.setVelocity(powerRB * Config.maxDriveTicksPerSecond * speed + (Math.signum(powerRB) * Config.fRB * Config.maxDriveTicksPerSecond));
        LB.setVelocity(powerLB * Config.maxDriveTicksPerSecond * speed + (Math.signum(powerLB) * Config.fLB * Config.maxDriveTicksPerSecond));
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

    public double[] getEncoders() {
        return new double[] {
                RF.getCurrentPosition(),
                LF. getCurrentPosition(),
                RB.getCurrentPosition(),
                LB.getCurrentPosition()
        };
    }

    public DcMotorEx getLF() {return LF;}
    public DcMotorEx getLB() {return LB;}
    public DcMotorEx getRF() {return RF;}
    public DcMotorEx getRB() {return RB;}
}
