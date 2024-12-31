package org.firstinspires.ftc.teamcode.Hardware.Camera;

import static java.lang.String.format;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocatorClass {
    List<ColorBlobLocatorProcessor.Blob> blobs;
    ColorBlobLocatorProcessor.Blob bigBlob;
    ColorBlobLocatorProcessor colorLocator;
    Point[] contourPoints;
    Point testPoint;
    int contPointLen;
    ArrayList<Double> xPoints, yPoints = new ArrayList<Double>();
    double minX, maxX, minY, maxY, boxX, boxY;
    VisionPortal portal;
    public LocatorClass(ColorRange targetColorRange, LinearOpMode opMode){

        colorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(targetColorRange)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 1, 1, -1))
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .build();

        portal = new VisionPortal.Builder()
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();
    }

    public List<ColorBlobLocatorProcessor.Blob> getBlobsList(){
//        if (!blobs.isEmpty()){
//            blobs = colorLocator.getBlobs();
//        ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);  // filter out very small blobs.
//        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs); // Largest blob is first (hopefully correct one)
//        }
        blobs = colorLocator.getBlobs();

        if (blobs.toArray().length == 0) {
            blobs = colorLocator.getBlobs();
        }

        ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);  // filter out very small blobs.
        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs); // Largest blob is first (hopefully correct one)

        return (blobs);
    }

//    public boolean checkIfEmpty(){
//        blobs = colorLocator.getBlobs();
//
//        if (blobs.toArray().length == 0) {
//            blobs = colorLocator.getBlobs();
//        }
//    }

    public double[] blobPos(){
        getBlobsList();
        return (new double[]{blobs.get(0).getBoxFit().center.x, blobs.get(0).getBoxFit().center.y});
    }

    public double blobPosX(){
        getBlobsList();
        return (blobs.get(0).getBoxFit().center.x);
    }

    public double blobPosY(){
        getBlobsList();
        return (blobs.get(0).getBoxFit().center.y);
    }

    public double getSpecRatio(){
        getBlobsList();
        return (blobs.get(0).getDensity());
    }

    public int blobCount(){
        getBlobsList();
        try{
            return (blobs.toArray().length);
        } catch (Exception e){
            return (0);
        }
    }

    public Point[] getPoints(){
        getBlobsList();
        //        Arrays.stream(Arrays.stream(blobs.get(0).getContourPoints()).toArray());
//        blobs.get(0).getContourPoints();
//        return (Arrays) Arrays.stream(Arrays.stream(blobs.get(0).getContourPoints()).toArray());
        try{
            return (blobs.get(0).getContourPoints());
        } catch (Exception e){
            return(new Point[] {new Point(0,0),new Point(0,0)});
        }
    }

    public MatOfPoint getContourLine(){
        getBlobsList();
        try{
            return (blobs.get(0).getContour());
        } catch (Exception e){
            return (new MatOfPoint());
        }
    }

//    public double[] getStraightBoxFit(){
//        blobs = getBlobsList();
//        contourPoints = getPoints();
//        contPointLen = contourPoints.length;
//        testPoint = contourPoints[0];
//
//        for(Point points : contourPoints){
//            xPoints.add(points.x);
//            yPoints.add(points.y);
//        }
//
//        xPoints.sort(Collections.reverseOrder());
//        yPoints.sort(Collections.reverseOrder());
//
//        minX = xPoints.get(0);
//        maxX = xPoints.get(contPointLen-1);
//
//        minY = yPoints.get(0);
//        maxY = yPoints.get(contPointLen-1);
//
//        boxX = maxX-minX;
//        boxY = maxY-minY;
//
//        try{
//            return (new double[]{boxX,boxY});
//        } catch (Exception e){
//            return (new double[]{0});
//        }
//    }

    public int numContPoints(){
        blobs = getBlobsList();
        contourPoints = getPoints();
        contPointLen = contourPoints.length;
//        getStraightBoxFit();
        try {
            return(contPointLen);
        } catch (Exception e){
            return(0);
        }
    }

}
