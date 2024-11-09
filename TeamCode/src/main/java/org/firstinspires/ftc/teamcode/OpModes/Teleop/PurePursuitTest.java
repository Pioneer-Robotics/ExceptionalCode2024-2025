package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.BezierCalc;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.PurePursuit;

@Autonomous(name = "Pure Pursuits", group = "Auto")
public class PurePursuitTest extends LinearOpMode {
    public void runOpMode() {
        PurePursuit purePursuit = new PurePursuit(Config.drivePID[0], Config.drivePID[1], Config.drivePID[2]);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();
        Bot.init(this);
        waitForStart();
        while (opModeIsActive()) {
            double[] p1 = {0,0};
            double[] p2 = {0,100};
            double[] p3 = {100, 0};
            double[] p4 = {100, 100};
            double[][] path = BezierCalc.cubicBez(p1, p2, p3, p4, 100);

            // Set the target path
            purePursuit.setTargetPath(path);

            // Replace with state machine
            while (!purePursuit.reachedTarget()) {
                // Update the pure pursuit
                purePursuit.update();
                dashboardTelemetry.addData("X", Bot.pose.getRawOdometer()[0]);
                dashboardTelemetry.addData("Y", Bot.pose.getRawOdometer()[1]);
                dashboardTelemetry.addData("Heading", Bot.pose.getRawOdometer()[2]);
                dashboardTelemetry.addData("Distance", purePursuit.getDistance());
                dashboardTelemetry.addData("Velocity X", Bot.pose.getVelocityOTOS()[0]);
                dashboardTelemetry.addData("Velocity Y", Bot.pose.getVelocityOTOS()[1]);
                dashboardTelemetry.addData("Velocity Theta", Bot.pose.getVelocityOTOS()[2]);
                dashboardTelemetry.update();
            }
            terminateOpModeNow();
        }
    }
}
