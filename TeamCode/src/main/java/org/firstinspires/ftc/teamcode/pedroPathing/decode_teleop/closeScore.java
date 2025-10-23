package org.firstinspires.ftc.teamcode.pedroPathing.decode_teleop;

import static java.lang.Math.tan;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class closeScore extends OpMode {

    private Follower follower;
    private int pathState;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private DcMotor scoreMotor = null;
    private DcMotor intakeMotor = null;
    private CRServo funnelServoL = null;
    private CRServo funnelServoR = null;
    private Servo adjustHood = null;
    private int lastPosition = 0;
    private double lastTime;
    private double velocity;
    private double timeCheck;
    private TelemetryManager telemetryM;
    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);

        rfMotor = hardwareMap.dcMotor.get("frontRightMotor");
        lfMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        rrMotor = hardwareMap.dcMotor.get("backRightMotor");
        lrMotor = hardwareMap.dcMotor.get("backLeftMotor");

        intakeMotor = hardwareMap.get(DcMotor.class, "IM");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        scoreMotor = hardwareMap.get(DcMotor.class, "ScoreMotor");
        scoreMotor.setDirection(DcMotor.Direction.FORWARD);

        funnelServoL = hardwareMap.get(CRServo.class, "FL");
        funnelServoR = hardwareMap.get(CRServo.class, "FR");

        parallelEncoder = hardwareMap.get(DcMotorEx.class, "parallelEncoder");
        perpendicularEncoder = hardwareMap.get(DcMotorEx.class, "perpendicularEncoder");

        adjustHood = hardwareMap.get(Servo.class, "AH");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathTimer = new Timer();
        actionTimer = new Timer();
        actionTimer.resetTimer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        lastPosition = scoreMotor.getCurrentPosition();
        runtime.reset();
        lastTime = runtime.seconds();
        timeCheck = runtime.seconds();


        telemetry.addLine("Auto complete");
        telemetry.update();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();


        double currentTime = runtime.seconds();
        int currentPosition = scoreMotor.getCurrentPosition();

        double deltaTime = currentTime - lastTime;
        double deltaPosition = currentPosition - lastPosition;

        velocity = deltaPosition / deltaTime;

        lastTime = currentTime;
        lastPosition = currentPosition;

        telemetry.addData("Motor Velocity", velocity);
        telemetry.update();

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (timeCheck < 1.2){
                    funnelServoL.setPower(0.5);
                    funnelServoR.setPower(0.5);
                }
                adjustHood.setPosition(0);
                scoreMotor.setPower(1.0);
                break;

            case 1:
                if (velocity > 5900){
                    funnelServoL.setPower(0.5);
                    funnelServoR.setPower(0.5);
                }
        }
    }

}