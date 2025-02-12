package org.firstinspires.ftc.teamcode.TestingMocks.fakes;

import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import org.firstinspires.ftc.teamcode.SelfDrivingAuto.GoBildaPinpointDriver;

public class FakeGoBildaPinpointDriver extends GoBildaPinpointDriver {
    public FakeGoBildaPinpointDriver(I2cDeviceSynchSimple deviceClient, boolean deviceClientIsOwned) {
        super(new FakeI2cDeviceSynchSimple(), deviceClientIsOwned);
    }
}