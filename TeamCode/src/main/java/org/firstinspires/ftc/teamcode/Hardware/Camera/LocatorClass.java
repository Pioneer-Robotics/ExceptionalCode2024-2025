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

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.List;

import org.firstinspires.ftc.teamcode.Helpers.AngleUtils;
import org.opencv.core.RotatedRect;

public class LocatorClass {
    List<ColorBlobLocatorProcessor.Blob> blobs;
    ColorBlobLocatorProcessor colorLocator;
    Point maxYPoint, minYPoint, maxXPoint, minXPoint;
    Point[] boxPoints = new Point[]{new Point(0,0), new Point(0,0), new Point(0,0), new Point(0,0)};
    double[] blobPos;
    double minX, maxX, minY, maxY, boxX, boxY, dX, dY, sampleThetaRad, sampleThetaDeg;
    int streamWidth = 320;
    int streamHeight = 240;
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
                .setCameraResolution(new Size(streamWidth, streamHeight))
                .setCamera(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();
    }
    //-------
    //All Try/Catches are because the initial frame of the camera stream isn't processed
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
        ColorBlobLocatorProcessor.Util.filterByArea(50, 80000, blobs);
        //Sorts blobs
        ColorBlobLocatorProcessor.Util.sortByArea(SortOrder.DESCENDING, blobs); // Largest blob is first, which is ideally the sample
        //TODO: NEED TO SORT BY CLoSEST TO CENTER OF SCREEN
        return (blobs);
    }

    /***
     * Returns certain blob from the list of blobs
     * @param inputBlobs list of color blobs
     * @param index index value for said blob in list
     * @return
     */
    public ColorBlobLocatorProcessor.Blob getBlob(List<ColorBlobLocatorProcessor.Blob> inputBlobs, int index){
        try {
            return (inputBlobs.get(index));
        } catch (Exception e){
            return (new ColorBlobLocatorProcessor.Blob() {
                @Override
                public MatOfPoint getContour() {
                    return new MatOfPoint();
                }

                @Override
                public Point[] getContourPoints() {
                    return new Point[0];
                }

                @Override
                public int getContourArea() {
                    return 0;
                }

                @Override
                public double getDensity() {
                    return 0;
                }

                @Override
                public double getAspectRatio() {
                    return 0;
                }

                @Override
                public RotatedRect getBoxFit() {
                    return new RotatedRect();
                }
            });
        }
    }

    /***
     *
     * @return (X,Y,) position of a color blob
     */
    public double[] getBlobPos(ColorBlobLocatorProcessor.Blob blob){
        blobPos = new double[]{blob.getBoxFit().center.x, blob.getBoxFit().center.y};
        try{
            return (blobPos);
        } catch (Exception e){
            return (new double[]{-1,-1});
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
     * @return 4 points of the rectangle fit around a color blob
     */
    public Point[] getBoxPoints(ColorBlobLocatorProcessor.Blob blob){
        //ORIGIN IS TOP LEFT
        blob.getBoxFit().points(boxPoints);
//        int i = 0;
//        for (Point p : boxPoints){            //Used for testing, rounds coordinates, makes reading points easier
//            boxPoints[i].x = Math.rint(boxPoints[i].x);
//            boxPoints[i].y = Math.rint(boxPoints[i].y);
//            i +=1;
//        }
        return(boxPoints);

    }

    /***
     * Built in calculation from RotatedRect class
     * Issues: Angle jumps when sample rotated past PI/2, also jumps between 0 and PI/2 reading when sample is at 0 radians
     * @return Angle of the largest detect blob
     */
    public double getBuiltInAngle(ColorBlobLocatorProcessor.Blob blob){
        rotationDirection(blob);
        try {
            if (sampleDirection == SampleDirection.RIGHT){
                return (blob.getBoxFit().angle);
            } else {
                //Need to subtract 90 degrees from it
                return (blob.getBoxFit().angle - 90);
            }
        } catch (Exception e){
            return (0);
        }
    }

    /***
     *Inverse trig based
     * @return Angle of a color blob
     */
    public double getSampleTheta(ColorBlobLocatorProcessor.Blob blob){
        //ORIGIN IS TOP LEFT
        getBoxPoints(blob);
        rotationDirection(blob);
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
            } else if (sampleDirection == SampleDirection.RIGHT){
                return (sampleThetaDeg);
            }  else {
                return (-1);
            }
        } catch (Exception e) {
            return (-1);
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

    /***
     * Calculates slope between two points
     * @param p1
     * @param p2
     * @return Slope between p1 and p2
     */
    public double pointSlope(Point p1, Point p2){
        return ((p2.y-p1.y)/(p2.x-p1.x));
    }

    /***
     * @return what percentage of the screen the blob takes up
     */
    public double percentScreen(ColorBlobLocatorProcessor.Blob blob){
        try {
            return (blob.getContourArea()/((double )streamHeight*streamWidth));
        } catch (Exception e){
            return (0);
        }
    }

    /***
     * Determines whether the sample is rotated left or right
     * @return Left or Right
     */
    public SampleDirection rotationDirection(ColorBlobLocatorProcessor.Blob blob){
        //Finds the longer side of the sample
        getBoxPoints(blob);
        double side1 = pointDistance(boxPoints[0], boxPoints[1]);
        double side2 = pointDistance(boxPoints[1], boxPoints[2]);
        double longSlope = 0;
        //Gets the slope of the long side
        //Negative slope = Right
        //Positive slope = Left
        if (side1 > side2){
            longSlope = pointSlope(boxPoints[0], boxPoints[1]);
        } else if (side2 > side1){
            longSlope = pointSlope(boxPoints[1], boxPoints[2]);
        }

        if (longSlope > 0){
            sampleDirection = SampleDirection.LEFT;
        } else if (longSlope < 0){
            sampleDirection = SampleDirection.RIGHT;
        } else {
            sampleDirection = SampleDirection.BLANK;
        }

        try {
            return (sampleDirection);
        } catch (Exception e){
            return (SampleDirection.BLANK);
        }

    }

    /***
     * Determines whether the sample is rotated to the left or right
     */
    @Deprecated
    public SampleDirection determineRotDirBad(ColorBlobLocatorProcessor.Blob blob){
        //Currently broken
        getBoxPoints(blob);
        //Index for minimum and maximum box points
        int pointMaxTempY = -1;
        int pointMinTempY = -1;
        int pointMaxTempX = -1;
        int pointMinTempX = -1;

        double dOuterX = -1;
        double dOuterY = -1;

        int i = 0;
        //Gets the minimum and maximum Y values
        //Flipped, minimum Y value is actually highest point, (0,0) is top left
        maxY = Math.min(Math.min(boxPoints[0].y, boxPoints[1].y), Math.min(boxPoints[2].y, boxPoints[3].y));
        minY = Math.max(Math.max(boxPoints[0].y,boxPoints[1].y), Math.max(boxPoints[2].y, boxPoints[3].y));
        minX = Math.min(Math.min(boxPoints[0].x, boxPoints[1].x), Math.min(boxPoints[2].x, boxPoints[3].x));
        maxX = Math.max(Math.max(boxPoints[0].x, boxPoints[1].x), Math.max(boxPoints[2].x, boxPoints[3].x));

        //Determines the top and bottom points
        for (Point p : boxPoints){
            if(p.y == maxY){
                pointMaxTempY = i;
            } else if (p.y == minY){
                pointMinTempY = i;
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

        dOuterX = Math.abs(maxXPoint.x - minXPoint.x);
        dOuterY = Math.abs(maxYPoint.y - minYPoint.y);

        if (dOuterX < dOuterY){
            //Use Xs to determine
            //Can use X values of the Y min and max points to determine which way its rotated
            if (maxXPoint.y > minXPoint.y){
                //Sample is turned right
                sampleDirection = SampleDirection.RIGHT;
            } else if (maxXPoint.y < minXPoint.y){
                //Sample is turned left
                sampleDirection = SampleDirection.LEFT;
            } else {
                sampleDirection = SampleDirection.BLANK;
            }
        } else if (dOuterY < dOuterX){
            //Use Ys to determine
            //Can use X values of the Y min and max points to determine which way its rotated
            if (maxYPoint.x > minYPoint.x){
                //Sample is turned right
                sampleDirection = SampleDirection.RIGHT;
            } else if (maxYPoint.x < minYPoint.x){
                //Sample is turned left
                sampleDirection = SampleDirection.LEFT;
            } else {
                sampleDirection = SampleDirection.BLANK;
            }
        } else {
            sampleDirection = SampleDirection.BLANK;
        }


        try {
            return (sampleDirection);
        } catch (Exception e){
            return (SampleDirection.BLANK);
        }
    }

    public double[] getBlobInfo(ColorBlobLocatorProcessor.Blob blob){
        getBlobPos(blob);
        getSampleTheta(blob);
        try {
            return (new double[]{blobPos[0], blobPos[1], sampleThetaDeg});
        } catch (Exception e){
            return (new double[]{-1,-1,-1});
        }
    }

    public double[][] getAllBlobsInfo(List<ColorBlobLocatorProcessor.Blob> inputblobs){
        double[][] infoList = new double[][]{};
        int i = 0;
        for (ColorBlobLocatorProcessor.Blob blob : inputblobs) {
            infoList[i] = getBlobInfo(blob);
            i += 1;
        }
        return (infoList);
    }


    public VisionPortal getCameraStream() {
        return portal;
    }

}