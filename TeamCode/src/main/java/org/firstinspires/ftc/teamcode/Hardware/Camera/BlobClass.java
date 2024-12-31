package org.firstinspires.ftc.teamcode.Hardware.Camera;

import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.Arrays;
import java.util.List;

public class BlobClass {
    List<ColorBlobLocatorProcessor.Blob> blobs;
    public BlobClass(List<ColorBlobLocatorProcessor.Blob> blobs){
        this.blobs = blobs;
    }

        public double[] blobPos(){
            return (new double[]{blobs.get(0).getBoxFit().center.x, blobs.get(0).getBoxFit().center.y});
        }

        public double blobPosX(){
            return (blobs.get(0).getBoxFit().center.x);
        }

        public double blobPosY(){
            return (blobs.get(0).getBoxFit().center.y);
        }

        public double getSpecRatio(){
            return (blobs.get(0).getDensity());
        }

        public Arrays getPoints(){
    //        Arrays.stream(Arrays.stream(blobs.get(0).getContourPoints()).toArray());
            return (Arrays) Arrays.stream(Arrays.stream(blobs.get(0).getContourPoints()).toArray());
        }
}
