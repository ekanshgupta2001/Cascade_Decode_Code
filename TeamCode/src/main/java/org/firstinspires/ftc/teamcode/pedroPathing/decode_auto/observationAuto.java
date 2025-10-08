package org.firstinspires.ftc.teamcode.pedroPathing.decode_auto;

import static java.lang.Math.tan;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.List;

@Autonomous(name = "Observation Zone Auto", group = "Pedro")
public class observationAuto extends LinearOpMode {

    private Follower follower;

    private Timer pathTimer, actionTimer, opmodeTimer;

    private final ElapsedTime runtime = new ElapsedTime();
    private final ElapsedTime sequenceTimer = new ElapsedTime();
    private final ElapsedTime sampleTimer = new ElapsedTime();

    private CRServo intakeMotorL;
    private CRServo intakeMotorR;
    private CRServo rotateL;
    private CRServo rotateR;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    private observationMacro scoreMacro;
    private int pathState;

    private final Pose score1 = new Pose(0, 0, Math.toRadians(-15));
    private final Pose moveToObservation = new Pose(-25, -5, Math.toRadians(0));
    private final Pose returnToScore = new Pose(0, 0, Math.toRadians(-15));
    private final Pose movetoObservationTwo = new Pose(-25, 5, Math.toRadians(0));
    private final Pose returnToScoretwo = new Pose(0, 0, Math.toRadians(-15));

    private PathChain move, observationPickup, moveAgain, observationPickuptwo;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);

        rfMotor = hardwareMap.dcMotor.get("frontRightMotor");
        lfMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        rrMotor = hardwareMap.dcMotor.get("backRightMotor");
        lrMotor = hardwareMap.dcMotor.get("backLeftMotor");

        rotateL = hardwareMap.get(CRServo.class, "rotateIntakeLeft");
        rotateR = hardwareMap.get(CRServo.class, "rotateIntakeRight");
        rotateL.setDirection(CRServo.Direction.FORWARD);
        rotateR.setDirection(CRServo.Direction.FORWARD);

        intakeMotorL = hardwareMap.get(CRServo.class, "intakeMotorLeft");
        intakeMotorR = hardwareMap.get(CRServo.class, "intakeMotorRight");
        intakeMotorL.setDirection(CRServo.Direction.FORWARD);
        intakeMotorR.setDirection(CRServo.Direction.FORWARD);

        parallelEncoder = hardwareMap.get(DcMotorEx.class, "parallelEncoder");
        perpendicularEncoder = hardwareMap.get(DcMotorEx.class, "perpendicularEncoder");

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        telemetry.addLine("Auto complete");
        telemetry.update();
    }

    public void buildPaths() {
        move = follower.pathBuilder()
                .addPath(new BezierLine(score1, moveToObservation))
                .setLinearHeadingInterpolation(score1.getHeading(), moveToObservation.getHeading())
                .build();

        observationPickup = follower.pathBuilder()
                .addPath(new BezierLine(moveToObservation, returnToScore))
                .setLinearHeadingInterpolation(moveToObservation.getHeading(), returnToScore.getHeading())
                .build();

        moveAgain = follower.pathBuilder()
                .addPath(new BezierLine(returnToScore, movetoObservationTwo))
                .setLinearHeadingInterpolation(returnToScore.getHeading(), movetoObservationTwo.getHeading())
                .build();

        observationPickuptwo = follower.pathBuilder()
                .addPath(new BezierLine(movetoObservationTwo, returnToScoretwo))
                .setLinearHeadingInterpolation(movetoObservationTwo.getHeading(), returnToScoretwo.getHeading())
                .build();
    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                scoreMacro.start();
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy()){
                    follower.followPath(move);
                }
                break;

            case 2:
                if(!follower.isBusy()){
                    follower.followPath(observationPickup);
                }
                break;
            case 3:
                if(!follower.isBusy()){
                    scoreMacro.start();
                }
                break;
            case 4:
                if (!follower.isBusy()){
                    follower.followPath(moveAgain);
                }
                break;
            case 5:
                if (follower.isBusy()){
                    follower.followPath(observationPickuptwo);
                }
                break;
            case 6:
                scoreMacro.start();
        }
    }
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
}