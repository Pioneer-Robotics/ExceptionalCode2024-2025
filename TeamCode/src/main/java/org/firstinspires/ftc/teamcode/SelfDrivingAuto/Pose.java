/**
This class is used to calculate the robots current position on the field with 3 values, an X coordinate, Y coordinate, and Theta(Robot Angle).
 */

package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.Camera.AprilTagProcessor;
import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;
import org.firstinspires.ftc.teamcode.Helpers.KalmanFilter;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * This class is used to calculate the robots current position on the field.
 * It uses the odometer encoders to calculate the robots position.
 * The robot's position is stored as an X coordinate, Y coordinate, and Theta (Robot Yaw).
 */
public class Pose{
    private final Dictionary<Integer, double[]> aprilTagPos = new Hashtable<>(); // April tag id to x, y position
    Bot bot;
    LinearOpMode opMode;
    DcMotorEx odoLeft, odoRight, odoCenter;
    AprilTagProcessor aprilTagProcessor;
    KalmanFilter kalmanFilter;
    private double prevLeftTicks, prevRightTicks, prevCenterTicks;
    private double xOdom, yOdom, thetaOdom;

    /**
     * Constructor for Pose
     *
     * @param opMode LinearOpMode
     */
    public Pose(LinearOpMode opMode, Bot bot) {
        // Set up Kalman Filter
        initKalmanFilter();

        // Set up odometers
        odoLeft = opMode.hardwareMap.get(DcMotorEx.class, Config.odoLeft);
        odoRight = opMode.hardwareMap.get(DcMotorEx.class, Config.odoRight);
        odoCenter = opMode.hardwareMap.get(DcMotorEx.class, Config.odoCenter);

        odoLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odoCenter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        this.opMode = opMode;
        this.bot = bot;
        this.aprilTagProcessor = new AprilTagProcessor(bot.webcam);

        xOdom = yOdom = thetaOdom = 0;
    }

    /**
     * This method updates the current position of the robot on the field by using delta values from the odometers
     */
    public void calculateRawOdom() {
        // Odo readings
        double curLeftTicks = -odoLeft.getCurrentPosition();
        double curRightTicks = -odoRight.getCurrentPosition();
        double curCenterTicks = -odoCenter.getCurrentPosition();

        // Calculate the change in odometers
        double dLeftCM = (curLeftTicks - prevLeftTicks) * Config.ticsToCM;
        double dRightCM = (curRightTicks - prevRightTicks) * Config.ticsToCM;
        double dCenterCM = (curCenterTicks - prevCenterTicks) * Config.ticsToCM;

        // Calculate the change in angle
        double dTheta = (dLeftCM - dRightCM) / Config.trackWidth;

        double centerDisplacement = (dLeftCM + dRightCM) / 2;
        double horizontalDisplacement = dCenterCM - (Config.forwardOffset * dTheta);

        // Calculate the change in x and y
        double a, b;
        if (dTheta != 0) {
            a = (Math.sin(dTheta) / dTheta) * centerDisplacement + ((Math.cos(dTheta) - 1) / dTheta) * horizontalDisplacement;
            b = ((1 - Math.cos(dTheta)) / dTheta) * centerDisplacement + (Math.sin(dTheta) / dTheta) * horizontalDisplacement;
        } else {
            a = centerDisplacement;
            b = horizontalDisplacement;
        }

        double dX = Math.sin(thetaOdom) * a + Math.cos(thetaOdom) * b;
        double dY = Math.cos(thetaOdom) * a - Math.sin(thetaOdom) * b;

        // Update the current x, y, and theta values
        xOdom += dX;
        yOdom += dY;
        thetaOdom += dTheta;

        // Normalize theta [-pi to pi]
        thetaOdom = AngleUtils.normalizeRadians(thetaOdom);

        // Set the previous values to the current values
        prevLeftTicks = curLeftTicks;
        prevRightTicks = curRightTicks;
        prevCenterTicks = curCenterTicks;
    }

    public double getXTag() {
        return (aprilTagProcessor.getXYZ()[0]);
    }

    public double getYTag() {
        return (aprilTagProcessor.getXYZ()[1]);
    }

    public double getZTag() {
        return (aprilTagProcessor.getXYZ()[2]);
    }

    public int getTagID() {
        return (aprilTagProcessor.getTagID()[0]);
    }

    public double getYOdom() {
        return (yOdom);
    }

    public double getXOdom() {
        return (xOdom);
    }

    public double getThetaOdom() {
        return (thetaOdom);
    }

    /**
     * Returns the current x, y, and theta values of the robot using the Kalman Filter
     * @return double[] {x, y, theta}
     */
    public double[] getPose() {
        // Update the raw odometer values
        calculateRawOdom();

        // Predict the state (no control input)
        kalmanFilter.predict(new GeneralMatrixF(1, 1, new float[]{0}));

        // Update the state estimate with the odometer measurements and the vision measurements
        MatrixF zOdom = new GeneralMatrixF(3, 1, new float[]{(float) xOdom, (float) yOdom, (float) thetaOdom});
        MatrixF zVision = new GeneralMatrixF(3, 1, new float[]{(float) getXTag(), (float) getYTag(), (float) getZTag()});

        // Always update the odometer
        kalmanFilter.updateOdom(zOdom);

        // Only update the vision if a tag is detected and the tag is in the dictionary
        if (getXTag() != 0 || getYTag() != 0 || getZTag() != 0 || aprilTagPos.get(getTagID()) != null) {
            // Convert april tag pos from local to global
            double[] tagPos = aprilTagPos.get(getTagID());
            double xTag = tagPos[0] + getXTag();
            double yTag = tagPos[1] + getYTag();
            zVision = new GeneralMatrixF(3, 1, new float[]{(float) xTag, (float) yTag, (float) getZTag()});
            kalmanFilter.updateVision(zVision);
        }

        // Get the current state estimate
        float[] state = kalmanFilter.getStateArray();
        return new double[]{state[0], state[1], state[2]};
    }


    private void initKalmanFilter() {
        // X - Initial state estimate - 3x1 matrix - (x, y, z)
        MatrixF X = new GeneralMatrixF(3, 1, new float[]{0, 0, 0});
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
        MatrixF H_odom = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        // R Vision - Measurement noise covariance for vision - 3x3 diagonal matrix - how much we trust the measurements
        MatrixF R_vision = new GeneralMatrixF(3, 3, new float[]{0.5f, 0, 0, 0, 0.5f, 0, 0, 0, 0.5f});
        // H Vision - Measurement matrix - 3x3 diagonal matrix - how we map the measurements to the state
        // Don't include the z value in the vision measurements
        MatrixF H_vision = new GeneralMatrixF(3, 3, new float[]{1, 0, 0, 0, 1, 0, 0, 0, 0});

        kalmanFilter = new KalmanFilter(X, P, A, B, Q, R_odom, H_odom, R_vision, H_vision);
    }

    /**
     * Set the position of the april tag relative to the robot origin/start pos in the x and y direction
     *
     * @param id The id of the april tag
     * @param x  The x position of the april tag relative to the robot start pos (0,0) (in cm)
     * @param y  The y position of the april tag relative to the robot start pos (0,0) (in cm)
     */
    public void setAprilTagPos(int id, double x, double y) {
        aprilTagPos.put(id, new double[]{x, y});
    }
}
