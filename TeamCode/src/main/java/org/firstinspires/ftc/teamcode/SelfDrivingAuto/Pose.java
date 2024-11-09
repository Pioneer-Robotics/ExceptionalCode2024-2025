/**
This class is used to calculate the robots current position on the field with 3 values, an X coordinate, Y coordinate, and Theta(Robot Angle).
 */

package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.Odometry;
import org.firstinspires.ftc.teamcode.Hardware.SparkfunOTOS;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;
import org.firstinspires.ftc.teamcode.Helpers.KalmanFilter;

/**
 * This class is used to calculate the robots current position on the field.
 * It uses a kalman filter to combine data from the odometers and OTOS
 * The robot's position is stored as an X coordinate, Y coordinate, and Theta (Robot Yaw).
 */
// TODO: Clean up pose class
// Probably not going to use kalman filter
public class Pose{
    private double x, y, theta;
    private KalmanFilter kalmanFilter;
    private final Odometry odometry;
    private final SparkfunOTOS otos;

    public Pose() {
        this(new double[]{0, 0, 0});
    }

    /**
     * Constructor for Pose with starting position
     *
     * @param startPos double[] - starting position of the robot {x, y, theta}
     */
    public Pose(double[] startPos) {
        x = startPos[0];
        y = startPos[1];
        theta = startPos[2];

        // Set up sensors
        odometry = new Odometry(startPos);
        otos = new SparkfunOTOS(startPos);

        // Set up kalman filter
        initKalmanFilter(startPos);
    }

    public void calculate(){
        // Predict the next state
        MatrixF u = new GeneralMatrixF(3, 1, new float[]{0, 0, 0}); // No control input
        kalmanFilter.predict(u);

        // Update the odometry and OTOS sensors
        odometry.calculate();
        double[] odometerPose = odometry.returnPose();
        double[] otosPose = otos.getPose();

        // Create matrices for the odometer and OTOS data
        MatrixF Z_odom = new GeneralMatrixF(3, 1, new float[]{(float)odometerPose[0], (float)odometerPose[1], (float)odometerPose[2]});
        MatrixF Z_OTOS = new GeneralMatrixF(3, 1, new float[]{(float)otosPose[0], (float)otosPose[1], (float)otosPose[2]});

        // Update the kalman filter with the new data
        kalmanFilter.updateOdom(Z_odom);
        kalmanFilter.updateOTOS(Z_OTOS);

        // Get the new state estimate from the kalman filter
        float[] state = kalmanFilter.getStateArray();
        x = state[0];
        y = state[1];
        theta = state[2];
    }

    public double getY() {
        return (y);
    }

    public double getX() {
        return (x);
    }

    public double getTheta() {
        return (theta);
    }

    /**
     * Get the raw odometer data
     * @return double[] {x, y, theta}
     */
    public double[] getRawOdometer() {
        return odometry.returnPose();
    }

    public double[] getRawOTOS() {
        return otos.getPose();
    }

    public double[] getVelocityOTOS() {return otos.getVelocity();}

    // Enable or disable each sensor
    // Useful for testing or if a sensor is broken/inaccurate
    public void disableOdometer() {
        MatrixF H_odom = new GeneralMatrixF(3, 3, new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        kalmanFilter.setHOdom(H_odom);
    }

    public void disableOTOS() {
        MatrixF H_OTOS = new GeneralMatrixF(3, 3, new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0});
        kalmanFilter.setHOTOS(H_OTOS);
    }

    public void enableOdometer() {
        MatrixF H_odom = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        kalmanFilter.setHOdom(H_odom);
    }

    public void enableOTOS() {
        MatrixF H_OTOS = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        kalmanFilter.setHOTOS(H_OTOS);
    }

    /**
     * Returns the current x, y, and theta values of the robot
     * Calls calculate() to update the values
     * @return double[] {x, y, theta}
     */
    public double[] returnPose(){
        calculate();
        return (new double[]{x, y, theta});
    }

    private void initKalmanFilter(double[] startPos) {
        // X - Initial state estimate - 3x1 matrix - (x, y, z)
        MatrixF X = new GeneralMatrixF(3, 1, new float[]{(float)startPos[0], (float)startPos[1], (float)startPos[2]});
        // P - Initial error covariance - 3x3 diagonal matrix - uncertainty in the initial state
        MatrixF P = new GeneralMatrixF(3, 3, new float[]{0.01f, 0, 0, 0, 0.01f, 0, 0, 0, 0.01f});
        // A - State transition matrix - 3x3 diagonal matrix
        MatrixF A = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        // B - Control input matrix - 3x1 matrix - no control input
        MatrixF B = new GeneralMatrixF(3, 1, new float[]{0, 0, 0});
        // Q - Process noise covariance - 3x3 diagonal matrix - how much we trust the model
        MatrixF Q = new GeneralMatrixF(3, 3, new float[]{0.005f, 0, 0, 0, 0.005f, 0, 0, 0, 0.005f});
        // R Odometer - Measurement noise covariance for odometers - 3x3 diagonal matrix - how much we trust the measurements
        MatrixF R_odom = new GeneralMatrixF(3, 3, new float[]{0.5f, 0, 0, 0, 0.5f, 0, 0, 0, 0.5f});
        // H Odometer - Measurement matrix - 3x3 diagonal matrix - how we map the measurements to the state
//        MatrixF H_odom = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        MatrixF H_odom = new GeneralMatrixF(3, 3, new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0}); // Don't include the odometer in position measurements
        // R OTOS - Measurement noise covariance for OTOS - 3x3 diagonal matrix - how much we trust the measurements
        MatrixF R_OTOS = new GeneralMatrixF(3, 3, new float[]{0.5f, 0, 0, 0, 0.5f, 0, 0, 0, 0.5f});
        // H OTOS - Measurement matrix - 3x3 diagonal matrix - how we map the measurements to the state
        MatrixF H_OTOS = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});

        kalmanFilter = new KalmanFilter(X, P, A, B, Q, R_odom, H_odom, R_OTOS, H_OTOS);
    }
}
