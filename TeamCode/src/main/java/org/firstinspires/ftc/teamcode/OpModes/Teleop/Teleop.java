package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop.SpecimenCycle;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver1;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver2;

@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {
    TeleopDriver1 driver1;
    TeleopDriver2 driver2;
    SpecimenCycle specimenCycle;

    private enum CycleState {
        MANUAL,
        SPECIMEN_HANG
    }

    private CycleState cycleState = CycleState.MANUAL;
    private final Toggle specimenCycleToggle = new Toggle(false);

    public void runOpMode() {
        Bot.init(this);

        driver1 = TeleopDriver1.createInstance(gamepad1);
        driver2 = TeleopDriver2.createInstance(gamepad2);
        specimenCycle = SpecimenCycle.createInstance(gamepad1);

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
        telemetry.addData("Slide pos", Bot.slideArm.getArmPosition());
        telemetry.addData("North Mode", driver1.getNorthModeToggle());
        telemetry.addData("Speed", driver1.getSpeed());
        telemetry.addData("Cycle State", cycleState);
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
        telemetry.addData("Theta", Bot.pinpoint.getHeading());
        telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
        telemetry.addData("Specimen Arm Pos", Bot.specimenArm.getPositionTicks());

        telemetry.addData("Claw State", Bot.intakeClaw.getClawState());

        telemetry.update();
        Bot.dashboardTelemetry.update();
    }
}
