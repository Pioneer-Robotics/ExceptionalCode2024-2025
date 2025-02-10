package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.OpModes.SubSystems.TeleopDriver1;
import org.firstinspires.ftc.teamcode.OpModes.SubSystems.TeleopDriver2;


@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {

    // Properties
    ElapsedTime timer;
    double prevMilliseconds;

    TeleopDriver1 driver1;
    TeleopDriver2 driver2;

    // Run Loop
    public void runOpMode() {
        Bot.init(this);

        initializeRunLoop();
        driver1 = TeleopDriver1.createInstance(gamepad1, telemetry);
        driver2 = TeleopDriver2.createInstance(gamepad2);

        waitForStart();

        while(opModeIsActive()) {
            driver1.doOneStateLoop();
            driver2.doOneStateLoop();
            updateTelemetry();
            updateRobotLights();
            prevMilliseconds = timer.milliseconds();
        }
        Bot.currentThreads.stopThreads();
    }

    // Private methods
    private void initializeRunLoop() {
        timer = new ElapsedTime();
        prevMilliseconds = 0;
    }

    public void updateTelemetry() {
        // Telemetry and update
        Bot.pinpoint.update();
        telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
        telemetry.addData("dt", timer.milliseconds() - prevMilliseconds);
        telemetry.addData("Refresh Rate (Hz)", 1000 / (timer.milliseconds() - prevMilliseconds));
        telemetry.addData("Thread Current Slide 1", Bot.currentThreads.getSlideCurrent1());
        telemetry.addData("Thread Current Slide 2", Bot.currentThreads.getSlideCurrent2());
        telemetry.addData("Thread Current Spec", Bot.currentThreads.getSpecArmCurrent());
        telemetry.addData("Slide Arm 1 Position", Bot.slideArm.getMotor1().getCurrentPosition());
        telemetry.addData("Slide Arm 2 Position", Bot.slideArm.getMotor2().getCurrentPosition());
        telemetry.addData("Specimen Arm Position", Bot.specimenArm.getPositionTicks());
        telemetry.addData("Specimen Endstop", Bot.specimenEndStop.getVoltage());
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
        telemetry.addData("Heading", Bot.pinpoint.getHeading());
        telemetry.update();

        Bot.dashboardTelemetry.addData("Slide 1 Velocity", Bot.slideArm.getMotor1().getVelocity());
        Bot.dashboardTelemetry.addData("Slide 2 Velocity", Bot.slideArm.getMotor2().getVelocity());
        Bot.dashboardTelemetry.update();
    }

    private void updateRobotLights() {
        // Get data for telemetry
        double voltage = Bot.voltageHandler.getVoltage();
        if (voltage < 10) {
            Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
            telemetry.addData("WARNING: Voltage Low", voltage);
        } else {
            Bot.led.lightsOn(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        }
        telemetry.addData("Voltage", voltage);
    }
}
