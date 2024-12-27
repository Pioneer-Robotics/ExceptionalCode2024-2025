package org.firstinspires.ftc.teamcode.Hardware.Camera;

import android.util.Size;

import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import org.firstinspires.ftc.teamcode.Bot;

import java.util.List;

public class SampleLocator {
    private List<ColorBlobLocatorProcessor.Blob> blobs;
    ColorBlobLocatorProcessor blueLocator = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(ColorRange.BLUE)         // use a predefined color match
            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
            .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 1, 1, -1))  // search central 1/4 of camera view
            .setDrawContours(true)                        // Show contours on the Stream Preview
            .setBlurSize(5)                               // Smooth the transitions between different colors in image
            .build();

    VisionPortal portal = new VisionPortal.Builder()
            .addProcessor(blueLocator)
            .setCameraResolution(new Size(320, 240))
            .setCamera(Bot.opMode.hardwareMap.get(WebcamName.class, "Webcam 1"))
            .build();

    public void getBlobs(){
        blobs = blueLocator.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);  // filter out very small blobs.
        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs); // Largest blob is first (hopefully correct one)
    }

    public double[] blobPos(){
        getBlobs();
        return (new double[]{blobs.get(0).getBoxFit().center.x, blobs.get(0).getBoxFit().center.y});
    }

    public double blobPosX(){
        getBlobs();
        return (blobs.get(0).getBoxFit().center.x);
    }

    public double blobPosY(){
        getBlobs();
        return (blobs.get(0).getBoxFit().center.y);
    }

    public double getSpecRatio(){
        getBlobs();
        return (blobs.get(0).getDensity());
    }


}
