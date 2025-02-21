package org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PID;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.opencv.core.Point;

import java.util.List;
//@TeleOp(name = "Auto Intake Test", group = "Testing")
public class AutoIntake {
    ColorBlobLocatorProcessor.Blob largestBlob;
    List<ColorBlobLocatorProcessor.Blob> locatedBlobs;

    PID pidX = new PID(Config.autoIntakePIDX[0],Config.autoIntakePIDX[1],Config.autoIntakePIDX[2]);
    PID pidY = new PID(Config.autoIntakePIDY[0],Config.autoIntakePIDY[1],Config.autoIntakePIDY[2]);

    public static AutoIntake createInstance() {
        return new AutoIntake();
    }

    public void update() {
        locatedBlobs = Bot.locator.getBlobsList();
        largestBlob = Bot.locator.getBlob(locatedBlobs, 0);

        Point blobPos = Bot.locator.getAlignPoint(largestBlob);

        double PIDXOutput = pidX.calculate(blobPos.x, Config.blobTargetPoint.x);
        double PIDYOutput = pidY.calculate(blobPos.y, Config.blobTargetPoint.y);

        Bot.intake.changePosition(PIDYOutput);
        Bot.mecanumBase.move(0, 0, PIDXOutput, 0.5);
        Bot.dashboardTelemetry.addData("PID X Output", PIDXOutput);
        Bot.dashboardTelemetry.addData("PID Y Output", PIDYOutput);
    }
}
