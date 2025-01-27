package org.firstinspires.ftc.teamcode.OpModes.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

@Autonomous(name = "Sample Auto", group = "Autos")
public class SampleAuto extends LinearOpMode {

    public void runOpMode() {
        Bot.init(this);

    }
}
