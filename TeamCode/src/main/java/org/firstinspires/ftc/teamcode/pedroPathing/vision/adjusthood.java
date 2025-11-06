package org.firstinspires.ftc.teamcode.pedroPathing.vision;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

public class adjusthood extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private Servo adjustHood = null;
    private DcMotor scoreMotor = null;
    private final ElapsedTime runtime = new ElapsedTime();
    private double distance = 0;
    private final int RED_SCORE_ZONE_ID = 24;
    private final int BLUE_SCORE_ZONE_ID = 20;
    private double lastTime;
    private int lastPosition = 0;
    private double velocity;
    private double timeCheck;
    private CRServo funnelServoL = null;
    private CRServo funnelServoR = null;
    private AprilTagDetection targetTag = null;
    private int targetId;

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);

        adjustHood = hardwareMap.get(Servo.class, "AH");
        scoreMotor = hardwareMap.get(DcMotor.class, "ScoreMotor");
        scoreMotor.setDirection(DcMotor.Direction.FORWARD);

        funnelServoL = hardwareMap.get(CRServo.class, "FL");
        funnelServoR = hardwareMap.get(CRServo.class, "FR");

        lastPosition = scoreMotor.getCurrentPosition();
        runtime.reset();
        lastTime = runtime.seconds();
        timeCheck = runtime.seconds();
    }

    public void start(){
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

        loop();
    }

    @Override
    public void loop() {
        aprilTagWebcam.update();
        distanceOfAT();
        aprilTagWebcam.displayTelemetry(targetTag);

        Shoot();

        double currentTime = runtime.seconds();
        int currentPosition = scoreMotor.getCurrentPosition();

        double deltaTime = currentTime - lastTime;
        double deltaPosition = currentPosition - lastPosition;

        velocity = deltaPosition / deltaTime;

        lastTime = currentTime;
        lastPosition = currentPosition;

        telemetry.addData("Motor Velocity", velocity);
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
                Hood(10);
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

    public void Shoot(){
        if (gamepad1.a){
            scoreMotor.setPower(1.0);
            if (velocity >= 4000){
                funnelServoL.setPower(1.0);
                funnelServoR.setPower(1.0);
            }
        }
    }
}
