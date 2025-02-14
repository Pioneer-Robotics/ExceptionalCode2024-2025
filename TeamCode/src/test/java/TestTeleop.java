import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
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

    private final double yawServoMid = (Config.intakeYawLeft + Config.intakeYawRight) / 2;
    private final double yawServo45 = (yawServoMid + Config.intakeYawRight) / 2;
    private final double yawServoNeg45 = (yawServoMid + Config.intakeYawLeft) / 2;

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
    // Game Pad 1 **************
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

    // Game Pad 2 *************
    @Test
    public void test_Driver2_DPad_ButtonPressed() {
        systemUnderTest._gamepad2.dpad_down = true;
        systemUnderTest.runOpMode();

        // check to see position 0
        Assert.assertEquals(0, Bot.specimenArm.position);

        systemUnderTest._gamepad2.dpad_down = false;
        systemUnderTest._gamepad2.dpad_left = true;
        systemUnderTest.runOpMode();

        // check to see if position 2
        Assert.assertEquals(2, Bot.specimenArm.position);
    }

    @Test
    public void test_Driver2_Circle_ButtonPressed() { // **** This might be a bug ****
        systemUnderTest.initalize();

        systemUnderTest._gamepad2.circle = true;
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if Specimen arm is at open position
        Assert.assertEquals(Bot.specimenArm.claw.openPos, Bot.specimenArm.claw.getPos(), 0.01);

        systemUnderTest._gamepad2.circle = false; // let go of button
        systemUnderTest.runLoop(); // only run runloop after initialize
        systemUnderTest._gamepad2.circle = true; // press button again
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if Specimen arm is at closed position
        Assert.assertEquals(Bot.specimenArm.claw.closePos, Bot.specimenArm.claw.getPos(), 0.01);
    }

    // TODO: - Claw Rotation
    @Test
    public void test_Driver2_LeftStick_X_Moved() {
        systemUnderTest.initalize();

        double test = Bot.intakeClaw.yawServo.getPos();
        systemUnderTest._gamepad2.left_stick_x = (float) -1.0; // left_stick_x < -0.5
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(Config.intakeYawLeft, Bot.intakeClaw.yawServo.getPos(), 0.01);

        systemUnderTest._gamepad2.left_stick_x = (float) -0.4; // left_stick_x > -0.5 && left_stick_x < -0.1
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(yawServoNeg45, Bot.intakeClaw.yawServo.getPos(), 0.01);

        systemUnderTest._gamepad2.left_stick_x = (float) 1.0; // left_stick_x > 0.5
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(Config.intakeYawRight, Bot.intakeClaw.yawServo.getPos(), 0.01);

        systemUnderTest._gamepad2.left_stick_x = (float) 0.2; // left_stick_x < 0.5 && left_stick_x > 0.1
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(yawServo45, Bot.intakeClaw.yawServo.getPos(), 0.01);

        systemUnderTest._gamepad2.left_stick_x = (float) -0.0; // else...
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(yawServoMid, Bot.intakeClaw.yawServo.getPos(), 0.01);

        systemUnderTest._gamepad2.left_stick_x = (float) -0.5; // else...
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(Config.intakeYawLeft, Bot.intakeClaw.yawServo.getPos(), 0.01);

        systemUnderTest._gamepad2.left_stick_x = (float) 0.5; // else...
        systemUnderTest.runLoop(); // only run runloop after initialize

        // check to see if claw position is correct
        Assert.assertEquals(Config.intakeYawRight, Bot.intakeClaw.yawServo.getPos(), 0.01);
    }

    // TODO: - Slide Arm - Test misumi wrist for each button ****
    // *** these next 3 button are either or and if all together then Y is first, A is second and X is last
    @Test
    public void test_Driver2_SlideArm_Y_ButtonsPressed() {
        // Y button
        systemUnderTest._gamepad2.y = true;
        systemUnderTest.runOpMode();

        // check to see if slide target position is accurate
        // *** note motor 1 is negative
        Assert.assertEquals(-Config.slideHighBasket, Bot.slideArm.getMotor1().getTargetPosition(), 0.01);
        Assert.assertEquals(Config.slideHighBasket, Bot.slideArm.getMotor2().getTargetPosition(), 0.01);
        systemUnderTest._gamepad2.y = false;
    }

    @Test
    public void test_Driver2_SlideArm_A_ButtonsPressed() {
        // A button
        systemUnderTest.initalize();

        // **** Note this has a limiter at the bottom so the slide MUST be up in order for it to go down****
        int turnOffThreshold = Config.slideDown + 50;
        Bot.slideArm.setTestingOnlyMotor1Position(turnOffThreshold + 1); // anything over Threshold should turn on the motor
        Bot.slideArm.setTestingOnlyMotor2Position(turnOffThreshold + 1); // anything over Threshold should turn on the motor

        systemUnderTest._gamepad2.a = true;
        systemUnderTest.runLoop();

        // check to see if slide target position is accurate
        // *** note motor 1 is negative
        Assert.assertEquals(-Config.slideDown, Bot.slideArm.getMotor1().getTargetPosition(), 0.01);
        Assert.assertEquals(Config.slideDown, Bot.slideArm.getMotor2().getTargetPosition(), 0.01);
        systemUnderTest._gamepad2.a = false;
    }

    @Test
    public void test_Driver2_SlideArm_X_ButtonsPressed() {
        systemUnderTest._gamepad2.x = true;
        systemUnderTest.runOpMode();

        // check to see if slide target position is accurate
        // *** note motor 1 is negative
        Assert.assertEquals(-Config.slideLowBasket, Bot.slideArm.getMotor1().getTargetPosition(), 0.01);
        Assert.assertEquals(Config.slideLowBasket, Bot.slideArm.getMotor2().getTargetPosition(), 0.01);
        systemUnderTest._gamepad2.x = false;
    }


    @Test
    public void test_Driver2_Bumpers_ButtonPressed() {
        // either or, but if both pressed box goes up ****
        systemUnderTest._gamepad2.left_bumper = true;
        systemUnderTest.runOpMode();

        // check to see if ocg box is pitch up
        Assert.assertEquals(true, Bot.ocgBox.pitchState);

        systemUnderTest._gamepad2.left_bumper = false;
        systemUnderTest._gamepad2.right_bumper = true;
        systemUnderTest.runOpMode();

        // check to see if ocg box is pitch down
        Assert.assertEquals(false, Bot.ocgBox.pitchState);
    }


}
