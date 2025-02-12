import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.SpecimenArm;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSpecimenArm {

    // Properties
    SpecimenArm systemUnderTest;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        systemUnderTest = new SpecimenArm(true);

    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void testMove() {
        double speed = 0.25;
        systemUnderTest.move(speed);

        Assert.assertEquals(speed * Config.maxSpecTicksPerSecond, systemUnderTest.getMotor().getVelocity(), 0.01);
    }

    @Test
    public void testMoveToPos() {
        int positionTicks = 10;
        double speed = 0.25;
        systemUnderTest.moveToPos(positionTicks, speed);

        Assert.assertEquals(positionTicks, systemUnderTest.getMotor().getTargetPosition(), 0.01);
        Assert.assertEquals(Config.maxSlideTicksPerSecond * speed, systemUnderTest.getMotor().getVelocity(), 0.01);
    }

    @Test
    public void testMovePreHang() {
        double speed = 0.25;
        systemUnderTest.movePrepHang(speed);

        Assert.assertEquals(speed * Config.maxSpecTicksPerSecond, systemUnderTest.getMotor().getVelocity(), 0.01);
        Assert.assertEquals(0, systemUnderTest.getPosition(), 0.01);
    }

    @Test
    public void testMovePostHang() {
        double speed = 0.25;
        systemUnderTest.movePostHang(speed);

//        Assert.assertEquals(speed * Config.maxSpecTicksPerSecond, systemUnderTest.getMotor().getVelocity(), 0.01);
        Assert.assertEquals(1, systemUnderTest.getPosition(), 0.01);
    }

    @Test
    public void testMoveToCollect() {
        double speed = 0.25;
        systemUnderTest.moveToCollect(speed);

        // move to collect is negative
        Assert.assertEquals(speed * Config.maxSpecTicksPerSecond, -systemUnderTest.getMotor().getVelocity(), 0.01);
        Assert.assertEquals(2, systemUnderTest.getPosition(), 0.01);
    }

}
