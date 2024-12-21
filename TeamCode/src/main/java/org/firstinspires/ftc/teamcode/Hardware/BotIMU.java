package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

/**
 * Class used to make accessing IMU easier.
 * Pass LinearOpMode (or 'this' when class
 * extends LinearOpMode) into the constructor.
 */
public class BotIMU {
    private final IMU imu;

    public BotIMU() {
        this.imu = Bot.opMode.hardwareMap.get(IMU.class, Config.imu);
        this.imu.initialize(new com.qualcomm.robotcore.hardware.IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        this.imu.resetYaw();
    }

    /**
     * @return the yaw of the robot in degrees
     */
    public double getDegrees(){
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    /**
     * @return the yaw of the robot in radians
     */
    public double getRadians(){
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    /**
     * Resets the yaw of the robot
     */
    public void resetYaw(){
        imu.resetYaw();
    }

    /**
     * Closes the imu
     */
    public void close(){
        imu.close();
    }
}
