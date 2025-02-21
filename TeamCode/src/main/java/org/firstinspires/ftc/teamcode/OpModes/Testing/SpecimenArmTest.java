package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;

@TeleOp(name="Spec Arm Test")
public class SpecimenArmTest extends LinearOpMode {

    public void runOpMode() {

        Bot.init(this);
        FtcDashboard dashboard = FtcDashboard.getInstance();
//        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        waitForStart();

        while(opModeIsActive()) {

            if(gamepad1.dpad_left){
                Bot.specimenArm.moveToCollect(0.5);
            }

            if(gamepad1.dpad_down){
                Bot.specimenArm.movePrepHang(1);
            }

            if(gamepad1.dpad_up){
                Bot.specimenArm.movePrepHangDampen(1, 2000);
            }

        }
    }
}
