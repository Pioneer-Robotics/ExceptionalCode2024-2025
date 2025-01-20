package org.firstinspires.ftc.teamcode.Hardware.Camera;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Helpers.TrueAngle;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import org.opencv.core.Point;

import java.util.List;

import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;

public class LocatorClass {
    List<ColorBlobLocatorProcessor.Blob> blobs;
    ColorBlobLocatorProcessor colorLocator;
    Point maxYPoint, minYPoint, maxXPoint, minXPoint;
    Point[] boxPoints = new Point[]{new Point(0,0), new Point(0,0), new Point(0,0), new Point(0,0)};
    double[] blobPos;
    double minX, maxX, minY, maxY, boxX, boxY, dX, dY, sampleThetaRad, sampleThetaDeg;
    TrueAngle trueAngle;
    VisionPortal portal;
    enum SampleDirection{
        LEFT,
        RIGHT,
        BLANK
    }

    SampleDirection sampleDirection;

    public LocatorClass(ColorRange targetColorRange, LinearOpMode opMode){
        //Creates processor that detects blue color blobs
        colorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(targetColorRange)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 1, 1, -1))
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .build();
        //Opens webcam running the color locator
        portal = new VisionPortal.Builder()
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();
    }
    //-------
    //All Try/Catches are because the initial frame of the camera stream isn't processed, which causes null pointer errors

    /***
     * @return List of all color blobs detected by camera
     */
    public List<ColorBlobLocatorProcessor.Blob> getBlobsList(){

        blobs = colorLocator.getBlobs();
        //Check if list is empty, because the initial frame isn't processed, which can cause null pointer errors
        if (blobs.toArray().length == 0) {
            blobs = colorLocator.getBlobs();
        }
        //Filter out very small blobs
        ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);
        //Sorts blobs
        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs); // Largest blob is first, which is ideally the sample
        //TODO: NEED TO SORT BY CLoSEST TO CENTER OF SCREEN
        return (blobs);
    }

    /***
     *
     * @return (X,Y,) position of the largest color blob detected
     */
    //TODO: add argument for any blob
    public double[] getBlobPos(){
        getBlobsList();
        blobPos = new double[]{blobs.get(0).getBoxFit().center.x, blobs.get(0).getBoxFit().center.y};
        try{
            return (blobPos);
        } catch (Exception e){
            return (new double[]{0,0});
        }
    }

    /***
     *
     * @return Number of blobs detected
     */
    public int blobCount(){
        getBlobsList();
        try{
            return (blobs.toArray().length);
        } catch (Exception e){
            return (0);
        }
    }

    /***
     *
     * @return 4 points of the rectangle fit around the largest color blob
     */
    public Point[] getBoxPoints(){
        //ORIGIN IS TOP LEFT
        getBlobsList();
        if (!blobs.isEmpty()) {
            blobs.get(0).getBoxFit().points(boxPoints);
//            int i = 0;
//            for (Point p : boxPoints){            //Used for testing, rounds coordinates, makes reading points easier
//                boxPoints[i].x = Math.rint(boxPoints[i].x);
//                boxPoints[i].y = Math.rint(boxPoints[i].y);
//                i +=1;
//            }
            return(boxPoints);
        } else {
            return new Point[]{new Point(0,0), new Point(0,0), new Point(0,0), new Point(0,0)};
        }
    }

    /***
     * Built in calculation from RotatedRect class
     * Issues: Angle jumps when sample rotated past PI/2, also jumps between 0 and PI/2 reading when sample is at 0 radians
     * @return Angle of the largest detect blob
     */
    public double getBuiltInAngle(){
        getBlobsList();
        determineRotDir();
        try {
            if (sampleDirection == SampleDirection.RIGHT){
                return (blobs.get(0).getBoxFit().angle);
            } else {
                //Returns incorrect angle when the sample is rotated to the left
                //Need to subtract 90 degrees from it
                return (blobs.get(0).getBoxFit().angle - 90);
            }
        } catch (Exception e){
            return (0);
        }
    }

    /***
     *Inverse trig based
     * @return Angle of the largest detect blob
     */
    public double getSampleTheta(){
        //ORIGIN IS TOP LEFT
        getBoxPoints();
        determineRotDir();
        dX = boxPoints[3].x - boxPoints[0].x;
        dY = boxPoints[3].y - boxPoints[0].y;
        //Atan2 has args (y,x), flipped for use case
        sampleThetaRad = Math.atan2(dY, dX);

        try{
            //Code for trying to account for PI/2 jump when box is rotated 90 degrees, currently broken
//            sampleThetaRad = trueAngle.updateAngle(sampleThetaRad, Math.PI/3, Math.PI);
            sampleThetaDeg = AngleUtils.radToDeg(sampleThetaRad);
            if (sampleDirection == SampleDirection.LEFT){
                return (sampleThetaDeg - 90);
            } else {
                return (sampleThetaDeg);
            }
        } catch (Exception e) {
            return (0);
        }
    }

    /***
     *
     * @param p1
     * @param p2
     * @return Euclidean distance between two points
     */
    public double pointDistance(Point p1, Point p2){
        //Uses distance formula sqrt( (dX^2) + (dY^2))
        return (Math.sqrt((Math.pow((p2.x-p1.x),2))+Math.pow((p2.y-p1.y),2)));
    }

    public void orderPoints(){
        getBoxPoints();
        //Used for determining which points make up the short or long sides of a specimen
        double d1 = pointDistance(boxPoints[0], boxPoints[1]);
        double d2 = pointDistance(boxPoints[1], boxPoints[2]);

        if (Math.max(d1,d2) == d1){
            //Long side is between points 1 and 2
        } else if (Math.max(d1,d2) == d2){
            //Long side is between points 2 and 3
        }
    }

    /***
     * Determines whether the sample is rotated to the left or right
     */
    public SampleDirection determineRotDir(){
        //Currently broken
        getBoxPoints();
        //Index for minimum and maximum box points
        int pointMaxTempY = -1;
        int pointMinTempY = -1;
        int pointMaxTempX = -1;
        int pointMinTempX = -1;

        int i = 0;
        //Gets the minimum and maximum Y values
        minY = Math.min(Math.min(boxPoints[0].y, boxPoints[1].y), Math.min(boxPoints[2].y, boxPoints[3].y));
        maxY = Math.max(Math.max(boxPoints[0].y,boxPoints[1].y), Math.max(boxPoints[2].y, boxPoints[3].y));
        minX = Math.min(Math.min(boxPoints[0].x, boxPoints[1].x), Math.min(boxPoints[2].x, boxPoints[3].x));
        maxX = Math.max(Math.max(boxPoints[0].x, boxPoints[1].x), Math.max(boxPoints[2].x, boxPoints[3].x));

        //Determines the top and bottom points
        //Flipped, minimum Y value is actually highest point
        for (Point p : boxPoints){
            if(p.y == maxY){
                pointMinTempY = i;
            } else if (p.y == minY){
                pointMaxTempY = i;
            }
            if (p.x == maxX){
                pointMaxTempX = i;
            } else if (p.x == minX){
                pointMinTempX = i;
            }
            i += 1;
        }

        maxYPoint = boxPoints[pointMaxTempY];
        minYPoint = boxPoints[pointMinTempY];
        maxXPoint = boxPoints[pointMaxTempX];
        minXPoint = boxPoints[pointMinTempX];
        //TODO: Check whether X difference of min/max Y points or Y difference of min/max X points is greater, use that to determine
        //Can use X values of the Y min and max points to determine which way its rotated
        if (maxYPoint.x > minYPoint.x && maxXPoint.y > minXPoint.y){
            //Sample is turned right
            sampleDirection = SampleDirection.RIGHT;
        } else if (maxYPoint.x < minYPoint.x && maxXPoint.y < minXPoint.y){
            //Sample is turned left
            sampleDirection = SampleDirection.LEFT;
        } else {
            sampleDirection = SampleDirection.BLANK;
        }
        try {
            return (sampleDirection);
        } catch (Exception e){
            return (SampleDirection.BLANK);
        }
    }

    public Point getMaxYPoint(){
        determineRotDir();
        return (maxYPoint);
    }

    public Point getMinYPoint(){
        determineRotDir();
        return (minYPoint);
    }

//    public int pointMaxVal(){
//        return (pointMax);
//    }
//
//    public int pointMinVal(){
//        return (pointMin);
//    }



    public VisionPortal getCameraStream() {
        return portal;
    }

}