package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeDcMotorEx;
import org.opencv.core.Mat;

public class SlideArm {
    private final DcMotorEx motor1, motor2;
    private final boolean isUnitTest;

    public SlideArm(boolean isUnitTest) {
        this.isUnitTest = isUnitTest;
        if (isUnitTest) {
            motor1 = new FakeDcMotorEx();
            motor2 = new FakeDcMotorEx();
        } else {
            motor1 = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor1);
            motor2 = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor2);
        }

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

    // Private methods
    private void moveToPositionTicks(int ticks, double speed) {
        motor1.setTargetPosition(-ticks);
        motor2.setTargetPosition(ticks);
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // FIXME: Isn't velocity always positive? I changed motor 2 to be positive for now.
        motor1.setVelocity(-Config.maxSlideTicksPerSecond * speed);
        motor2.setVelocity(Config.maxSlideTicksPerSecond * speed);
    }

    private void moveToPositionTicks(int ticks) {
        moveToPositionTicks(ticks, Config.defaultSlideSpeed);
    }

    // Public API Methods
    public void move(double power) {
        if (!motor1.isMotorEnabled()) { motor1On(); }
        if (!motor2.isMotorEnabled()) { motor2On(); }
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // FIXME: Isn't velocity always positive? I changed motor 2 to be positive for now.
        motor1.setVelocity(-power * Config.maxSlideTicksPerSecond);
        motor2.setVelocity(power * Config.maxSlideTicksPerSecond);
    }

    public void moveDown(double speed) {
        // FIXME: I think this is the correct logic
        if (getArmPositionMotor1() < (Config.slideDown + 50.0)) {
            motor1Off();
            motor2Off();
            return;
        }
        if (getArmPositionMotor2() < (Config.slideDown + 50.0)) {
            motor1Off();
            motor2Off();
            return;
        }

        if (!motor1.isMotorEnabled()) { motor1On(); }
        if (!motor2.isMotorEnabled()) { motor2On(); }
        moveToPositionTicks(Config.slideDown, speed);
    }

    public void moveMid(double speed) {
        if (!motor1.isMotorEnabled()) { motor1On(); }
        if (!motor2.isMotorEnabled()) { motor2On(); }
        moveToPositionTicks(Config.slideLowBasket, speed);
    }

    public void moveUp(double speed) {
        if (!motor1.isMotorEnabled()) { motor1On(); }
        if (!motor2.isMotorEnabled()) { motor2On(); }
        moveToPositionTicks(Config.slideHighBasket, speed);
    }

    public void motor1Off() {
        motor1.setPower(0.0);
        motor1.setVelocity(0.0);
        motor1.setMotorDisable();
    }

    public void motor2Off() {
        motor2.setPower(0.0);
        motor2.setVelocity(0.0);
        motor2.setMotorDisable();
    }

    public void motor1On() {
        motor1.setMotorEnable();
    }
    public void motor2On() {
        motor2.setMotorEnable();
    }

    // Testing Only Methods
    public void setTestingOnlyMotor1Position(int position) {
        if (isUnitTest) {
            FakeDcMotorEx testMotor = (FakeDcMotorEx)motor1;
            testMotor.setCurrentPosition(-position);
        }
    }

    public void setTestingOnlyMotor2Position(int position) {
        if (isUnitTest) {
            FakeDcMotorEx testMotor = (FakeDcMotorEx) motor2;
            testMotor.setCurrentPosition(position);
        }
    }

    // Getters
    public double getArmPositionMotor1() {
        return(-motor1.getCurrentPosition());
    }
    public double getArmPositionMotor2() {
        return(motor2.getCurrentPosition());
    }
    public DcMotorEx getMotor1() {
        return(motor1);
    }
    public DcMotorEx getMotor2() {
        return(motor2);
    }

    public boolean reachedPosition() {
        // FIXME: - This line may be too unspecific
        return Math.abs(motor1.getTargetPosition() - motor1.getCurrentPosition()) < 10;
    };
}
