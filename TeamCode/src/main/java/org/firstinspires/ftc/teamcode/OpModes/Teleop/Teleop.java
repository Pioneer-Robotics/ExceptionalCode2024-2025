package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
//import org.firstinspires.ftc.teamcode.OpModes.SubSystems.TeleopDriver1;
//import org.firstinspires.ftc.teamcode.OpModes.SubSystems.TeleopDriver2;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver1;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver2;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop.SpecimenCycle;

@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {
    public TeleopDriver1 driver1;
    public TeleopDriver2 driver2;
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

    public Gamepad _gamepad1 = gamepad1;
    public Gamepad _gamepad2 = gamepad2;
    public Telemetry _telemetry = telemetry;
    public boolean unitTestIsActive = false;

    // Run Loop
    public void runOpMode() {
        Bot.init(this);

        driver1 = TeleopDriver1.createInstance(_gamepad1);
        driver2 = TeleopDriver2.createInstance(_gamepad2);
        specimenCycle = SpecimenCycle.createInstance();

        if (!Bot.isUnitTest) {
            waitForStart();
        } else {
            unitTestIsActive = true;
        }

        while (opModeIsActive() || unitTestIsActive) {
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
            unitTestIsActive = false;
        }
        Bot.currentThreads.stopThreads();
    }

    // Private methods
    private void updateCycleState() {
        specimenCycleToggle.toggle(_gamepad1.touchpad);
        // When the button is first pressed, set robot position with specimenCycle.start()
        if (specimenCycleToggle.justChanged() && _gamepad1.touchpad) {
            specimenCycle.start();
        }
        // If the button is held, set cycleState to SPECIMEN_HANG
        if (_gamepad1.touchpad) {
            cycleState = CycleState.SPECIMEN_HANG;
        } else {
            cycleState = CycleState.MANUAL;
        }
    }

    private void updateTelemetry() {
//        telemetry.addData("North Mode", driver1.getNorthModeToggle());
//        telemetry.addData("Speed", driver1.getSpeed());
        _telemetry.addData("Cycle State", cycleState);
        _telemetry.addData("X", Bot.pinpoint.getX());
        _telemetry.addData("Y", Bot.pinpoint.getY());
        _telemetry.addData("Theta", Bot.pinpoint.getHeading());
        _telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
        _telemetry.update();
    }
}
