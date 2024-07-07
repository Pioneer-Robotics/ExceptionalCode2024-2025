package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;

public class KalmanFilter {
    private MatrixF x; // State estimate
    private MatrixF P; // Error covariance

    // Default system matrices
    private MatrixF A; // State transition matrix
    private MatrixF B; // Control input matrix
    private MatrixF H; // Measurement matrix

    // Default process and measurement noise covariance matrices
    private MatrixF Q; // Process noise covariance
    private MatrixF R; // Measurement noise covariance

    public KalmanFilter(MatrixF initialX, MatrixF initialP) {
        this.x = initialX;
        this.P = initialP;
    }

    public KalmanFilter(MatrixF initialX, MatrixF initialP,
                        MatrixF A, MatrixF B, MatrixF H,
                        MatrixF Q, MatrixF R) {
        this.x = initialX;
        this.P = initialP;
        this.A = A;
        this.B = B;
        this.H = H;
        this.Q = Q;
        this.R = R;
    }

    public void setA(MatrixF A) {
        this.A = A;
    }

    public void setB(MatrixF B) {
        this.B = B;
    }

    public void setH(MatrixF H) {
        this.H = H;
    }

    public void setQ(MatrixF Q) {
        this.Q = Q;
    }

    public void setR(MatrixF R) {
        this.R = R;
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
    public void update(MatrixF z) {
        // Calculate the Kalman gain
        MatrixF K = P.multiplied(H.transposed()).multiplied(
                H.multiplied(P).multiplied(H.transposed()).added(R).inverted());

        // Update the state estimate
        x = x.added(K.multiplied(z.subtracted(H.multiplied(x))));

        // Update the error covariance
        P = (MatrixF.identityMatrix(P.numCols())).subtracted(K.multiplied(H)).multiplied(P);
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
