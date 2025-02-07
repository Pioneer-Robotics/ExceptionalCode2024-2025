package org.firstinspires.ftc.teamcode.SelfDrivingAuto;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

public class Pinpoint {
    GoBildaPinpointDriver pinpoint;
    public Pinpoint(double startX, double startY) {
        pinpoint = Bot.opMode.hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(0,-134);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);
        pinpoint.recalibrateIMU();
        pinpoint.setPosition(new Pose2D(DistanceUnit.CM, startX, startY, AngleUnit.RADIANS, 0));
        pinpoint.update();
        Bot.opMode.telemetry.addData("StartX", startX);
        Bot.opMode.telemetry.addData("StartY", startY);
        Bot.opMode.telemetry.update();
//        pinpoint.setPosition(new Pose2D(DistanceUnit.CM, Config.specimenStartX, Config.specimenStartY, AngleUnit.RADIANS, 0));
    }

    public void update() {
        pinpoint.update();
    }

    public void resetIMU() {
        pinpoint.recalibrateIMU();
    }

    public Pose2D getPositionPose2D() {
        return pinpoint.getPosition();
    }

    public double[] getPosition() {
        pinpoint.update();
        return new double[] { pinpoint.getPosX()/10, pinpoint.getPosY()/10 };
    }

    public double getX() {
        update();
        return pinpoint.getPosX() / 10;
    }

    public double getY() {
        update();
        return pinpoint.getPosY() / 10;
    }

    public double getHeading() {
        return -pinpoint.getHeading();
    }

    public Pose2D getVelocityPose2D() {
        return pinpoint.getVelocity();
    }

    public double[] getVelocity() {
        return new double[] { pinpoint.getVelX()/10, pinpoint.getVelY()/10 };
    }
}
