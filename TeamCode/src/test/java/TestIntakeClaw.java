import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.IntakeClaw;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestIntakeClaw {

    // Properties
    IntakeClaw systemUnderTest;

    private final double yawServoMid = (Config.intakeYawLeft + Config.intakeYawRight) / 2;
    private final double yawServo45 = (yawServoMid + Config.intakeYawRight) / 2;
    private final double yawServoNeg45 = (yawServoMid + Config.intakeYawLeft) / 2;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        systemUnderTest = new IntakeClaw(true);

    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void testClawDown() {
        systemUnderTest.clawDown();
        Assert.assertEquals(Config.intakeRollDown, systemUnderTest.rollServo.getPos(), 0.01);
        Assert.assertEquals(yawServoMid, systemUnderTest.yawServo.getPos(), 0.01);
    }

    @Test
    public void testClawUp() {
        systemUnderTest.clawUp();
        Assert.assertEquals(Config.intakeRollUp, systemUnderTest.rollServo.getPos(), 0.01);
        Assert.assertEquals(yawServoMid, systemUnderTest.yawServo.getPos(), 0.01);
    }

    @Test
    public void testClaw90() {
        systemUnderTest.clawPos90();
        Assert.assertEquals(Config.intakeYawRight, systemUnderTest.yawServo.getPos(), 0.01);
    }

    @Test
    public void testClaw45() {
        systemUnderTest.clawPos45();
        Assert.assertEquals(yawServo45, systemUnderTest.yawServo.getPos(), 0.01);
    }

    @Test
    public void testClawPos0() {
        systemUnderTest.clawPos0();
        Assert.assertEquals(yawServoMid, systemUnderTest.yawServo.getPos(), 0.01);
    }

    @Test
    public void testClawNeg45() {
        systemUnderTest.clawNeg45();
        Assert.assertEquals(yawServoNeg45, systemUnderTest.yawServo.getPos(), 0.01);
    }

    @Test
    public void testClawNeg90() {
        systemUnderTest.clawNeg90();
        Assert.assertEquals(Config.intakeYawLeft, systemUnderTest.yawServo.getPos(), 0.01);
    }
}

