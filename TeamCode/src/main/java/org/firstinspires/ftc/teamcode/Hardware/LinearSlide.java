package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Config;

public class LinearSlide {
    private final DcMotorEx motor;
    private final LinearOpMode opMode;

    public LinearSlide(LinearOpMode opMode) {
        this.opMode = opMode;
        motor = opMode.hardwareMap.get(DcMotorEx.class, Config.slideMotor);
        motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Move the linear slide to a specified position at full speed
     * @param position The position to move to, in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     */
    public void moveToPosition(double position) {
        moveToPosition(position, 1);
    }

    /**
     * Move the linear slide to a specified position at a specified speed
     * @param position The position to move to, in the range [0, 1] where 0 is fully retracted and 1 is fully extended
     * @param speed The speed to move the slide at, in the range [-1, 1] where 1 is full speed in the positive direction and -1 is full speed in the negative direction
     */
    public void moveToPosition(double position, double speed) {
        // Convert position to encoder ticks (-3000 is fully extended and 0 is fully retracted)
        int targetPosition = (int) (-3000 * position);
        targetPosition = Math.max(10, targetPosition); // Prevent the motor from moving to 0 and getting stuck

        while (!opMode.isStopRequested() && Math.abs(motor.getCurrentPosition() - targetPosition) > 10) {
            motor.setPower(speed);
        }

        motor.setPower(0);
    }

    /**
     * Moves the linear slide at a specified speed if the slide is not at a limit
     * @param speed The speed to move the slide at, in the range [-1, 1] where 1 is full speed in the positive direction and -1 is full speed in the negative direction
     */
    public void move(double speed) {
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
     * Check if the motor is busy moving to a target position (motor will be busy if holding position)
     * @return True if the motor is busy moving to a target position, false otherwise
     */
    public boolean isBusy() {
        return motor.isBusy();
    }
}
