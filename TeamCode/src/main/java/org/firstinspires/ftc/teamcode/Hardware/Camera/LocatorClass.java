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

import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LocatorClass {
    List<ColorBlobLocatorProcessor.Blob> blobs;
    ColorBlobLocatorProcessor.Blob biggestBlob;
    ColorBlobLocatorProcessor colorLocator;
    Point[] boxPoints = new Point[]{new Point(0,0), new Point(0,0), new Point(0,0), new Point(0,0)};
    double[] blobPos;
    int contPointLen;
    ArrayList<Double> xPoints, yPoints = new ArrayList<Double>();
    ArrayList<ArrayList<Double>> arrayBoxPoints = new ArrayList<ArrayList<Double>>();
    double minX, maxX, minY, maxY, boxX, boxY, dX, dY, sampleTheta;
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

//    public ColorBlobLocatorProcessor.Blob biggestBlob(){
//        getBlobsList();
//        try{
//            return (blobs.get(0));
//        } catch (Exception e){
//            return(n);
//        }
//    }

//    public boolean checkIfEmpty(){
//        blobs = colorLocator.getBlobs();
//
//        if (blobs.toArray().length == 0) {
//            blobs = colorLocator.getBlobs();
//        }
//    }

    public double[] getBlobPos(){
        getBlobsList();
        blobPos = new double[]{blobs.get(0).getBoxFit().center.x, blobs.get(0).getBoxFit().center.y};
        try{
            return (blobPos);
        } catch (Exception e){
            return (new double[]{0,0});
        }
    }

    public double blobPosX(){
        getBlobsList();
        return (getBlobPos()[0]);
    }

    public double blobPosY(){
        getBlobsList();
        return (getBlobPos()[1]);
    }

    public int blobCount(){
        getBlobsList();
        try{
            return (blobs.toArray().length);
        } catch (Exception e){
            return (0);
        }
    }

    public Point[] getBoxPoints(){
        getBlobsList();
        if (!blobs.isEmpty()) {
            int i = 0;
            blobs.get(0).getBoxFit().points(boxPoints);
//            for (Point p : boxPoints){            //Used for testing, makes reading points easier
//                boxPoints[i].x = Math.rint(boxPoints[i].x);
//                boxPoints[i].y = Math.rint(boxPoints[i].y);
//
//                i +=1;
//
//            }
            return(boxPoints);
        } else {
            return new Point[]{new Point(0,0), new Point(0,0), new Point(0,0), new Point(0,0)};
        }
    }

    public void sortBoxPoints(){
        getBoxPoints();
        for (Point p : boxPoints){
            int i = 0;
            xPoints.add(p.x);
            yPoints.add(p.y);
        }
//Sort box points by x and y values to get which points are farthest left/right or up/down
    }

    public ArrayList<Double> getXPoints(){
        sortBoxPoints();
        return (xPoints);
    }

    public ArrayList<Double> getYPoints(){
        sortBoxPoints();
        return (yPoints);
    }

//    public double[] getStraightBoxFit(){
//        getBlobsList();
//        contourPoints = getPoints();
//        contPointLen = contourPoints.length;
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
//
//        try{
//            return (new double[]{boxX,boxY});
//        } catch (Exception e){
//            return (new double[]{0,0});
//        }
//    }

    public double getSampleTheta(){
        getBoxPoints();
        dX = boxPoints[0].x - boxPoints[1].x;
        dY = boxPoints[0].y - boxPoints[1].y;
        //Atan2 has args (y,x), flipped for use case
        sampleTheta = Math.atan2(dX, dY);
        try{
            return (sampleTheta);
        } catch (Exception e) {
            return (0);
        }
    }

    public int numContPoints(){
        blobs = getBlobsList();
        boxPoints = getBoxPoints();
        contPointLen = boxPoints.length;
//        getStraightBoxFit();
        try {
            return(contPointLen);
        } catch (Exception e){
            return(0);
        }
    }

    public VisionPortal getCameraStream() {
        return portal;
    }
}
