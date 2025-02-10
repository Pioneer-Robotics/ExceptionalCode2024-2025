package org.firstinspires.ftc.teamcode.OpModes.SubSystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TeleopDriver1 {

    // Properties
    Gamepad gamepad1;
    Telemetry telemetry;

    RobotDriveSubSystem driveSubSystem;
    RobotIntakeSubSystem intakeSubSystem;
    RobotTransferSubSystem transferSubSystem;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private TeleopDriver1(Gamepad gamepad1, Telemetry telemetry) {
        this.gamepad1 = gamepad1;
        this.telemetry = telemetry;

        driveSubSystem = RobotDriveSubSystem.createInstance(gamepad1, telemetry);
        intakeSubSystem = RobotIntakeSubSystem.createInstance(gamepad1);
        transferSubSystem = RobotTransferSubSystem.createInstance(gamepad1, telemetry);
    }

    // Factory method to create instances with variables
    public static TeleopDriver1 createInstance(Gamepad gamepad1, Telemetry telemetry) {
        return new TeleopDriver1(gamepad1, telemetry);
    }

    // Run Loop
    public void doOneStateLoop() {
        driveSubSystem.doOneStateLoop();
        intakeSubSystem.doOneStateLoop();
        transferSubSystem.doOneStateLoop();
    }
}
