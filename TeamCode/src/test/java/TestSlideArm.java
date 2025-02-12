import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.SlideArm;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeDrive.FakeDcMotorEx;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSlideArm {

    // Properties
    SlideArm systemUnderTest;

    FakeDcMotorEx sutMotor1;
    FakeDcMotorEx sutMotor2;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        systemUnderTest = new SlideArm(true);

        sutMotor1 = (FakeDcMotorEx) systemUnderTest.getMotor1();
        sutMotor2 = (FakeDcMotorEx) systemUnderTest.getMotor2();

        // FIXME: Is the motor enabled at first or not ????
        sutMotor1.setMotorDisable();
        sutMotor2.setMotorDisable();
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void testDPadPressed_Up() {
        systemUnderTest.getMotor1().setVelocity(0.25 * Config.maxSlideTicksPerSecond);
        systemUnderTest.getMotor2().setVelocity(0.25 * Config.maxSlideTicksPerSecond);

        Assert.assertEquals(0.25 * Config.maxSlideTicksPerSecond, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(0.25 * Config.maxSlideTicksPerSecond, sutMotor2.getVelocity(), 0.01);
    }

    @Test
    public void testDPadDown_Pressed() {
        systemUnderTest.getMotor1().setPower(-0.25 * Config.maxSlideTicksPerSecond);
        systemUnderTest.getMotor2().setPower(-0.25 * Config.maxSlideTicksPerSecond);

        Assert.assertEquals(-0.25 * Config.maxSlideTicksPerSecond, sutMotor1.getPower(), 0.01);
        Assert.assertEquals(-0.25 * Config.maxSlideTicksPerSecond, sutMotor2.getPower(), 0.01);
    }

    @Test
    public void test_MoveUp() {
        double speed = 0.25;
        systemUnderTest.moveUp(speed);

        // Motor 1 should be a negative number so it spin the opposite way
        Assert.assertEquals(-Config.maxSlideTicksPerSecond * speed, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(Config.maxSlideTicksPerSecond * speed, sutMotor2.getVelocity(), 0.01);

        // Motor 1 should be a negative number so it spin the opposite way
        Assert.assertEquals(-Config.slideHighBasket, sutMotor1.getTargetPosition(), 0.01);
        Assert.assertEquals(Config.slideHighBasket, sutMotor2.getTargetPosition(), 0.01);
    }

    @Test
    public void test_moveDown() {
        int turnOffThreshold = Config.slideDown + 50;
        systemUnderTest.setTestingOnlyMotor1Position(turnOffThreshold + 1); // anything over Threshold should turn on the motor
        systemUnderTest.setTestingOnlyMotor2Position(turnOffThreshold + 1); // anything over Threshold should turn on the motor

        double speed = 0.25;
        systemUnderTest.moveDown(speed);

        // Motor 1 should be a negative number so it spin the opposite way
        Assert.assertEquals(-Config.maxSlideTicksPerSecond * speed, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(Config.maxSlideTicksPerSecond * speed, sutMotor2.getVelocity(), 0.01);

        // Motor 1 should be a negative number so it spin the opposite way
        Assert.assertEquals(-Config.slideDown, sutMotor1.getTargetPosition(), 0.01);
        Assert.assertEquals(Config.slideDown, sutMotor2.getTargetPosition(), 0.01);
    }

    @Test
    public void test_moveMid() {
        double speed = 0.25;
        systemUnderTest.moveMid(speed);

        // Motor 1 should be a negative number so it spin the opposite way
        Assert.assertEquals(-Config.maxSlideTicksPerSecond * speed, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(Config.maxSlideTicksPerSecond * speed, sutMotor2.getVelocity(), 0.01);

        // Motor 1 should be a negative number so it spin the opposite way
        Assert.assertEquals(-Config.slideLowBasket, sutMotor1.getTargetPosition(), 0.01);
        Assert.assertEquals(Config.slideLowBasket, sutMotor2.getTargetPosition(), 0.01);
    }

    @Test
    public void test_motorOffIfSlideIsDown() {
        double speed = 0.25;

        int turnOffThreshold = Config.slideDown + 50;

        // if either are over then the test passes - both must over for test to fail
        // Both Under
        systemUnderTest.setTestingOnlyMotor1Position(turnOffThreshold - 1); // anything under Threshold should turn off the motor
        systemUnderTest.setTestingOnlyMotor2Position(turnOffThreshold - 1); // anything under Threshold should turn off the motor

        systemUnderTest.moveDown(speed);

        // Motor should be NOT be enabled
        Assert.assertFalse(sutMotor1.isMotorEnabled());
        Assert.assertFalse(sutMotor2.isMotorEnabled());
        // Power is set to 0
        Assert.assertEquals(0, sutMotor1.getPower(), 0.01);
        Assert.assertEquals(0, sutMotor2.getPower(), 0.01);

        // Velocity is set to 0
        Assert.assertEquals(0, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(0, sutMotor2.getVelocity(), 0.01);

        // Motor 1 over
        systemUnderTest.setTestingOnlyMotor1Position(turnOffThreshold + 1); // anything under Threshold should turn off the motor
        systemUnderTest.setTestingOnlyMotor2Position(turnOffThreshold - 1); // anything under Threshold should turn off the motor
        // if either are over then the test passes - both must over for test to fail

        systemUnderTest.moveDown(speed);

        // Motor should be NOT be enabled
        Assert.assertFalse(sutMotor1.isMotorEnabled());
        Assert.assertFalse(sutMotor2.isMotorEnabled());
        // Power is set to 0
        Assert.assertEquals(0, sutMotor1.getPower(), 0.01);
        Assert.assertEquals(0, sutMotor2.getPower(), 0.01);

        // Velocity is set to 0
        Assert.assertEquals(0, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(0, sutMotor2.getVelocity(), 0.01);

        // Motor 2 over
        systemUnderTest.setTestingOnlyMotor1Position(turnOffThreshold - 1); // anything under Threshold should turn off the motor
        systemUnderTest.setTestingOnlyMotor2Position(turnOffThreshold + 1); // anything under Threshold should turn off the motor
        // if either are over then the test passes - both must over for test to fail

        systemUnderTest.moveDown(speed);

        // Motor should be NOT be enabled
        Assert.assertFalse(sutMotor1.isMotorEnabled());
        Assert.assertFalse(sutMotor2.isMotorEnabled());
        // Power is set to 0
        Assert.assertEquals(0, sutMotor1.getPower(), 0.01);
        Assert.assertEquals(0, sutMotor2.getPower(), 0.01);

        // Velocity is set to 0
        Assert.assertEquals(0, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(0, sutMotor2.getVelocity(), 0.01);
    }

    @Test
    public void test_motorOnWhenMovedDownAbove50() {
        double speed = 0.25;

        int turnOffThreshold = Config.slideDown + 50;
        systemUnderTest.setTestingOnlyMotor1Position(turnOffThreshold + 1); // anything over Threshold should turn on the motor
        systemUnderTest.setTestingOnlyMotor2Position(turnOffThreshold + 1); // anything over Threshold should turn on the motor

        systemUnderTest.moveDown(speed);

        // Motor should be Enabled
        Assert.assertTrue(sutMotor1.isMotorEnabled());
        Assert.assertTrue(sutMotor2.isMotorEnabled());

        // Velocity is NOT set to 0
        Assert.assertNotEquals(0, sutMotor1.getVelocity(), 0.01);
        Assert.assertNotEquals(0, sutMotor2.getVelocity(), 0.01);
    }

    @Test
    public void test_move() {
        double power = 0.25;

        systemUnderTest.move(power);
        Assert.assertEquals(-power * Config.maxSlideTicksPerSecond, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(power * Config.maxSlideTicksPerSecond, sutMotor2.getVelocity(), 0.01);

        power = -0.25;
        systemUnderTest.move(power);
        Assert.assertEquals(-power * Config.maxSlideTicksPerSecond, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(power * Config.maxSlideTicksPerSecond, sutMotor2.getVelocity(), 0.01);

        power = 1.0;
        systemUnderTest.move(power);
        Assert.assertEquals(-power * Config.maxSlideTicksPerSecond, sutMotor1.getVelocity(), 0.01);
        Assert.assertEquals(power * Config.maxSlideTicksPerSecond, sutMotor2.getVelocity(), 0.01);
    }

}
