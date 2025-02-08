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

        waitForStart();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        DcMotorEx motor1 = hardwareMap.get(DcMotorEx.class, "slideMotor1");
        DcMotorEx motor2 = hardwareMap.get(DcMotorEx.class, "slideMotor2");

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);

//        Boolean leftSide = false;

//        motor1.setTargetPosition(0);
//        motor2.setTargetPosition(0);
//
//        motor1.setVelocity(Config.maxSlideTicksPerSecond*0.25);
//        motor2.setVelocity(Config.maxSlideTicksPerSecond*0.25);

        while(opModeIsActive()) {
            DcMotorSimple.Direction direction1 = motor1.getDirection();
            DcMotorSimple.Direction direction2 = motor2.getDirection();

            if (direction1 == DcMotorSimple.Direction.FORWARD) {
                if (gamepad1.dpad_up) {
                    motor1.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
//                    motor2.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                } else if (gamepad1.dpad_down) {
                    motor1.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
//                    motor2.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                } else {
                    motor1.setVelocity(0);
//                    motor2.setVelocity(0);
                }
            } else {
                if (!gamepad1.dpad_up) {
                    motor1.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
//                    motor2.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                } else if (!gamepad1.dpad_down) {
                    motor1.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
//                    motor2.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                } else {
                    motor1.setVelocity(0);
//                    motor2.setVelocity(0);
                }
            }

            if (direction2 == DcMotorSimple.Direction.FORWARD) {
                if (gamepad1.dpad_up) {
//                    motor1.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                    motor2.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                } else if (gamepad1.dpad_down) {
//                    motor1.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                    motor2.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                } else {
//                    motor1.setVelocity(0);
                    motor2.setVelocity(0);
                }
            } else {
                if (!gamepad1.dpad_up) {
//                    motor1.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                    motor2.setVelocity(0.25 * Config.maxSlideTicksPerSecond);
                } else if (!gamepad1.dpad_down) {
//                    motor1.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                    motor2.setVelocity(-0.25 * Config.maxSlideTicksPerSecond);
                } else {
//                    motor1.setVelocity(0);
                    motor2.setVelocity(0);
                }
            }


//            if (gamepad1.right_trigger > 0.05) {
//                motor1.setVelocity(gamepad1.right_trigger * Config.maxSlideTicksPerSecond);
//                motor2.setVelocity(gamepad1.right_trigger * Config.maxSlideTicksPerSecond);
//            } else if (gamepad1.left_trigger > 0.05) {
//                motor1.setVelocity(-gamepad1.left_trigger * Config.maxSlideTicksPerSecond);
//                motor2.setVelocity(-gamepad1.left_trigger * Config.maxSlideTicksPerSecond);
//            } else {
//                motor1.setVelocity(0);
//                motor2.setVelocity(0);
//            }
//            if (gamepad1.y && gamepad1.a) {
//                if (gamepad1.dpad_up) {
//                    motor1.setVelocity(0.2 * Config.maxSlideTicksPerSecond);
//                    motor2.setVelocity(-0.2 * Config.maxSlideTicksPerSecond);
//                } else if (gamepad1.dpad_down) {
//                    motor1.setVelocity(-0.2 * Config.maxSlideTicksPerSecond);
//                    motor2.setVelocity(0.2 * Config.maxSlideTicksPerSecond);
//                } else {
//                    motor1.setVelocity(0);
//                    motor2.setVelocity(0);
//                }
//            } else {
//                if (gamepad1.dpad_up && gamepad1.y) {
//                    motor1.setVelocity(0.2 * Config.maxSlideTicksPerSecond);
//                } else if (gamepad1.dpad_down && gamepad1.y) {
//                    motor1.setVelocity(-0.2 * Config.maxSlideTicksPerSecond);
//                } else {
//                    motor1.setVelocity(0);
//                }
//
//                if (gamepad1.dpad_up && gamepad1.a) {
//                    motor2.setVelocity(-0.2 * Config.maxSlideTicksPerSecond);
//                } else if (gamepad1.dpad_down && gamepad1.a) {
//                    motor2.setVelocity(0.2 * Config.maxSlideTicksPerSecond);
//                } else {
//                    motor2.setVelocity(0);
//                }
//            }

//            if (gamepad1.dpad_up) {
//                motor1.setTargetPosition(-500);
//                motor2.setTargetPosition(-500);
//            }
//
//            if (gamepad1.dpad_down) {
//                motor1.setTargetPosition(0);
//                motor2.setTargetPosition(0);
//            }
//
//            motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//            dashboardTelemetry.addData("M1 Velocity", motor1.getVelocity());
//            dashboardTelemetry.addData("M2 Velocity", motor2.getVelocity());
//            dashboardTelemetry.addData("M1 Current", motor1.getCurrent(CurrentUnit.MILLIAMPS));
//            dashboardTelemetry.addData("M2 Current", motor2.getCurrent(CurrentUnit.MILLIAMPS));
//
//            if (gamepad1.dpad_up) {
//                Bot.slideArm.moveMid(0.25);
//            }
//
//            if (gamepad1.dpad_down) {
//                Bot.slideArm.moveDown(0.25);
//            }

//            dashboardTelemetry.update();
        }
    }
}
