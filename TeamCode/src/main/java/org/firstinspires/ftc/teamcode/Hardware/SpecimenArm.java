package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeDrive.FakeDcMotorEx;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeDrive.FakeServo;

import java.util.Timer;
import java.util.TimerTask;

public class SpecimenArm {
    private final DcMotorEx motor;
    public ServoClass claw, specimenWrist;
    public int position = 0;
    private final boolean isUnitTest;

    public SpecimenArm(boolean isUnitTest) {
        this.isUnitTest = isUnitTest;
        if (isUnitTest) {
            motor = new FakeDcMotorEx();

            claw = new ServoClass(new FakeServo(), Config.clawOpen, Config.clawClose);
            specimenWrist = new ServoClass(new FakeServo(), Config.specWristCollect, Config.specWristHang);
        } else {
            motor = Bot.opMode.hardwareMap.get(DcMotorEx.class, Config.specimenArmMotor);

            // Servos
            claw = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.clawServo), Config.clawOpen, Config.clawClose);
            specimenWrist = new ServoClass(Bot.opMode.hardwareMap.get(Servo.class, Config.specimenWristServo), Config.specWristCollect, Config.specWristHang);
        }

        motor.setTargetPositionTolerance(5);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw.closeServo();
    }
    public void move(double speed) {
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        motor.setVelocity(speed * Config.maxSpecTicksPerSecond);
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
    //Ram hang position
    public void movePrepHang(double speed) {
        move(speed);
        startEndStopThread();
        position = 0;
    }

    public void movePostHang(double speed) {
//        moveToPos(Config.specimenArmPostHang, speed);
        position = 1;
    }

    public void moveToCollect(double speed) {
//        move(speed);
        move(-speed);
//        moveToPos(Config.specimenCollect, speed);
        wristCollect();
        position = 2;
    }

    public int getPosition() {
        return position;
    }

    public void movePostHangUp(double speed) {
//        moveToPos(Config.specimenArmPostHangUp, speed);
    }

    public void moveToIdle() { moveToPos(-15); }

    // Servo control
    public void openClaw() { claw.openServo(); }
    public void closeClaw() { claw.closeServo(); }
    public void wristCollect() { specimenWrist.openServo(); }
    public void wristHang() { specimenWrist.closeServo(); }

    public void setClawPosBool(boolean pos) { claw.selectBoolPos(pos); }

    // Getters
    public int getPositionTicks() {
        return motor.getCurrentPosition();
    }
    public boolean reachedPosition() { return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < Config.specimenArmTolerance; }
    public DcMotorEx getMotor() {
        return motor;
    }

    public void startEndStopThread() {
        if (Bot.isUnitTest) {
            return;
        }

        Timer timer = new Timer();
        ElapsedTime elapsedTime = new ElapsedTime();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (elapsedTime.seconds() > 0.5) {
                    wristHang();
                }
                if (Bot.specimenEndStop.getVoltage() < 0.5 || !Bot.opMode.opModeIsActive()) {
                    motor.setPower(0);
                    motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.cancel();
                }
            }
        };
        elapsedTime.reset();
        timer.schedule(task, 0, 10);
    }
}
