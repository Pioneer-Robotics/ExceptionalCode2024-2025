package org.firstinspires.ftc.teamcode.SelfDrivingAuto;


import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;

/**
 * PID controller class
 */
public class PID {
    private double kP, kI, kD;
    private double integral, prevError;
    private boolean haltIntegral; // Stop the integral from accumulating if the system is moving at 100% to prevent windup

    /**
     * Constructor for PID controller
     * @param kP Proportional constant
     * @param kI Integral constant
     * @param kD Derivative constant
     */
    public PID (double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        integral = 0;
        prevError = 0;
        haltIntegral = false;
    }

    /**
     * Constructor for PID controller with initial error
     * @param kP Proportional constant
     * @param kI Integral constant
     * @param kD Derivative constant
     * @param initialError Initial error
     */
    public PID (double kP, double kI, double kD, double initialError) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        integral = 0;
        prevError = initialError;
        haltIntegral = false;
    }

    public void setPIDCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Calculate the move value based on the current and target values
     *
     * @param current Current value
     * @param target  Target value
     * @return Move value in range -1 to 1
     */
    public double calculate(double current, double target) {
        return calculate(current, target, false);
    }

    /**
     * Calculate the move value based on the current and target values
     * @param current Current value
     * @param target Target value
     * @param normalizeError Used for dealing with wrapping angles such as -pi to pi
     * @return Move value in range -1 to 1
     */
    public double calculate(double current, double target, boolean normalizeError) {
        // Calculate error
        double error = target - current;

        // Normalize error [-pi to pi] if flag is true
        if (normalizeError) {
            error = AngleUtils.normalizeRadians(error); }

        // Calculate derivative
        double derivative = error - prevError;

        // Update integral
        if (!haltIntegral) {
            integral += error;
        }

        // Calculate move
        double move = kP * error + kI * integral + kD * derivative;

        // Update previous error
        prevError = error;

        // Normalize move to be between -1 and 1
        // If the system is moving at 100%, stop the integral to prevent windup
        if (move > 1) {
            move = 1;
            haltIntegral = true;
        } else if (move < -1) {
            move = -1;
            haltIntegral = true;
        } else {
            haltIntegral = false;
        }

        // Return the move
        return move;
    }

    /**
     * Get the previous error value
     * @return Previous error value
     */
    public double getError() {
        return prevError;
    }
}

