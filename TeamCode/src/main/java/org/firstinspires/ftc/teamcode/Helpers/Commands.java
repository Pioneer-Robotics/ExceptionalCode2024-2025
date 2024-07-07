package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.teamcode.Bot;

/**
 * Used to store groups of instructions for the bot
 * Used in both auto and teleop
 */
public class Commands {
    private final Bot bot;

    public Commands(Bot bot) {
        this.bot = bot;
    }

    public void armUp() {
        bot.gripper.closeServo();
        bot.slide.moveToPosition(1);
        bot.wrist.openServo();
    }

    public void armMid() {
        bot.gripper.closeServo();
        bot.slide.moveToPosition(0.5);
        bot.wrist.openServo();
    }

    public void armDown() {
        bot.slide.moveToPosition(0);
        bot.wrist.closeServo();
        bot.gripper.openServo();
    }
}
