import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.OpModes.Teleop.Teleop;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeGamepad;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeTelemetry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTeleop {

    // Properties
    Teleop systemUnderTest;

    FakeGamepad gamepad1;
    FakeGamepad gamepad2;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        Bot.isUnitTest = true;

        systemUnderTest = new Teleop();
        systemUnderTest.unitTestIsActive = true;
        systemUnderTest._telemetry = new FakeTelemetry();

        gamepad1 = new FakeGamepad();
        gamepad2 = new FakeGamepad();

        systemUnderTest._gamepad1 = gamepad1;
        systemUnderTest._gamepad2 = gamepad2;
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void test_Driver1_Bumpers_ButtonPressed() {
        systemUnderTest._gamepad1.right_bumper = true;
        systemUnderTest.runOpMode();

        // check to see if Speed changed + 0.1 from 0.5 default
        Assert.assertEquals(0.6, systemUnderTest.driver1.speed, 0.01);

        systemUnderTest._gamepad1.left_bumper = true;
        systemUnderTest.runOpMode();

        // check to see if Speed changed - 0.1 back to 0.5
        Assert.assertEquals(0.5, systemUnderTest.driver1.speed, 0.01);
    }

    @Test
    public void test_Driver1_Trigger_ButtonPressed() {
        systemUnderTest._gamepad1.left_trigger = 1.0F;
        systemUnderTest.runOpMode();

        // check to see if North mode is triggered
        Assert.assertEquals(true, systemUnderTest.driver1.northMode);

        systemUnderTest._gamepad1.right_trigger = 1.0F;
        systemUnderTest.runOpMode();

        // check to see if North mode is triggered
        Assert.assertEquals(false, systemUnderTest.driver1.northMode);
    }

    @Test
    public void test_Driver1_X_ButtonPressed() {
        systemUnderTest._gamepad1.x = true;
        systemUnderTest.runOpMode();

        // check to see if resetIMUToggle is true & just changed
        Assert.assertEquals(true, systemUnderTest.driver1.resetIMUToggle.get());
        Assert.assertEquals(true, systemUnderTest.driver1.resetIMUToggle.justChanged());
    }

    // TODO: - DriveRobot

    @Test
    public void test_Driver1_DPad_ButtonPressed() {
        systemUnderTest._gamepad1.dpad_up = true;
        systemUnderTest.runOpMode();

        // check to see if Mid Wrist has been toggled
        Assert.assertEquals(true, Bot.intake.isWristMid());

        systemUnderTest._gamepad1.dpad_down = true;
        systemUnderTest.runOpMode();

        // check to see if Mid Wrist has been toggled
        Assert.assertEquals(true, Bot.intake.isWristMid());
        Assert.assertEquals(true, Bot.intakeClaw.isClawUp());
    }

    @Test
    public void test_Driver1_B_ButtonPressed() {
        systemUnderTest._gamepad1.b = true;
        systemUnderTest.runOpMode();

        // check to see if Intake Claw has been toggled
        Assert.assertEquals(true, Bot.intakeClaw.isClawOpen());
    }

    @Test
    public void test_Driver1_Y_ButtonPressed() {
        systemUnderTest._gamepad1.y = true;
        systemUnderTest.runOpMode();

        // check to see if Mid Wrist has been toggled
        Assert.assertEquals(true, Bot.intake.isWristMid());

        systemUnderTest._gamepad1.y = false;
        systemUnderTest.runOpMode();
        systemUnderTest._gamepad1.y = false;
        systemUnderTest.runOpMode();

        // check to see if Wrist Up has been toggled
        Assert.assertEquals(true, Bot.intake.isWristUp());

        Bot.intake.isExtended = true;

        systemUnderTest._gamepad1.y = true;
        systemUnderTest.runOpMode();

        // check to see if Mid Wrist has been toggled
        Assert.assertEquals(true, Bot.intake.isWristMid());

        systemUnderTest._gamepad1.y = false;
        systemUnderTest.runOpMode();
        systemUnderTest._gamepad1.y = false;
        systemUnderTest.runOpMode();

        // check to see if Wrist Up has been toggled
        Assert.assertEquals(true, Bot.intake.isWristUp());
    }




}
