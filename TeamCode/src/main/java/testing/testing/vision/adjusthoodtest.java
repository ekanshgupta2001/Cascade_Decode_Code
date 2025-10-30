package testing.testing.vision;

import static java.lang.Math.tan;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.FTCCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Autonomous
public class adjusthoodtest extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private Servo adjustHood = null;
    private DcMotor scoreMotor = null;
    private double distance = 0;
    private final int RED_SCORE_ZONE_ID = 24;
    private final int BLUE_SCORE_ZONE_ID = 20;
    private AprilTagDetection targetTag = null;
    private int targetId;

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);

        adjustHood = hardwareMap.get(Servo.class, "AH");
        scoreMotor = hardwareMap.get(DcMotor.class, "ScoreMotor");
        scoreMotor.setDirection(DcMotor.Direction.FORWARD);

        while (targetTag == null) {
            aprilTagWebcam.update();
            List<AprilTagDetection> detections = aprilTagWebcam.getDetectedTags();

            for (AprilTagDetection detection : detections) {
                if (detection.id == BLUE_SCORE_ZONE_ID) {
                    targetTag = detection;
                    targetId = BLUE_SCORE_ZONE_ID;
                    telemetry.addData("Target Tag Found", targetTag.id);
                    telemetry.update();
                    break;
                }
                if (detection.id == RED_SCORE_ZONE_ID) {
                    targetTag = detection;
                    targetId = RED_SCORE_ZONE_ID;
                    telemetry.addData("Target Tag Found", targetTag.id);
                    telemetry.update();
                    break;
                }
            }
        }

    }

    @Override
    public void loop() {
        aprilTagWebcam.update();
        distanceOfAT();
    }

    public void distanceOfAT(){
        if (targetTag != null) {
            AprilTagDetection updatedTag = aprilTagWebcam.getTagBySpecificID(targetTag.id);

            if (updatedTag != null) {
                distance = updatedTag.ftcPose.range;
                Hood(distance);
                aprilTagWebcam.displayTelemetry(updatedTag);
            } else {
                telemetry.addData("Target Tag", "Lost");
            }
        }
    }

    public void Hood(double distance){
        if (distance > 200){
            adjustHood.setPosition(0.5);
        }
        else if (distance < 200 && distance > 80) {
            adjustHood.setPosition(0.3);
        }
        else {
            adjustHood.setPosition(0.0);
        }
    }
}
