package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;

public class KalmanFilter {
    private MatrixF x; // State estimate
    private MatrixF P; // Error covariance

    // Default system matrices
    private MatrixF A; // State transition matrix
    private MatrixF B; // Control input matrix
    private MatrixF H_odom; // Measurement matrix for odometers
    private MatrixF H_OTOS; // Measurement matrix for OTOS

    // Default process and measurement noise covariance matrices
    private MatrixF Q; // Process noise covariance
    private MatrixF R_odom; // Measurement noise covariance for odometers
    private MatrixF R_OTOS; // Measurement noise covariance for OTOS

    public KalmanFilter(MatrixF initialX, MatrixF initialP) {
        this.x = initialX;
        this.P = initialP;
    }

    public KalmanFilter(MatrixF initialX, MatrixF initialP,
                        MatrixF A, MatrixF B, MatrixF Q,
                        MatrixF R_odom, MatrixF H_odom,
                        MatrixF R_OTOS, MatrixF H_OTOS) {
        this.x = initialX;
        this.P = initialP;
        this.A = A;
        this.B = B;
        this.Q = Q;
        this.R_odom = R_odom;
        this.H_odom = H_odom;
        this.R_OTOS = R_OTOS;
        this.H_OTOS = H_OTOS;
    }

    public void setA(MatrixF A) {
        this.A = A;
    }

    public void setB(MatrixF B) {
        this.B = B;
    }

    public void setHOdom(MatrixF H) {
        this.H_odom = H;
    }

    public void setHOTOS(MatrixF H) {
        this.H_OTOS = H;
    }

    public void setQ(MatrixF Q) {
        this.Q = Q;
    }

    public void setROdom(MatrixF R) {
        this.R_odom = R;
    }

    public void setROTOS(MatrixF R) {
        this.R_OTOS = R;
    }

    /**
     * Predict the state and error covariance
     *
     * @param u Control input
     */
    public void predict(MatrixF u) {
        // Predict the state
        x = A.multiplied(x).added(B.multiplied(u));
        // Predict the error covariance
        P = A.multiplied(P).multiplied(A.transposed()).added(Q);
    }

    /**
     * Update the state estimate and error covariance
     *
     * @param z Measurement
     */
    public void update(MatrixF z, MatrixF H, MatrixF R) {
        // Calculate the Kalman gain
        MatrixF K = P.multiplied(H.transposed()).multiplied(
                H.multiplied(P).multiplied(H.transposed()).added(R).inverted());

        // Update the state estimate
        x = x.added(K.multiplied(z.subtracted(H.multiplied(x))));

        // Update the error covariance
        P = (MatrixF.identityMatrix(P.numCols())).subtracted(K.multiplied(H)).multiplied(P);
    }

    public void updateOdom(MatrixF z) {
        update(z, H_odom, R_odom);
    }

    public void updateOTOS(MatrixF z) {
        update(z, H_OTOS, R_OTOS);
    }

    public MatrixF getState() {
        return x;
    }

    public float[] getStateArray() {
        return x.toVector().getData();
    }

    public MatrixF getErrorCovariance() {
        return P;
    }

    public float[] getErrorCovarianceArray() {
        return P.toVector().getData();
    }
}