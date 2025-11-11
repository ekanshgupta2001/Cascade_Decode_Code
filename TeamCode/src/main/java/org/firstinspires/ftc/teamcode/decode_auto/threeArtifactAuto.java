package org.firstinspires.ftc.teamcode.decode_auto;

import static java.lang.Math.tan;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.nio.file.Path;

@Autonomous
public class threeArtifactAuto extends OpMode {

    private Follower follower;
    private int pathState;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private DcMotor scoreMotor = null;
    private DcMotor intakeMotor = null;
    private Servo adjustHood = null;
    private TelemetryManager telemetryM;
    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    private final Pose startPose = new Pose(56, 8, Math.toRadians(110));
    private final Pose startTwo = new Pose(56, 8, Math.toRadians(110));

    private PathChain start;
    private PathChain start2;
    @Override
    public void init() {

        buildPaths();

        follower = Constants.createFollower(hardwareMap);

        rfMotor = hardwareMap.dcMotor.get("frontRightMotor");
        lfMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        rrMotor = hardwareMap.dcMotor.get("backRightMotor");
        lrMotor = hardwareMap.dcMotor.get("backLeftMotor");

        intakeMotor = hardwareMap.get(DcMotor.class, "IM");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        scoreMotor = hardwareMap.get(DcMotor.class, "ScoreMotor");
        scoreMotor.setDirection(DcMotor.Direction.FORWARD);

        parallelEncoder = hardwareMap.get(DcMotorEx.class, "parallelEncoder");
        perpendicularEncoder = hardwareMap.get(DcMotorEx.class, "perpendicularEncoder");

        adjustHood = hardwareMap.get(Servo.class, "AH");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        telemetry.addLine("Auto complete");
        telemetry.update();
    }

    public void buildPaths() {
        start = follower.pathBuilder()
                .addPath(new BezierLine(startPose, startTwo))
                .setLinearHeadingInterpolation(startPose.getHeading(), startTwo.getHeading())
                .build();

        start2 = follower.pathBuilder()
                .addPath(new BezierLine(startTwo, startPose))
                .setLinearHeadingInterpolation(startTwo.getHeading(), startPose.getHeading())
                .build();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void start(){
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                adjustHood.setPosition(0.5);
                follower.followPath(start);
                setPathState(1);
                break;

            case 1:
                if (!follower.isBusy()){
                    follower.followPath(start2);
                    scoreMotor.setPower(1.0);
                    setPathState(2);
                }
                break;

            case 2:
                if (!follower.isBusy()){
                    setPathState(-1);
                }
                break;
        }
    }

    @Override
    public void init_loop() {}

    @Override
    public void stop() {}

}