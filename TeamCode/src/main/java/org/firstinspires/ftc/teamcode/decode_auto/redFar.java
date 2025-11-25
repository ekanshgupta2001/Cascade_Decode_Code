package org.firstinspires.ftc.teamcode.decode_auto;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.paths.closePath;
import org.firstinspires.ftc.teamcode.paths.farPaths;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
@Autonomous
public class redFar extends OpMode {
    private Follower follower;
    Alliance alliance;
    Intake i;
    Shooter s;
    Webcam w;
    farPaths p;
    private TelemetryManager telemetryM;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        w = new Webcam(hardwareMap, telemetry, "Webcam 1");
        i = new Intake(hardwareMap);
        s = new Shooter(hardwareMap, i);
        p = new farPaths(follower, Alliance.RED);

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower.setStartingPose(p.start);
        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    public void autonomousPathUpdate(){
        switch (pathState){
            case 0:
                follower.followPath(p.scoreP());
                s.farAuto();
                setPathState(1);
                break;
            case 1:
                if (!follower.isBusy() && !s.isAutoActionRunning()){
                    follower.followPath(p.setOne());
                    i.inCommand();
                    setPathState(2);
                }
                break;
            case 2:
                if (!follower.isBusy()){
                    follower.followPath(p.pickOne());
                    setPathState(3);
                }
                break;
            case 3:
                if (!follower.isBusy() && actionTimer.getElapsedTime() > 1){
                    i.stopCommand();
                    s.farAuto();
                    follower.followPath(p.scoreTwo());
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy() && !s.isAutoActionRunning()){
                    follower.followPath(p.setTwo());
                    i.inCommand();
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy()){
                    follower.followPath(p.pickTwo());
                    setPathState(6);
                }
                break;
            case 6:
                if (!follower.isBusy() && actionTimer.getElapsedTime() > 1){
                    i.stopCommand();
                    s.closeAuto();
                    follower.followPath(p.scoreThird());
                    setPathState(7);
                }
                break;
            case 7:
                if (!follower.isBusy() && !s.isAutoActionRunning()){
                    follower.followPath(p.parkPath());

                    setPathState(8);
                }
                break;
            case 8:
                if(!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }
}
