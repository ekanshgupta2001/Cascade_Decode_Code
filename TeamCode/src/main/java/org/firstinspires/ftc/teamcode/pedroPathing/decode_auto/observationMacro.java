package org.firstinspires.ftc.teamcode.pedroPathing.decode_auto;


import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class observationMacro extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private final ElapsedTime runtime = new ElapsedTime();
    private final ElapsedTime sequenceTimer = new ElapsedTime();
    private final ElapsedTime sampleTimer = new ElapsedTime();
    private CRServo intakeMotorL;
    private CRServo intakeMotorR;
    private CRServo rotateL;
    private CRServo rotateR;
    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    private DcMotor rotateRampMotor;
    private DcMotor scoreMotorL;
    private DcMotor scoreMotorR;
    private CRServo scoreIntakeL;
    private CRServo scoreIntakeR;

    private Limelight3A limelight;

    @Override
    public void init() {

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

    @Override
    public void loop() {
        rotateRampPosition(250, 0.8);
        shoot(2500, 1.0, 1.0);
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