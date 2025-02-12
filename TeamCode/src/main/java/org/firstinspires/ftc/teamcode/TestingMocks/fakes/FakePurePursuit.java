package org.firstinspires.ftc.teamcode.TestingMocks.fakes;

import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PurePursuit;

public class FakePurePursuit extends PurePursuit {
    public FakePurePursuit(double kP, double kI, double kD) {
        super(kP, kI, kD);
    }

    public FakePurePursuit(double kP, double kI, double kD, double initialError) {
        super(kP, kI, kD, initialError);
    }

    @Override
    public boolean reachedTarget(double tolerance, double turnTolerance) {
        return true;
    }

    @Override
    public boolean reachedTargetXY(double xTolerance, double yTolerance) {
        return true;
    }
}
