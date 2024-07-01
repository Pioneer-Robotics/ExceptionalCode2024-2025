/* Copyright (c) 2023 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Hardware.Camera;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;

import java.util.List;


public class AprilTagProcessor {

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    private final WebcamName webcam;
    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private org.firstinspires.ftc.vision.apriltag.AprilTagProcessor aprilTag;
    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    public AprilTagProcessor(WebcamName webcam) {
        this.webcam = webcam;
        initAprilTag();
    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new org.firstinspires.ftc.vision.apriltag.AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(org.firstinspires.ftc.vision.apriltag.AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                .setOutputUnits(DistanceUnit.CM, AngleUnit.RADIANS)

                // == CAMERA CALIBRATION ==
                // SDK will attempt to find the camera calibration if not manually set.
//            .setLensIntrinsics(796.057405467, 796.057405467, 221.031337028, 295.154101395)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(webcam);
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);
    }

    /**
     * Stop streaming the camera.
     */
    public void stop() {
        visionPortal.stopStreaming();
    }

    /**
     * Resume streaming the camera.
     */
    public void resume() {
        visionPortal.resumeStreaming();
    }

    /**
     * Stop processing the camera feed, but keep streaming.
     */
    public void stopProcessing() {
        visionPortal.setProcessorEnabled(aprilTag, false);
    }

    /**
     * Resume processing the camera feed.
     */
    public void resumeProcessing() {
        visionPortal.setProcessorEnabled(aprilTag, true);
    }

    /**
     * Set the detector decimation.
     * Higher decimation increases frame rate at the expense of reduced range.
     *
     * @param decimation float (3 is default)
     */
    public void setDecimation(float decimation) {
        aprilTag.setDecimation(decimation);
    }

    /**
     * Get the AprilTag detections.
     *
     * @return A list of AprilTag detections given by the tag id.
     */
    public int[] getTagID() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        if (!currentDetections.isEmpty()) {
            return new int[]{currentDetections.get(0).id};
        } else {
            return new int[]{};
        }
    }

    /**
     * Get the AprilTag position relative to the camera.
     * If there are multiple tags, only the first tag is returned.
     * If there are no tags, returns {0, 0, 0}
     */
    public double[] getXYZ() {
        return getXYZ(0);
    }

    /**
     * Get the AprilTag position relative to the camera.
     * Returns {0, 0, 0} if the tag number is out of bounds.
     *
     * @param tagNumber The tag number given by its index in getTagID().
     */
    public double[] getXYZ(int tagNumber) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        if (!currentDetections.isEmpty() && tagNumber < currentDetections.size()) {
            return new double[]{currentDetections.get(tagNumber).ftcPose.x, currentDetections.get(tagNumber).ftcPose.y, currentDetections.get(tagNumber).ftcPose.z};
        } else {
            return new double[]{0, 0, 0};
        }
    }

    /**
     * Get the AprilTag orientation relative to the camera.
     * If there are multiple tags, only the first tag is returned.
     * If there are no tags, returns {0, 0, 0}
     */
    public double[] getPRY() {
        return getPRY(0);
    }

    /**
     * Get the AprilTag orientation relative to the camera.
     * Returns {0, 0, 0} if the tag number is out of bounds.
     *
     * @param tagNumber The tag number given by its index in getTagID().
     */
    public double[] getPRY(int tagNumber) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        if (!currentDetections.isEmpty() && tagNumber < currentDetections.size()) {
            return new double[]{currentDetections.get(0).ftcPose.pitch, currentDetections.get(0).ftcPose.roll, currentDetections.get(0).ftcPose.yaw};
        } else {
            return new double[]{0, 0, 0};
        }
    }
}
