package org.firstinspires.ftc.teamcode.OpModes.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config;

@Disabled
@TeleOp(name="Slide Test")
public class SlideTest extends LinearOpMode {

    public void runOpMode() {

//        Bot.init(this);

        waitForStart();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        DcMotorEx motor1 = hardwareMap.get(DcMotorEx.class, "slideMotor1");
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, "slideMotor2");

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor1.setTargetPosition(15);

        while(opModeIsActive()) {

//            if (gamepad1.dpad_up) {
//                motor1.setVelocity(0.5 * Config.maxSlideTicksPerSecond);
//            } else if (gamepad1.dpad_down) {
//                motor1.setVelocity(-0.5 * Config.maxSlideTicksPerSecond);
//            } else {
//                motor1.setPower(0);
//            }
//
//            if (gamepad1.triangle) {
//                motor2.setVelocity(-0.5 * Config.maxSlideTicksPerSecond);
//            } else if (gamepad1.cross) {
//                motor2.setVelocity(0.5 * Config.maxSlideTicksPerSecond);
//            } else {
//                motor2.setPower(0);
//            }

            if (gamepad1.right_bumper) {
                motor1.setTargetPosition(-1500);
                motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor1.setVelocity(0.5 * Config.maxSlideTicksPerSecond);
            }
            else if (gamepad1.left_bumper) {
                motor1.setTargetPosition(15);
                motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                motor1.setVelocity(0.5 * Config.maxSlideTicksPerSecond);
            } else {
                motor1.setVelocity(0);
            }

//            if (gamepad1.circle) {
//                motor1.setPower(-0.25);
//                motor2.setPower(0.25);
//            }
            telemetry.addData("Slide Arm 1 Position", motor1.getCurrentPosition());
            telemetry.addData("Slide Arm 2 Position", motor2.getCurrentPosition());
            telemetry.addData("Slide Arm 1 Target Position", motor1.getTargetPosition());
            telemetry.addData("Slide Arm 2 Target Position", motor2.getTargetPosition());
            telemetry.update();
        }
    }
}
