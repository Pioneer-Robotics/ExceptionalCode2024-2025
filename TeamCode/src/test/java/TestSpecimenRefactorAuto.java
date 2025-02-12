import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.OpModes.Autos.SpecimenRefactorAuto;
import org.firstinspires.ftc.teamcode.TestingMocks.fakes.FakeTelemetry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSpecimenRefactorAuto {

    // Properties
    SpecimenRefactorAuto systemUnderTest;

    // Setup & Tear Down
    @Before
    public void setUp() throws Exception {
        Bot.isUnitTest = true;
        systemUnderTest = new SpecimenRefactorAuto();
        systemUnderTest.unitTestIsActive = true;
        FakeTelemetry telemetry = new FakeTelemetry();
        systemUnderTest._telemetry = telemetry;
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests
    @Test
    public void testRunWholeAuto() {
        systemUnderTest.runOpMode();
        Assert.assertTrue(true);
    }

    @Test
    public void testInitState() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState();
        Assert.assertEquals(SpecimenRefactorAuto.State.SPECIMEN_HANG_2, systemUnderTest.state);
    }

    @Test
    public void testHangState() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // hang is dependant on init state
        systemUnderTest.handleHang2State();
        Assert.assertEquals(SpecimenRefactorAuto.State.PUSH_SAMPLE_1, systemUnderTest.state);
    }

    @Test
    public void testPushSample1State() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // all are dependant on init state
        systemUnderTest.handlePushSampleOne();
        Assert.assertEquals(SpecimenRefactorAuto.State.PUSH_SAMPLE_2, systemUnderTest.state);

        // should tell the robot to set the speed to 0.85
        Assert.assertEquals(0.85, Bot.purePursuit.currentSpeedForUnitTest, 0.01);
    }

    @Test
    public void testPushSample2State() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // all are dependant on init state
        systemUnderTest.handlePushSampleTwo();
        Assert.assertEquals(SpecimenRefactorAuto.State.PUSH_SAMPLE_3, systemUnderTest.state);

        // should tell the robot to set the speed to 0.8
        Assert.assertEquals(0.8, Bot.purePursuit.currentSpeedForUnitTest, 0.01);
    }

    @Test
    public void testPushSample3State() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // all are dependant on init state
        systemUnderTest.handlePushSampleThree();
        Assert.assertEquals(SpecimenRefactorAuto.State.COLLECT_SPECIMEN_1, systemUnderTest.state);

        // should tell the robot to set the speed to 0.75
        Assert.assertEquals(0.75, Bot.purePursuit.currentSpeedForUnitTest, 0.01);
    }

    @Test
    public void testSpecimen1State() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // all are dependant on init state
        systemUnderTest.handleCollectSpecimenOne();
        Assert.assertEquals(SpecimenRefactorAuto.State.COLLECT_SPECIMEN_2, systemUnderTest.state);

        // should tell the robot to set the speed to 0.55
        Assert.assertEquals(0.55, Bot.purePursuit.currentSpeedForUnitTest, 0.01);

    }

    @Test
    public void testSpecimen2State() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // all are dependant on init state
        systemUnderTest.handleSpecimenTwo();
        Assert.assertEquals(SpecimenRefactorAuto.State.SPECIMEN_HANG_2, systemUnderTest.state);
    }

    @Test
    public void testHang2MiddleAttemptsState() {
        systemUnderTest.initalize();
        systemUnderTest.handleInitState(); // all are dependant on init state

        systemUnderTest.handleHang2State(); // 1st time goes to PUSH_SAMPLE_1
        Assert.assertEquals(4, systemUnderTest.hang_number, 0.01);

        systemUnderTest.handleSpecimenTwo(); // this should lower the hang_state variable
        systemUnderTest.handleHang2State();
        Assert.assertEquals(3, systemUnderTest.hang_number, 0.01);
        Assert.assertEquals(SpecimenRefactorAuto.State.COLLECT_SPECIMEN_1, systemUnderTest.state);

        systemUnderTest.handleSpecimenTwo(); // this should lower the hang_state variable
        systemUnderTest.handleHang2State();
        Assert.assertEquals(2, systemUnderTest.hang_number, 0.01);
        Assert.assertEquals(SpecimenRefactorAuto.State.COLLECT_SPECIMEN_1, systemUnderTest.state);

        systemUnderTest.handleSpecimenTwo(); // this should lower the hang_state variable
        systemUnderTest.handleHang2State();
        Assert.assertEquals(1, systemUnderTest.hang_number, 0.01);
        Assert.assertEquals(SpecimenRefactorAuto.State.COLLECT_SPECIMEN_1, systemUnderTest.state);

        systemUnderTest.handleSpecimenTwo(); // this should lower the hang_state variable
        systemUnderTest.handleHang2State();
        Assert.assertEquals(0, systemUnderTest.hang_number, 0.01);
        Assert.assertEquals(SpecimenRefactorAuto.State.PARK, systemUnderTest.state);
    }

    @Test
    public void testParkState() {
        systemUnderTest.initalize();
        systemUnderTest.hang_number = 0;
        systemUnderTest.handleInitState(); // all are dependant on init state
        systemUnderTest.handleHang2State();
        Assert.assertEquals(0, systemUnderTest.hang_number, 0.01);
        Assert.assertEquals(SpecimenRefactorAuto.State.PARK, systemUnderTest.state);
    }

}
