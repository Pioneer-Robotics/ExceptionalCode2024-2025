package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Konami {
    LinearOpMode opMode;
    public Konami(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public void checkUp() throws Exception {
        while(true) {
            if(opMode.gamepad2.dpad_up) {break;}
            else if (opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_left || opMode.gamepad2.dpad_down || opMode.gamepad2.a || opMode.gamepad2.b || opMode.gamepad2.x || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.dpad_up) {continue;}
    }

    public void checkRight() throws Exception {
        while(true) {
            if(opMode.gamepad2.dpad_right) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_left || opMode.gamepad2.dpad_down || opMode.gamepad2.a || opMode.gamepad2.b || opMode.gamepad2.x || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.dpad_right) {continue;}
    }

    public void checkLeft() throws Exception {
        while(true) {
            if(opMode.gamepad2.dpad_left) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_down || opMode.gamepad2.a || opMode.gamepad2.b || opMode.gamepad2.x || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.dpad_left) {continue;}
    }

    public void checkDown() throws Exception {
        while(true) {
            if(opMode.gamepad2.dpad_down) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_left || opMode.gamepad2.a || opMode.gamepad2.b || opMode.gamepad2.x || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.dpad_down) {continue;}
    }

    public void checkA() throws Exception {
        while(true) {
            if(opMode.gamepad2.a) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_left || opMode.gamepad2.dpad_down || opMode.gamepad2.b || opMode.gamepad2.x || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.a) {continue;}
    }

    public void checkB() throws Exception {
        while(true) {
            if(opMode.gamepad2.b) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_left || opMode.gamepad2.dpad_down || opMode.gamepad2.a || opMode.gamepad2.x || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.b) {continue;}
    }

    public void checkX() throws Exception {
        while(true) {
            if(opMode.gamepad2.x) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_left || opMode.gamepad2.dpad_down || opMode.gamepad2.a || opMode.gamepad2.b || opMode.gamepad2.y) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.x) {continue;}
    }

    public void checkY() throws Exception {
        while(true) {
            if(opMode.gamepad2.y) {break;}
            else if (opMode.gamepad2.dpad_up || opMode.gamepad2.dpad_right || opMode.gamepad2.dpad_left || opMode.gamepad2.dpad_down || opMode.gamepad2.a || opMode.gamepad2.b || opMode.gamepad2.x) {throw new Exception("Incorrect Passcode");}
        }
        while(opMode.gamepad2.y) {continue;}
    }



    public void konamiEasy() throws Exception {
        checkUp();
        checkRight();
        checkUp();
        checkLeft();
        checkDown();
    }

    public void konamiHard() throws Exception {
        checkB();
        checkB();
        checkUp();
        checkLeft();
        checkB();
        checkY();
        checkY();
        checkX();
        checkUp();
        checkDown();
        checkB();
        checkA();
        checkLeft();
        checkRight();
        checkDown();
        checkRight();
        checkDown();
        checkB();
        checkUp();
        checkA();
    }


}
