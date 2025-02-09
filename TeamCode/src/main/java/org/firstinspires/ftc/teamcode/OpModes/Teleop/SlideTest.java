package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

@TeleOp(name="Slide Test")
public class SlideTest extends LinearOpMode {

    public void runOpMode() {

        Bot.init(this);

        DcMotorEx motor1 = hardwareMap.get(DcMotorEx.class, "slideMotor1");
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, "slideMotor2");

        // Reset encoder before starting
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set motor direction explicitly
        motor1.setDirection(DcMotorSimple.Direction.FORWARD); // or REVERSE
        motor2.setDirection(DcMotorSimple.Direction.FORWARD); // or REVERSE

        // Set motor Zero Power Behavior
        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        while(opModeIsActive()) {
            if (gamepad1.a) {
                // Reset the Encoder with the A button if issues arise
                motor1.setPower(0);  // Stop the motor
                motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motor2.setPower(0);  // Stop the motor
                motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else {
                // Look for up or down
                if (gamepad1.dpad_up) {
                    motor1.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                    motor2.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                } else if (gamepad1.dpad_down) {
                    motor1.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                    motor2.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                } else {
                    motor1.setVelocity(0);
                    motor2.setVelocity(0);
                }
            }

            // Log motors current positions
            dashboardTelemetry.addData("Encoder Position 1", motor1.getCurrentPosition());
            dashboardTelemetry.addData("Encoder Position 2", motor2.getCurrentPosition());
            
            // Log additional related values
            dashboardTelemetry.addData("Motor Velocity 1", motor1.getVelocity());
            dashboardTelemetry.addData("Motor Direction 1", motor1.getDirection());
            dashboardTelemetry.addData("Motor Velocity 2", motor2.getVelocity());
            dashboardTelemetry.addData("Motor Direction 2", motor2.getDirection());

            dashboardTelemetry.update();
        }
    }
}
