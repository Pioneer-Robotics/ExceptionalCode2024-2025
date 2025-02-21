package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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

        // Normalize movement vector
        double moveMag = Math.hypot(x, y);
        if (moveMag > 1) {
            x /= moveMag;
            y /= moveMag;
            moveMag = 1;
        }

        // Apply speed to movement
        x *= speed;
        y *= speed;

        // Limit turn to available power
        double remainingPower = Math.max(0, 1 - speed * moveMag);
        turn = Math.signum(turn) * Math.min(Math.abs(turn), remainingPower);

        // Ensure minimum turn of 0.5 if any turn is requested
        if (Math.abs(turn) > 0 && Math.abs(turn) < 0.5) {
            double scale = 0.5 / Math.abs(turn);
            turn *= scale;
            x /= scale;
            y /= scale;
        }

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
        double powerRF = (y - x - turn) / denominator;
        double powerLF = (y + x + turn) / denominator;
        double powerRB = (y + x - turn) / denominator;
        double powerLB = (y - x + turn) / denominator;

        double MDTPS = Config.maxDriveTicksPerSecond;

        RF.setVelocity(powerRF * MDTPS + (Math.signum(powerRF) * Config.fRF * MDTPS));
        LF.setVelocity(powerLF * MDTPS + (Math.signum(powerLF) * Config.fLF * MDTPS));
        RB.setVelocity(powerRB * MDTPS + (Math.signum(powerRB) * Config.fRB * MDTPS));
        LB.setVelocity(powerLB * MDTPS + (Math.signum(powerLB) * Config.fLB * MDTPS));
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
