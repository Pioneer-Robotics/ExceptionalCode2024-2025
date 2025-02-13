package org.firstinspires.ftc.teamcode.TestingMocks.fakes;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class FakeIMU implements IMU {

    @Override
    public boolean initialize(Parameters parameters) {
        return false;
    }

    @Override
    public void resetYaw() {

    }

    @Override
    public YawPitchRollAngles getRobotYawPitchRollAngles() {
        return null;
    }

    @Override
    public Orientation getRobotOrientation(AxesReference reference, AxesOrder order, AngleUnit angleUnit) {
        return null;
    }

    @Override
    public Quaternion getRobotOrientationAsQuaternion() {
        return null;
    }

    @Override
    public AngularVelocity getRobotAngularVelocity(AngleUnit angleUnit) {
        return null;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return "";
    }

    @Override
    public String getConnectionInfo() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
