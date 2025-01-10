package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;


@TeleOp(name = "Motor Velocity", group = "Calibration")
public class MotorVelocity extends LinearOpMode {
    public void runOpMode() {
        Bot.init(this);

        waitForStart();

        while (opModeIsActive()) {
            double power = gamepad1.right_trigger;
//            Bot.mecanumBase.getLB().setVelocity(power * Config.maxDriveTicksPerSecond);
//            Bot.mecanumBase.getRB().setVelocity(power * Config.maxDriveTicksPerSecond);
//            Bot.mecanumBase.getLF().setVelocity(power * Config.maxDriveTicksPerSecond);
//            Bot.mecanumBase.getRF().setVelocity(power * Config.maxDriveTicksPerSecond);
//            telemetry.addData("Target Velocity", power * Config.maxDriveTicksPerSecond);
            Bot.mecanumBase.move(0, gamepad1.right_trigger - gamepad1.left_trigger, 0, 1);
            Bot.dashboardTelemetry.addData("Velocity LB", Bot.mecanumBase.getLB().getVelocity());
            Bot.dashboardTelemetry.addData("Velocity RB", Bot.mecanumBase.getRB().getVelocity());
            Bot.dashboardTelemetry.addData("Velocity LF", Bot.mecanumBase.getLF().getVelocity());
            Bot.dashboardTelemetry.addData("Velocity RF", Bot.mecanumBase.getRF().getVelocity());
            Bot.dashboardTelemetry.update();
        }
    }
}
