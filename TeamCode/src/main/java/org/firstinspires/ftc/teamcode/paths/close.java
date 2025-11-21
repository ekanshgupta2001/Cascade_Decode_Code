package org.firstinspires.ftc.teamcode.paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.Alliance;

public class close {
    private Follower follower;

    public Pose start = new Pose(21.913, 123, Math.toRadians(136));
    public Pose scorefirst = new Pose(60, 84, Math.toRadians(136));
    public Pose setFirstPick = new Pose(16.5, 84, Math.toRadians(180));
    public Pose firstPick = new Pose(60, 84, Math.toRadians(136));
    public Pose scoreSecond = new Pose(45, 60, Math.toRadians(180));
    public Pose setSecondPick = new Pose(10, 60, Math.toRadians(180));
    public Pose secondPick = new Pose(45, 60, Math.toRadians(180));
    public Pose robotPositioning = new Pose(45, 60, Math.toRadians(180));
    public Pose thirdScore = new Pose(10, 60, Math.toRadians(180));
    public Pose setThirdPick = new Pose(10, 60, Math.toRadians(180));
    public Pose thirdPick = new Pose(10, 60, Math.toRadians(180));
    public Pose fourthScore = new Pose(10, 60, Math.toRadians(180));

    public close(Alliance a){
        if (a.equals(Alliance.RED)){
            start = start.mirror();
            scorefirst = scorefirst.mirror();
            setFirstPick = setFirstPick.mirror();
            firstPick = firstPick.mirror();
            scoreSecond = scoreSecond.mirror();
            setSecondPick = setSecondPick.mirror();
            secondPick = secondPick.mirror();
            robotPositioning = robotPositioning.mirror();
            thirdScore = thirdScore.mirror();
            setThirdPick = setThirdPick.mirror();
            thirdPick = thirdPick.mirror();
            fourthScore = fourthScore.mirror();
        }
    }
}
