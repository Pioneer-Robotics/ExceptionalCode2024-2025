package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class RobotDriveSubSystem {

    // Properties
    Gamepad gamepad;
    Telemetry telemetry;

    double maxSpeed;
    boolean bothTrigPressed;
    Toggle northModeToggle;

    Toggle incSpeedToggle;
    Toggle decSpeedToggle;

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotDriveSubSystem(Gamepad gamepad, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.telemetry = telemetry;

        // Initialize max speed
        maxSpeed = 0.5;
        bothTrigPressed = false;
        northModeToggle = new Toggle(true);
    }

    // Factory method to create instances with variables
    public static RobotDriveSubSystem createInstance(Gamepad gamepad, Telemetry telemetry) {
        return new RobotDriveSubSystem(gamepad, telemetry);
    }

    // Run Loop
    public void doOneStateLoop() {
        setupToggles();
        moveRobot();
        handleToggleForFieldCentric();
        resetIMU();
        handleToggleSpeed();
        updateTelemetry();
    }

    private void setupToggles() {
        incSpeedToggle = new Toggle(false);
        decSpeedToggle = new Toggle(false);
    }

    // Driving
    private void moveRobot() {
        // Inputs for driving
        double px = gamepad.left_stick_x;
        double py = -gamepad.left_stick_y;
        double turn = gamepad.right_stick_x;

        // Move
        Bot.mecanumBase.move(px, py, turn, maxSpeed);
    }

    private void handleToggleForFieldCentric() {
        bothTrigPressed = gamepad.left_trigger > 0.8 && gamepad.right_trigger > 0.8;
        northModeToggle.toggle(bothTrigPressed); // Toggle north mode
        Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode
    }

    private void resetIMU() {
        if (gamepad.x) {
            Bot.pinpoint.resetIMU();
        }
    }

    private void handleToggleSpeed() {
        incSpeedToggle.toggle(gamepad.right_bumper);
        decSpeedToggle.toggle(gamepad.left_bumper);
        if (incSpeedToggle.justChanged()) {
            maxSpeed += 0.1;
        }
        if (decSpeedToggle.justChanged()) {
            maxSpeed -= 0.1;
        }
    }

    public void updateTelemetry() {
        telemetry.addData("North Mode", northModeToggle.get());
        telemetry.addData("Speed", maxSpeed);
        telemetry.update();
    }
}
