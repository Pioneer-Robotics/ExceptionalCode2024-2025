package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

@Autonomous(name = "Auto Calibration", group = "Calibration")
public class AutoCalibration extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        waitForStart();

        // Offset needs to be 0,0,0 for optical odometry
        while (opModeIsActive()) {
            while (Bot.pose.getRawOTOS()[2] < 3.13) {
                Bot.mecanumBase.move(0, 0.1, 0); // Turn to pi radians
                dashboardTelemetry.addData("Angle", Bot.pose.getRawOTOS()[2]);
                dashboardTelemetry.update();
            }
            Bot.mecanumBase.stop();
//
//            double[] pos = Bot.pose.getRawOTOS();
//            double radius = Math.sqrt(pos[0]*pos[0] + pos[1]*pos[1])/2;
//            double[] center = new double[] {pos[0]/2, pos[1]/2};
//            double angle = Math.atan2(center[0], center[1]);
//
//            dashboardTelemetry.addData("X Offset", Math.cos(angle) * radius);
//            dashboardTelemetry.addData("Y Offset", Math.sin(angle) * radius);
//            dashboardTelemetry.update();
            Utils.sleep(10);
            terminateOpModeNow();
        }
    }
}
