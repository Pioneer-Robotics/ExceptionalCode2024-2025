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
    private final boolean isSpecimenArm;
    private final boolean isSlideArm;
    private final DcMotorEx motor;
    Timer timer;
    TimerTask task;

    public CurrentDetection(DcMotorEx motor, double maxCurrent, boolean isSpecimenArm, boolean isSlideArm) {
        this.motor = motor;
        this.isSpecimenArm = isSpecimenArm;
        this.isSlideArm = isSlideArm;
        this.current = Float.POSITIVE_INFINITY;
        this.maxCurrent = maxCurrent;
    }

    public CurrentDetection(DcMotorEx motor, double maxCurrent) {
        this(motor, maxCurrent, false, false);
    }

    public CurrentDetection(DcMotorEx motor) {
        this(motor, Config.defaultMaxCurrent, false, false);
    }

    public void checkCurrent() {
        current = motor.getCurrent(CurrentUnit.MILLIAMPS);
        if (current > maxCurrent) {
            Bot.opMode.telemetry.addLine("MOTOR REACHED MAX CURRENT");
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//            motor.setPower(0);
            Bot.opMode.gamepad1.rumble(500);
            Bot.opMode.gamepad2.rumble(500);
        }
    }

    public void checkSpecimenCurrent() {
//        Bot.dashboardTelemetry.addData("Specimen Arm Pos", motor.getCurrentPosition());
//        Bot.dashboardTelemetry.addData("Diff to pos1", Math.abs(Config.specimenArmCollect1 - motor.getCurrentPosition()));
//        Bot.dashboardTelemetry.addData("Diff to pos2", Math.abs(Config.specimenArmCollect2 - motor.getCurrentPosition()));

//        Bot.dashboardTelemetry.update();
        current = motor.getCurrent(CurrentUnit.MILLIAMPS);
        // Virtual touch sensor
        if (current > maxCurrent) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (current > 8000) {
            Bot.opMode.telemetry.addLine("MOTOR REACHED MAX CURRENT");
            Bot.opMode.gamepad1.rumble(500);
            Bot.opMode.gamepad2.rumble(500);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(0);
        }
    }

    public void checkSlideCurrent() {
        current = motor.getCurrent(CurrentUnit.MILLIAMPS);
        if (current > maxCurrent) {
            Bot.opMode.telemetry.addLine("MOTOR REACHED MAX CURRENT");
            Bot.opMode.gamepad1.rumble(500);
            Bot.opMode.gamepad2.rumble(500);
            motor.setPower(0);
        }
    }

    public double getCurrent() {
        return current;
    }

    public void stop() {
        timer.cancel();
    }

    public void start() {
        this.timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (isSlideArm) {
                    checkSlideCurrent();
                } if (isSpecimenArm) {
                    checkSpecimenCurrent();
                } else {
                    checkCurrent();
                }
//                Bot.opMode.telemetry.addLine("Checked");
//                Bot.opMode.telemetry.update();
                if(Bot.opMode.isStopRequested()) {
                    stop();
                }
            }
        };
        timer.schedule(task, 100, 250);
    }
}
