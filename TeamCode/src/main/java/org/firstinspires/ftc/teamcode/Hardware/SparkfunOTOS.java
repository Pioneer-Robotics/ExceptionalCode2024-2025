package org.firstinspires.ftc.teamcode.Hardware;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;


public class SparkfunOTOS {
    SparkFunOTOS otos;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();

    public SparkfunOTOS() {
        this(new double[]{0, 0, 0});
    }

    public SparkfunOTOS(double[] startPos) {
        otos = Bot.opMode.hardwareMap.get(SparkFunOTOS.class, "sensor_otos");

        otos.calibrateImu();
        otos.resetTracking();

        otos.setLinearUnit(DistanceUnit.CM);
        otos.setAngularUnit(AngleUnit.RADIANS);

        // Used to compensate for scaling errors in sensor readings
//        otos.setLinearScalar(Config.OTOSLinearScale);
        otos.setAngularScalar(1.0);

        SparkFunOTOS.Pose2D currentPosition = new SparkFunOTOS.Pose2D(startPos[0], startPos[1], startPos[2]);
        otos.setPosition(currentPosition);
    }

    public double[] getPose() {
        SparkFunOTOS.Pose2D pose = otos.getPosition();
        // TODO: Use OTOS instead of IMU for angle
        double cosTheta = Math.cos(Bot.imu.getRadians());
        double sinTheta = Math.sin(Bot.imu.getRadians());
        // Rotate offset by theta
        double[] offset = new double[] {Config.OTOSOffset[0]*cosTheta - Config.OTOSOffset[1]*sinTheta, Config.OTOSOffset[0]*sinTheta + Config.OTOSOffset[1]*cosTheta};
        dashboardTelemetry.addData("Offset X", offset[0]);
        dashboardTelemetry.addData("Offset Y", offset[1]);
        dashboardTelemetry.update();
        double[] adjustment = new double[] {offset[0] - Config.OTOSOffset[0], offset[1] - Config.OTOSOffset[1]};
        return new double[]{(-pose.x - adjustment[0])*1.01, (-pose.y - adjustment[1])*1.015, -pose.h};
    }

    public double getHeading() { return getPose()[2]; }

    public double getX() { return getPose()[0]; }

    public double getY() { return getPose()[1]; }

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

    public void setOffset(double[] offset) { otos.setOffset(new SparkFunOTOS.Pose2D(offset[0], offset[1], offset[2])); }
}
