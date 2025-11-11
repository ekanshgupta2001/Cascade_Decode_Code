package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

public class adjusthood extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    private Servo adjustHood = null;
    private DcMotor scoreMotor = null;
    private DcMotor intakeMotor = null;
    private final ElapsedTime runtime = new ElapsedTime();
    private double distance = 0;
    private final int RED_SCORE_ZONE_ID = 24;
    private final int BLUE_SCORE_ZONE_ID = 20;
    private double lastTime;
    private int lastPosition = 0;
    private double velocity;
    private double timeCheck;
    double ticksPerRev = 0.0;
    double RPM = 0.0;
    private AprilTagDetection targetTag = null;
    private int targetId;

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);

        adjustHood = hardwareMap.get(Servo.class, "AH");
        scoreMotor = hardwareMap.get(DcMotor.class, "SM");
        scoreMotor.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor = hardwareMap.get(DcMotor.class, "IM");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

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

        ticksPerRev = scoreMotor.getMotorType().getTicksPerRev();
        RPM = (velocity / ticksPerRev) * 60;

        lastTime = currentTime;
        lastPosition = currentPosition;

        telemetry.addData("Motor Velocity", RPM);
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
            adjustHood.setPosition(0.1);
        }
        else {
            adjustHood.setPosition(0.0);
        }
    }

    public void Shoot(){
        if (gamepad1.a){
            scoreMotor.setPower(0.8);
            if (RPM > 4800){
                intakeMotor.setPower(-1.0);
            }
        }
    }
}
