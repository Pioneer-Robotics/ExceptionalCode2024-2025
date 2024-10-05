package org.firstinspires.ftc.teamcode.Helpers;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Konami {
    Gamepad gamepad;

    public Konami(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    private void checkUp() throws Exception {
        while(true) {
            if (gamepad.dpad_up) {
                break;
            } else if (gamepad.dpad_right || gamepad.dpad_left || gamepad.dpad_down || gamepad.a || gamepad.b || gamepad.x || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.dpad_up) {
        }
    }

    private void checkRight() throws Exception {
        while(true) {
            if (gamepad.dpad_right) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_left || gamepad.dpad_down || gamepad.a || gamepad.b || gamepad.x || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.dpad_right) {
        }
    }

    private void checkLeft() throws Exception {
        while(true) {
            if (gamepad.dpad_left) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_right || gamepad.dpad_down || gamepad.a || gamepad.b || gamepad.x || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.dpad_left) {
        }
    }

    private void checkDown() throws Exception {
        while(true) {
            if (gamepad.dpad_down) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_right || gamepad.dpad_left || gamepad.a || gamepad.b || gamepad.x || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.dpad_down) {
        }
    }

    private void checkA() throws Exception {
        while(true) {
            if (gamepad.a) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_right || gamepad.dpad_left || gamepad.dpad_down || gamepad.b || gamepad.x || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.a) {
        }
    }

    private void checkB() throws Exception {
        while(true) {
            if (gamepad.b) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_right || gamepad.dpad_left || gamepad.dpad_down || gamepad.a || gamepad.x || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.b) {
        }
    }

    private void checkX() throws Exception {
        while(true) {
            if (gamepad.x) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_right || gamepad.dpad_left || gamepad.dpad_down || gamepad.a || gamepad.b || gamepad.y) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.x) {
        }
    }

    private void checkY() throws Exception {
        while(true) {
            if (gamepad.y) {
                break;
            } else if (gamepad.dpad_up || gamepad.dpad_right || gamepad.dpad_left || gamepad.dpad_down || gamepad.a || gamepad.b || gamepad.x) {
                throw new Exception("Incorrect Passcode");
            }
        }
        while (gamepad.y) {
        }
    }

    public void konamiEasy() throws Exception {
        checkUp();
        checkUp();
        checkDown();
        checkDown();
        checkLeft();
        checkRight();
        checkLeft();
        checkRight();
        checkB();
        checkA();
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
