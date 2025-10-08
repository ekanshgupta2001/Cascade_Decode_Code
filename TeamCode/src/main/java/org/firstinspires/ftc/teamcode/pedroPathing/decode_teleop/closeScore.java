package org.firstinspires.ftc.teamcode.pedroPathing.decode_teleop;


import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class closeScore extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;

    private DcMotor slideMotor;
    private DcMotor armMotor;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private final ElapsedTime runtime = new ElapsedTime();
    private final ElapsedTime sequenceTimer = new ElapsedTime();
    private final ElapsedTime sampleTimer = new ElapsedTime();
    private DcMotor rotateRampMotor;
    private DcMotor scoreMotorL;
    private DcMotor scoreMotorR;
    private DcMotor intakeMotorL;
    private DcMotor intakeMotorR;
    private CRServo scoreIntakeL;
    private CRServo scoreIntakeR;

    private Limelight3A limelight;

    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);

        slideMotor = hardwareMap.get(DcMotor.class, "Slide Motor");
        slideMotor.setDirection(DcMotor.Direction.FORWARD);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intakeMotorL = hardwareMap.get(DcMotor.class, "intakeMotorLeft");
        intakeMotorR = hardwareMap.get(DcMotor.class, "intakeMotorRight");
        intakeMotorL.setDirection(DcMotor.Direction.FORWARD);
        intakeMotorR.setDirection(DcMotor.Direction.FORWARD);

        rotateRampMotor = hardwareMap.get(DcMotor.class, "RotateRampMotor");

        scoreIntakeL = hardwareMap.get(CRServo.class, "IntakeShooterLeft");
        scoreIntakeR = hardwareMap.get(CRServo.class, "IntakeShooterRight");

        scoreMotorL = hardwareMap.get(DcMotor.class, "ScoreMotorLeft");
        scoreMotorL.setDirection(DcMotor.Direction.REVERSE);
        scoreMotorR = hardwareMap.get(DcMotor.class, "ScoreMotorRight");
        scoreMotorR.setDirection(DcMotor.Direction.FORWARD);

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        telemetry.addLine("Auto complete");
        telemetry.update();

    }

    @Override
    public void loop() {
        rotateRampPosition(1000, 0.8);
        shoot(2500, 0.75, 1.0);
    }

    public void rotateRampPosition(int position, double power) {
        rotateRampMotor.setTargetPosition(position);
        rotateRampMotor.setPower(power);
        rotateRampMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void shoot(long time, double power, double servoPower){
        if (pathTimer.getElapsedTime() > time){
            scoreIntakeL.setPower(servoPower);
            scoreIntakeR.setPower(-servoPower);

            scoreMotorL.setPower(power);
            scoreMotorR.setPower(power);

        }
    }

}