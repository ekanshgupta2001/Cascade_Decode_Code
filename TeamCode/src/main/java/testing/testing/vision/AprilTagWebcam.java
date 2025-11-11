package testing.testing.vision;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class AprilTagWebcam {
    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    private Telemetry telemetry;

    public void init(HardwareMap hwMap, Telemetry telemetry){
        this.telemetry = telemetry;

        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true) //Shows what the ID actually is
                .setDrawTagOutline(true)
                .setDrawAxes(true) //Draws the Axes
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES) //Gets the units
                .setCameraPose(
                        new Position(DistanceUnit.INCH, 0, 13, 8, 0),
                        new YawPitchRollAngles(AngleUnit.DEGREES, 0, 45, 0, 0)
                )
                .build();

        aprilTagProcessor.setDecimation(2);

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
        visionPortal.resumeStreaming();
    }

    public void update(){
        detectedTags = aprilTagProcessor.getDetections();
    }

    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }

    public void displayTelemetry(AprilTagDetection detectionID){
        if (detectionID == null){return;}
        if (detectionID.metadata != null) {
            telemetry.addLine(String.format("\n==== (ID %d) %s", detectionID.id, detectionID.metadata.name));
            telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (CM)", detectionID.ftcPose.x, detectionID.ftcPose.y, detectionID.ftcPose.z));
            telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detectionID.ftcPose.pitch, detectionID.ftcPose.roll, detectionID.ftcPose.yaw));
            telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (CM, deg, deg)", detectionID.ftcPose.range, detectionID.ftcPose.bearing, detectionID.ftcPose.elevation));
        } else {
            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectionID.id));
            telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detectionID.center.x, detectionID.center.y));
        }
    }

    public AprilTagDetection getTagBySpecificID(int id){
        for (AprilTagDetection detection : detectedTags){
            if (detection.id == id){
                return detection;
            }
        }
        return null;
    }

    public void stop(){
        if (visionPortal != null){
            visionPortal.close();
        }
    }
}
