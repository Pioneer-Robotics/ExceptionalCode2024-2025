package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Utils;

@Autonomous(name = "Odometry Tuning", group = "Calibration")
public class OdometryTuning extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        waitForStart();
        while (opModeIsActive()) {
            Bot.purePursuit.setTargetPath(new double[][] {{0,0}, {0,180}});
            while(!Bot.purePursuit.reachedTarget()) {
                Bot.purePursuit.update();
                TelemetryPacket packet = new TelemetryPacket(false);
                packet.put("X", Bot.optical_odom.getX());
                packet.put("Y", Bot.optical_odom.getY());
                packet.put("Error X", 180-Bot.optical_odom.getX());
                packet.put("Error Y", 0-Bot.optical_odom.getY());
                dashboard.sendTelemetryPacket(packet);
            }
            Utils.sleep(5);
            Bot.purePursuit.setTargetPath(new double[][] {{0,180}, {180,180}});
            while(!Bot.purePursuit.reachedTarget()) {
                Bot.purePursuit.update();
                TelemetryPacket packet = new TelemetryPacket(false);
                packet.put("X", Bot.optical_odom.getX());
                packet.put("Y", Bot.optical_odom.getY());
                packet.put("Error X", 180-Bot.optical_odom.getX());
                packet.put("Error Y", 180-Bot.optical_odom.getY());
                dashboard.sendTelemetryPacket(packet);
            }
            terminateOpModeNow();
        }
    }
}
