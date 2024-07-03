package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Config;

/**
 * Class used to control the slide arm on the robot
 */
public class LinearSlide {
    private final DcMotorEx motor;
    private double defaultSpeed;

    public LinearSlide(LinearOpMode opMode) {
        motor = opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor);
        motor.setTargetPositionTolerance(5);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        defaultSpeed = .7;
    }

    /**
     * Move the linear slide to a specified position at default speed
     * @param position The position to move to, in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     */
    public void moveToPosition(double position) {
        moveToPosition(position, defaultSpeed);
    }

    /**
     * Move the linear slide to a specified position at a specified speed
     * @param position The position to move to, in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     * @param speed The speed to move the slide at, in the range [0, 1]
     */
    public void moveToPosition(double position, double speed) {
        // Convert to ticks and limit to [-10, -3000]
        int positionTicks = Math.min(Math.max((int) (position * -3000), -3000), -10);
        motor.setTargetPosition(positionTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Config.maxSlideTicksPerSecond * speed);
    }

    /**
     * Waits until slide motor is no longer busy, opMode is stopped, or timeout is reached.
     * Updates telemetry with information about slide motor
     */
    public void waitUntilPositionReached(LinearOpMode opMode) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (motor.isBusy() && opMode.opModeIsActive() && timer.seconds() < 5) {
            opMode.telemetry.addData("Arm Position: ", motor.getCurrentPosition());
            opMode.telemetry.addData("Target Position: ", motor.getTargetPosition() / -3000);
            opMode.telemetry.addData("Velocity: ", motor.getVelocity());
        }
    }

    /**
     * Moves the linear slide at a specified speed if the slide is not at a limit
     * @param speed The speed to move the slide at, in the range [-1, 1] where 1 is full speed in the positive direction and -1 is full speed in the negative direction
     */
    public void move(double speed) {
        motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        speed = -speed; // Invert speed because negative power moves the slide out
        if (speed > 0 && motor.getCurrentPosition() < -10) {
            motor.setPower(speed);
        } else if (speed < 0 && motor.getCurrentPosition() > -3000) {
            motor.setPower(speed);
        } else {
            motor.setPower(0);
        }
    }

    /**
     * Get the current position of the motor in encoder ticks
     * @return The current position of the motor in encoder ticks
     */
    public int getPositionTicks() {
        return motor.getCurrentPosition();
    }

    /**
     * Get the current position of the motor in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     * @return The current position of the motor in the range [0, 1]
     */
    public double getPosition() {
        return motor.getCurrentPosition() / -3000.0;
    }

    /**
     * Get the current motor velocity in ticks per second
     *
     * @return the current velocity of the motor
     */
    public double getVelocity() {
        return motor.getVelocity();
    }

    /**
     * Check if the motor is busy moving to a target position (motor will be busy if holding position)
     * @return True if the motor is busy moving to a target position, false otherwise
     */
    public boolean isBusy() {
        return motor.isBusy();
    }

    /**
     * Get if the slide motor is enabled
     *
     * @return boolean
     */
    public boolean isEnergized() {
        return motor.isMotorEnabled();
    }


    /**
     * Set the default motor speed
     *
     * @param newSpeed in range [0, 1]
     */
    public void setDefaultSpeed(double newSpeed) {
        defaultSpeed = newSpeed;
    }

    /**
     * Disable slide motor (turn off whenever possible)
     */
    public void turnOff() {
        motor.setVelocity(0);
        motor.setMotorDisable();
    }

    /**
     * Enable slide motor
     */
    public void turnOn() {
        motor.setMotorEnable();
    }

    /**
     * Calls turnOn() or turnOff based on boolean value
     *
     * @param enable boolean whether to enable motor
     */
    public void setMotorEnabled(boolean enable) {
        if (enable) {
            turnOn();
        } else {
            turnOff();
        }
    }
}
