import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Config;
import org.firstinspires.ftc.teamcode.Hardware.IntakeClaw;
import org.firstinspires.ftc.teamcode.OpModes.Autos.PreFlightCheckTimers;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeTelemetry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPreFlightCheckTimers {

    // Properties
    PreFlightCheckTimers systemUnderTest;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        Bot.isUnitTest = true;
        systemUnderTest = new PreFlightCheckTimers();
        systemUnderTest.unitTestIsActive = true;
        systemUnderTest._telemetry = new FakeTelemetry();
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void testRunAuto() {
        systemUnderTest.runOpMode();
        Assert.assertTrue(true);
    }
}
