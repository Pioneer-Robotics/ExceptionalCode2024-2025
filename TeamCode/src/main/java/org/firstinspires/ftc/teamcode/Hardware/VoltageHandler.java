package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.Bot;

public class VoltageHandler {
    private final HardwareMap.DeviceMapping<VoltageSensor> voltageSensors;

    public VoltageHandler() {
        voltageSensors = Bot.opMode.hardwareMap.voltageSensor;
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
