package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Config;

public class Collector {
    private final DcMotorEx motor;
    private final ServoClass servo;
    private double speed = .5;

    public Collector(LinearOpMode opMode) {
        motor = opMode.hardwareMap.get(DcMotorEx.class, Config.intakeMotor);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        servo = new ServoClass(opMode.hardwareMap.get(Servo.class, Config.intakeServo), Config.intakeUp, Config.intakeDown, Config.intakeUp);
    }

    public void up() {
        servo.openServo();
    }

    public void down() {
        servo.closeServo();
    }

    public void start() {
        motor.setPower(-speed);
    }

    public void stop() {
        motor.setPower(0);
    }

    public void reverse() {
        motor.setPower(speed * 0.8);
    }

    /**
     * Calls start() or stop() based on boolean value
     *
     * @param run runs collector if true
     */
    public void setRunning(boolean run) {
        if (run) start();
        else stop();
    }

    /**
     * Set default motor speed
     *
     * @param speed double in range [0, 1]
     */
    public void setDefaultSpeed(double speed) {
        this.speed = speed;
    }
}
