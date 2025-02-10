package org.firstinspires.ftc.teamcode.OpModes.SubSystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Helpers.Toggle;

public class RobotDriveSubSystem {

    // Properties
    Gamepad gamepad;
    Telemetry telemetry;

    public double maxSpeed;
    boolean bothTrigPressed;
    Toggle northModeToggle;

    Toggle incSpeedToggle;
    Toggle decSpeedToggle;

    // Unit Testing properties - TODO: - Abstract away
    boolean isUnitTesting;
    public int testNumberOfResetIMU = 0;
    public double testPx, testPy, testTurn;
    public boolean testIsNorthMode() { return northModeToggle.get(); }

    // Initialization
    // Private constructor to enforce the use of the factory method
    private RobotDriveSubSystem(Gamepad gamepad, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        setup();
        isUnitTesting = false;
    }

    private RobotDriveSubSystem() { // for testing with Gamepad or Telemetry
        setup();
        isUnitTesting = true;
    }

    private void setup() {
        maxSpeed = 0.5;
        bothTrigPressed = false;
        northModeToggle = new Toggle(true);
        setTogglesToDefault();
    }

    // Factory method to create instances with variables
    public static RobotDriveSubSystem createInstance(Gamepad gamepad, Telemetry telemetry) {
        return new RobotDriveSubSystem(gamepad, telemetry);
    }

    public static RobotDriveSubSystem createTestInstance() {
        return new RobotDriveSubSystem();
    }


    // Run Loop
    public void doOneStateLoop() {
        setTogglesToDefault();
        moveRobot();
        handleToggleForFieldCentric();
        handleResetIMU();
        handleToggleSpeed();
        updateTelemetry();
    }

    private void setTogglesToDefault() {
        incSpeedToggle = new Toggle(false);
        decSpeedToggle = new Toggle(false);
    }

    // Driving
    private void moveRobot() {
        moveRobotAction(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.right_stick_x);
    }

    private void handleToggleForFieldCentric() {
        toggleForFieldCentricAction(gamepad.left_trigger, gamepad.right_trigger);
    }

    private void handleResetIMU() {
        resetIMUAction(gamepad.x);
    }

    private void handleToggleSpeed() {
        toggleSpeedAction(gamepad.right_bumper, gamepad.left_bumper);
    }



    // Testable Actions ----------------------------------
    public void moveRobotAction(double px, double py, double turn) {
        if (isUnitTesting) {
            this.testPx = px;
            this.testPy = py;
            this.testTurn = turn;
        } else {
            Bot.mecanumBase.move(px, py, turn, maxSpeed);
        }
    }

    public void toggleForFieldCentricAction(double left_trigger, double right_trigger) {
        bothTrigPressed = left_trigger > 0.8 && right_trigger > 0.8;
        northModeToggle.toggle(bothTrigPressed); // Toggle north mode
        if (!isUnitTesting) {
            Bot.mecanumBase.setNorthMode(northModeToggle.get()); // Update north mode
        }
    }

    public void resetIMUAction(boolean isButtonPressed) {
        if (isButtonPressed) {
            if (isUnitTesting) {
                testNumberOfResetIMU += 1;
            } else {
                Bot.pinpoint.resetIMU();
            }
        }
    }

    public void toggleSpeedAction(boolean increment, boolean decrement) {
        incSpeedToggle.toggle(increment);
        decSpeedToggle.toggle(decrement);
        if (incSpeedToggle.justChanged()) {
            maxSpeed += 0.1;
        }
        if (decSpeedToggle.justChanged()) {
            maxSpeed -= 0.1;
        }
    }

    //Telemetry
    public void updateTelemetry() {
        telemetry.addData("North Mode", northModeToggle.get());
        telemetry.addData("Speed", maxSpeed);
        telemetry.update();
    }
}
