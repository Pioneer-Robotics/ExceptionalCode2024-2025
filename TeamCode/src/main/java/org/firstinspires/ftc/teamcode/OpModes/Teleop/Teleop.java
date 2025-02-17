package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.AutomatedTeleop.SpecimenCycle;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver1;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Drivers.TeleopDriver2;
import org.firstinspires.ftc.teamcode.OpModes.Testing.BlockDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@TeleOp(name="Teleop")
public class Teleop extends LinearOpMode {
    TeleopDriver1 driver1;
    TeleopDriver2 driver2;
    SpecimenCycle specimenCycle;

    OpenCvCamera phoneCam;
    BlockDetector detector;

    private enum CycleState {
        MANUAL,
        SPECIMEN_HANG
    }

    private CycleState cycleState = CycleState.MANUAL;
    private final Toggle specimenCycleToggle = new Toggle(false);

    public void runOpMode() {
        Bot.init(this);

        driver1 = TeleopDriver1.createInstance(gamepad1, useCameraCode(), detector);
        driver2 = TeleopDriver2.createInstance(gamepad2);
        specimenCycle = SpecimenCycle.createInstance();

        if (useCameraCode()) {
            initalizeCameraCode();
        }
        waitForStart();

        while (opModeIsActive()) {
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

    // Override methods
    BlockDetector.TeamColor teamColor() {
        return BlockDetector.TeamColor.RED; // default to red
    }

    boolean useCameraCode() {
        return false;
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

    private void initalizeCameraCode() {
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        BlockDetector detector = new BlockDetector(telemetry, teamColor());
        phoneCam.setPipeline(detector);
        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                // TODO: -
            }
        });
    }

    private void updateTelemetry() {
        telemetry.addData("North Mode", driver1.getNorthModeToggle());
        telemetry.addData("Speed", driver1.getSpeed());
        telemetry.addData("Cycle State", cycleState);
        telemetry.addData("X", Bot.pinpoint.getX());
        telemetry.addData("Y", Bot.pinpoint.getY());
        telemetry.addData("Theta", Bot.pinpoint.getHeading());
        telemetry.addData("Total Current", Bot.currentThreads.getTotalCurrent());
        telemetry.update();
    }
}
