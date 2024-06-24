package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class VoltageHandler {
    private final HardwareMap.DeviceMapping<VoltageSensor> voltageSensors;

    public VoltageHandler(LinearOpMode opMode) {
        voltageSensors = opMode.hardwareMap.voltageSensor;
    }

    /**
     * Iterate over all of our voltageHandler sensor readings and return the lowest value
     *
     * @return the lowest voltageHandler
     */
    public double getVoltage() {
        double lowestVoltage = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : voltageSensors) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                lowestVoltage = Math.min(lowestVoltage, voltage);
            }
        }
        return lowestVoltage;
    }
}
