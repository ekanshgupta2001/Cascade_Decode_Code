package org.firstinspires.ftc.teamcode.decode_auto;

import static java.lang.Math.tan;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.List;

@Autonomous
public class pickUpAuto extends OpMode {

    private Follower follower;

    private Timer pathTimer, actionTimer, opmodeTimer;

    private final ElapsedTime runtime = new ElapsedTime();
    private final ElapsedTime sequenceTimer = new ElapsedTime();
    private final ElapsedTime sampleTimer = new ElapsedTime();

    private DcMotor slideMotor;
    private DcMotor armMotor;
    private CRServo intakeMotorL;
    private CRServo intakeMotorR;
    private CRServo rotateL;
    private CRServo rotateR;

    private Limelight3A limelight;
    private int pathState;
    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));
    private final Pose moveToBalls = new Pose(25, 111, Math.toRadians(-90));
    private final Pose pickUpBalls = new Pose(25, 130, Math.toRadians(-90));
    private final Pose scorePose = new Pose(5, 130, Math.toRadians(-135));
    private final Pose movePose = new Pose(37, 121, Math.toRadians(0));
    private final Pose pickupPose = new Pose(37, 130, Math.toRadians(0));
    private final Pose score = new Pose(5, 130, Math.toRadians(-135));
    private final Pose parkPose = new Pose(100, 111, Math.toRadians(-180));


    private PathChain firstMove, move, scorePath, moveAgain, pickUpAgain, scoreAgain, parkPath;

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);

    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    public void buildPaths() {
        firstMove = follower.pathBuilder()
                .addPath(new BezierLine(startPose, moveToBalls))
                .setLinearHeadingInterpolation(startPose.getHeading(), moveToBalls.getHeading())
                .build();

        move = follower.pathBuilder()
                .addPath(new BezierLine(moveToBalls, pickUpBalls))
                .setLinearHeadingInterpolation(moveToBalls.getHeading(), pickUpBalls.getHeading())
                .build();

        scorePath = follower.pathBuilder()
                .addPath(new BezierLine(pickUpBalls, scorePose))
                .setLinearHeadingInterpolation(pickUpBalls.getHeading(), scorePose.getHeading())
                .build();

        moveAgain = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, movePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), movePose.getHeading())
                .build();

        pickUpAgain = follower.pathBuilder()
                .addPath(new BezierLine(movePose, pickupPose))
                .setLinearHeadingInterpolation(movePose.getHeading(), pickupPose.getHeading())
                .build();

        scoreAgain = follower.pathBuilder()
                .addPath(new BezierLine(pickupPose, score))
                .setLinearHeadingInterpolation(pickupPose.getHeading(), score.getHeading())
                .build();

        parkPath = follower.pathBuilder()
                .addPath(new BezierLine(score, parkPose))
                .setLinearHeadingInterpolation(score.getHeading(), parkPose.getHeading())
                .build();

        follower.followPath(move);
    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(firstMove);
                break;
            case 1:
                if (!follower.isBusy()){
                    follower.followPath(move);
                }
                break;

            case 2:
                if (!follower.isBusy()){
                    follower.followPath(scorePath);
                    if (pathTimer.getElapsedTime() > 2.5){
                        intakeMotorL.setPower(1.0);
                        intakeMotorR.setPower(1.0);
                    }
                }
                break;
        }
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

        slideMotor = hardwareMap.get(DcMotor.class, "Slide Motor");
        slideMotor.setDirection(DcMotor.Direction.FORWARD);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rotateL = hardwareMap.get(CRServo.class, "rotateIntakeLeft");
        rotateR = hardwareMap.get(CRServo.class, "rotateIntakeRight");
        rotateL.setDirection(CRServo.Direction.FORWARD);
        rotateR.setDirection(CRServo.Direction.FORWARD);

        intakeMotorL = hardwareMap.get(CRServo.class, "intakeMotorLeft");
        intakeMotorR = hardwareMap.get(CRServo.class, "intakeMotorRight");
        intakeMotorL.setDirection(CRServo.Direction.FORWARD);
        intakeMotorR.setDirection(CRServo.Direction.FORWARD);

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        telemetry.addLine("Auto complete");
        telemetry.update();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();
    }

    @Override
    public void init_loop() {}

    @Override
    public void stop() {}
}