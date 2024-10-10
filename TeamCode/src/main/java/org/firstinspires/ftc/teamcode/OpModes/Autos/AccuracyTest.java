package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

@Autonomous(name = "AccuracyTest", group = "Autos")
public class AccuracyTest extends LinearOpMode {
    public void runOpMode(){
        Bot.init(this);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.addData("X", 0);
        dashboardTelemetry.addData("Y", 0);
        dashboardTelemetry.addData("Odo X", 0);
        dashboardTelemetry.addData("Odo Y", 0);
        dashboardTelemetry.addData("OTOS X", 0);
        dashboardTelemetry.addData("OTOS Y", 0);
        dashboardTelemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            Bot.pidController.moveToPosition(120, 120);
            Utils.sleep(100);
//            Bot.pidController.moveToPosition(120, 120);
//            Utils.sleep(1);
//            Bot.pidController.moveToPosition(120, 0);
//            Utils.sleep(1);
//            Bot.pidController.moveToPosition(0, 0);
//            Utils.sleep(1);
            terminateOpModeNow();
        }
    }
}
