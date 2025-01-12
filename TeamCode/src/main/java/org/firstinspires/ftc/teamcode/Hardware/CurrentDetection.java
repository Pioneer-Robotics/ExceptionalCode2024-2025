package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentDetection {
    private volatile double current;
    private final double maxCurrent;
    private final DcMotorEx motor;
    Timer timer;
    TimerTask task;
    public CurrentDetection(DcMotorEx motor, double maxCurrent) {
        this.motor = motor;
        this.current = Float.POSITIVE_INFINITY;
        this.maxCurrent = maxCurrent;
    }

    public CurrentDetection(DcMotorEx motor) {
        this(motor, Config.defaultMaxCurrent);
    }

    public void checkCurrent() {
        current = motor.getCurrent(CurrentUnit.MILLIAMPS);
        if (current > maxCurrent) {
            Bot.opMode.telemetry.addLine("MOTOR REACHED MAX CURRENT");
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
            Bot.opMode.gamepad1.rumble(500);
            Bot.opMode.gamepad2.rumble(500);
        }
        Bot.opMode.telemetry.addLine("Checked");
        Bot.opMode.telemetry.update();
    }

    public void checkSlideCurrent() {
        current = motor.getCurrent(CurrentUnit.MILLIAMPS);
        if (current > maxCurrent) {
            Bot.opMode.telemetry.addLine("MOTOR REACHED MAX CURRENT");
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
            Bot.opMode.gamepad1.rumble(500);
            Bot.opMode.gamepad2.rumble(500);
            motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public double getCurrent() {
        return current;
    }

    public void stop() {
        timer.cancel();
    }

    public void start(boolean isSlideArm) {
        this.timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (isSlideArm){
                    checkSlideCurrent();
                } else {
                    checkCurrent();
                }
            }
        };
        timer.schedule(task, 0, 500);
    }
}
