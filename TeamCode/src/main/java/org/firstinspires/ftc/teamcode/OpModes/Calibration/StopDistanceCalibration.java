package org.firstinspires.ftc.teamcode.OpModes.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

import java.io.FileWriter;
import java.io.IOException;
@Disabled
@Autonomous(name = "Stop Distance Calibration", group = "Calibration")
public class StopDistanceCalibration extends LinearOpMode {
    FileWriter fileWriter;

    enum State {
        DRIVE_FORWARD,
        DRIVE_BACKWARD,
        WAIT_FOR_STOP
    }

    State state = State.DRIVE_FORWARD;

    public void runOpMode() {
        try {
            fileWriter = new FileWriter("/sdcard/stopDistance.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fileWriter.write("Velocity (cm/s),Stop Distance (cm)");
            fileWriter.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Bot.init(this);
        double power = 0.05;
        double velocity = 0;

        waitForStart();
        while (opModeIsActive()) {
            switch (state) {
                case DRIVE_FORWARD:
                    Bot.mecanumBase.move(0, 1, 0, power);
                    if (Bot.pinpoint.getY() >= 50) {
                        velocity = Bot.pinpoint.getVelocity()[1];
                        Bot.mecanumBase.stop();
                        state = State.WAIT_FOR_STOP;
                    }
                    break;

                case DRIVE_BACKWARD:
                    Bot.mecanumBase.move(0, -1, 0, power);
                    if (Bot.pinpoint.getY() <= 0) {
                        velocity = Bot.pinpoint.getVelocity()[1];
                        Bot.mecanumBase.stop();
                        state = State.WAIT_FOR_STOP;
                    }
                    break;

                case WAIT_FOR_STOP:
                    if (Math.abs(Bot.pinpoint.getVelocity()[1]) < 0.1) {
                        if (velocity > 0) {
                            try {
                                fileWriter.write(velocity + "," + (Bot.pinpoint.getY() - 50));
                                fileWriter.write("\n");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            state = State.DRIVE_BACKWARD;
                        } else {
                            try {
                                fileWriter.write(velocity + "," + (-Bot.pinpoint.getY()));
                                fileWriter.write("\n");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            power += 0.1;
                            state = State.DRIVE_FORWARD;
                        }
                    }
                    break;
            }

            Bot.pinpoint.update();
            if (power > 0.89) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                terminateOpModeNow();
            }
        }
    }
}
