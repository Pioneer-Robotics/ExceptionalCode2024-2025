package org.firstinspires.ftc.teamcode.Helpers;

import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ColorSpace;
import org.opencv.core.Scalar;

public class betterColorRange {
    protected final ColorSpace colorSpace;
    protected final Scalar min;
    protected final Scalar max;

    public betterColorRange(ColorSpace colorSpace, Scalar min, Scalar max) {
        this.colorSpace = colorSpace;
        this.min = min;
        this.max = max;
    }

    public static final ColorRange BLUE = new ColorRange(
            ColorSpace.HSV,
            new Scalar( 2, 77, 30),
            new Scalar(2, 77, 95)
    );

    public static final ColorRange RED = new ColorRange(
            ColorSpace.HSV,
            new Scalar( 229, 88,  50),
            new Scalar(229, 88, 100)
    );

    public static final ColorRange YELLOW = new ColorRange(
            ColorSpace.HSV,
            new Scalar( 32, 128,   0),
            new Scalar(255, 170, 120)
    );
}

