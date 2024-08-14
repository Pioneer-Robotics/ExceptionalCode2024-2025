package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.teamcode.Bot;

/**
 * Used to store groups of instructions for the bot
 * Used in both auto and teleop
 */
public class Commands {
    public static void armUp() {
        Bot.gripper.closeServo();
        Bot.slide.moveToPosition(1);
        Bot.wrist.openServo();
    }

    public static void armMid() {
        Bot.gripper.closeServo();
        Bot.slide.moveToPosition(0.5);
        Bot.wrist.openServo();
    }

    public static void armDown() {
        Bot.slide.moveToPosition(0);
        Bot.wrist.closeServo();
        Bot.gripper.openServo();
    }
}
