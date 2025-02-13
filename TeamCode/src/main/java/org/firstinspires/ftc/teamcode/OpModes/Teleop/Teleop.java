package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.OpModes.SubSystems.TeleopDriver1;
import org.firstinspires.ftc.teamcode.OpModes.SubSystems.TeleopDriver2;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop.SpecimenCycle;

@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {
    TeleopDriver1 driver1;
    TeleopDriver2 driver2;
    SpecimenCycle specimenCycle;

    // Properties
    ElapsedTime timer;
    double prevMilliseconds;

    private enum CycleState {
        MANUAL,
        SPECIMEN_HANG
    }

    private CycleState cycleState = CycleState.MANUAL;
    private final Toggle specimenCycleToggle = new Toggle(false);

    // Run Loop
    public void runOpMode() {
        Bot.init(this);

        driver1 = TeleopDriver1.createInstance(gamepad1, telemetry);
        driver2 = TeleopDriver2.createInstance(gamepad2);
        specimenCycle = SpecimenCycle.createInstance();

        waitForStart();

        while(opModeIsActive()) {
            updateCycleState();
            switch (cycleState) {
                case MANUAL:
                    driver1.loopGamepad();
                    driver2.loopGamepad();
                    break;
                case SPECIMEN_HANG:
                    specimenCycle.update();
                    break;
            }
            Bot.pinpoint.update();
            updateTelemetry();
        }
        Bot.currentThreads.stopThreads();
    }

    // Private methods
    private void updateCycleState() {
        specimenCycleToggle.toggle(gamepad1.touchpad);
        // When the button is first pressed, set robot position with specimenCycle.start()
        if (specimenCycleToggle.justChanged() && gamepad1.touchpad) {
            specimenCycle.start();
        }
        // If the button is held, set cycleState to SPECIMEN_HANG
        if (gamepad1.touchpad) {
            cycleState = CycleState.SPECIMEN_HANG;
        } else {
            cycleState = CycleState.MANUAL;
        }
    }

    private void updateTelemetry() {
//        telemetry.addData("North Mode", driver1.getNorthModeToggle());
//        telemetry.addData("Speed", driver1.getSpeed());
        telemetry.addData("Cycle State", cycleState);
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
        telemetry.addData("Theta", Bot.pinpoint.getHeading());
        telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
        telemetry.update();
    }
}
