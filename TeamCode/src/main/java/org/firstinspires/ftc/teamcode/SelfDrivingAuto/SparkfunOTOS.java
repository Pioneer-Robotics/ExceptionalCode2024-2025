package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;


public class SparkfunOTOS {
    SparkFunOTOS otos;

    public SparkfunOTOS() {
        this(new double[]{0, 0, 0});
    }

    public SparkfunOTOS(double[] startPos) {
        otos = Bot.opMode.hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        otos.setLinearUnit(DistanceUnit.CM);
        otos.setAngularUnit(AngleUnit.RADIANS);

        // If the sensor is mounted off-center, set the offset here (x, y, theta)
        SparkFunOTOS.Pose2D offset = new SparkFunOTOS.Pose2D(0, 0, 0);
        otos.setOffset(offset);

        // Used to compensate for scaling errors in sensor readings
        otos.setLinearScalar(1.0);
        otos.setAngularScalar(1.0);

        otos.calibrateImu();
        otos.resetTracking();
        
        // Set the initial position to 0, 0, 0
        SparkFunOTOS.Pose2D currentPosition = new SparkFunOTOS.Pose2D(startPos[0], startPos[1], startPos[2]);
        otos.setPosition(currentPosition);
    }

    public double[] getPose() {
        SparkFunOTOS.Pose2D pose = otos.getPosition();
        return new double[]{pose.x, pose.y, pose.h};
    }

    public double getHeading() {
        return otos.getPosition().h;
    }

    public double getX() {
        return otos.getPosition().x;
    }

    public double getY() {
        return otos.getPosition().y;
    }

    public void setPose(double x, double y, double h) {
        SparkFunOTOS.Pose2D pose = new SparkFunOTOS.Pose2D(x, y, h);
        otos.setPosition(pose);
    }

    public void reset() {
        otos.resetTracking();
    }

    public void calibrate() {
        otos.calibrateImu();
    }

    public void setLinearUnit(DistanceUnit unit) {
        otos.setLinearUnit(unit);
    }

    public void setAngularUnit(AngleUnit unit) {
        otos.setAngularUnit(unit);
    }
}
