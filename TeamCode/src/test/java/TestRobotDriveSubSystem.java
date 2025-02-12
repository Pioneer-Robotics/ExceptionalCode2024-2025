import org.firstinspires.ftc.teamcode.OpModes.SubSystems.RobotDriveSubSystem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRobotDriveSubSystem {

    // Properties
    RobotDriveSubSystem objectUnderTest;

    double startSpeed;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        objectUnderTest = RobotDriveSubSystem.createTestInstance();
        startSpeed = objectUnderTest.maxSpeed;
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    // Test Reset IMU
    @Test
    public void testResetIMUPressedDownOneTime() {
        objectUnderTest.resetIMUAction(false);
        objectUnderTest.resetIMUAction(true);
        objectUnderTest.resetIMUAction(false);
        Assert.assertEquals(1, objectUnderTest.testNumberOfResetIMU);
    }

    @Test
    public void testResetIMUPressedDownTwoTimes() {
        objectUnderTest.resetIMUAction(true);
        objectUnderTest.resetIMUAction(false);
        objectUnderTest.resetIMUAction(true);
        Assert.assertEquals(2, objectUnderTest.testNumberOfResetIMU);
    }

    // Test Move Robot
    @Test
    public void testResetMoveRobot() {
        objectUnderTest.moveRobotAction(1.1, 2.2, 3.3);
        Assert.assertEquals(1.1, objectUnderTest.testPx, 0.01);
        Assert.assertEquals(2.2, objectUnderTest.testPy, 0.01);
        Assert.assertEquals(3.3, objectUnderTest.testTurn, 0.01);
    }

    // Test isNorth button
    @Test
    public void testIsNorthModeStartsOn() {
        Assert.assertEquals(true, objectUnderTest.testIsNorthMode());
    }

    @Test
    public void testIsNorthModeTappedOn() {
        // less than 0.8 of both buttons stats at default on position
        objectUnderTest.toggleForFieldCentricAction(0.5, 0.5);
        Assert.assertEquals(true, objectUnderTest.testIsNorthMode());
    }

    @Test
    public void testIsNorthModeTappedOff() {
        // tap once for off
        objectUnderTest.toggleForFieldCentricAction(1.0, 1.0);
        Assert.assertEquals(false, objectUnderTest.testIsNorthMode());
    }

    @Test
    public void testIsNorthModeTappedOneSideOnly() {
        // less than 0.8 of both buttons stats at default on position
        objectUnderTest.toggleForFieldCentricAction(1.0, 0.5);
        Assert.assertEquals(true, objectUnderTest.testIsNorthMode());
    }

    // Test of the increment and Decrement buttons
    @Test
    public void testIncrementSpeedOneTime() {
        // test 1 button press
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        Assert.assertEquals(startSpeed + 0.1, objectUnderTest.maxSpeed, 0.01);
    }

    @Test
    public void testIncrementSpeedTwoTimes() {
        // test 2 button press
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        Assert.assertEquals(startSpeed + (0.1 * 2), objectUnderTest.maxSpeed, 0.01);
    }

    @Test
    public void testIncrementSpeedTenTimes() {
        // test 10 button press
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        Assert.assertEquals(startSpeed + (0.1 * 10), objectUnderTest.maxSpeed, 0.01);
    }

    @Test
    public void testDecrementSpeedOneTime() {
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        Assert.assertEquals(startSpeed - 0.1,objectUnderTest.maxSpeed,  0.01);
    }

    @Test
    public void testDecrementSpeedTwoTimes() {
        // test 2 button press
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        Assert.assertEquals(startSpeed - (0.1 * 2), objectUnderTest.maxSpeed,  0.01);
    }

    @Test
    public void testDecrementSpeedTenTimes() {
        // test 10 button press
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        Assert.assertEquals(startSpeed - (0.1 * 10), objectUnderTest.maxSpeed, 0.01);
    }

    @Test
    public void testRandomSpeedChanges() {
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(false, true);
        objectUnderTest.toggleSpeedAction(false, false);

        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);
        objectUnderTest.toggleSpeedAction(true, false);
        objectUnderTest.toggleSpeedAction(false, false);

        Assert.assertEquals(startSpeed + (0.1 * 1), objectUnderTest.maxSpeed, 0.01);
    }

}
