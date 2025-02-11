package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver1;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver2;

@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {
    TeleopDriver1 driver1;
    TeleopDriver2 driver2;

    public void runOpMode() {
        Bot.init(this);

        waitForStart();

        while(opModeIsActive()) {
            driver1.loopGamepad();
            driver2.loopGamepad();
            Bot.pinpoint.update();
            updateTelemetry();
        }

        Bot.currentThreads.stopThreads();
    }

    private void updateTelemetry() {
        telemetry.addData("North Mode", driver1.getNorthModeToggle());
        telemetry.addData("Speed", driver1.getSpeed());
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
        telemetry.addData("Theta", Bot.pinpoint.getHeading());
        telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
        telemetry.update();
    }
}
