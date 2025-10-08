package org.firstinspires.ftc.teamcode.pedroPathing.decode_teleop;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "Cascade Teleop", group = "Robot")
public class cascadeTeleop extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private DcMotor scoreMotor = null;
    private DcMotor intakeMotorL = null;
    private DcMotor intakeMotorR = null;
    private TelemetryManager telemetryM;
    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    private Servo rotateRampL;
    private Servo rotateRampR;
    private double currentPower = 0;
    private double adjustSpeed = 1.0;
    private double intakeDirection = 0.0;
    private boolean slowModeActive = false;
    public scoreFar farScore;
    public mediumScore scoreMedium;
    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);

        rfMotor = hardwareMap.dcMotor.get("frontRightMotor");
        lfMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        rrMotor = hardwareMap.dcMotor.get("backRightMotor");
        lrMotor = hardwareMap.dcMotor.get("backLeftMotor");

        intakeMotorL = hardwareMap.get(DcMotor.class, "intakeMotorLeft");
        intakeMotorR = hardwareMap.get(DcMotor.class, "intakeMotorRight");
        intakeMotorL.setDirection(DcMotor.Direction.FORWARD);
        intakeMotorR.setDirection(DcMotor.Direction.FORWARD);

        scoreMotor = hardwareMap.get(DcMotor.class, "ScoreMotor");
        scoreMotor.setDirection(DcMotor.Direction.FORWARD);

        rotateRampL = hardwareMap.get(Servo.class, "RotateRampLeft");
        rotateRampR = hardwareMap.get(Servo.class, "RotateRampRight");

        parallelEncoder = hardwareMap.get(DcMotorEx.class, "parallelEncoder");
        perpendicularEncoder = hardwareMap.get(DcMotorEx.class, "perpendicularEncoder");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        telemetry.addLine("Auto complete");
        telemetry.update();

    }

    public closeScore scoreClose;

    @Override
    public void loop() {
        movement();
        intake();
        farTriangle();
        mediumTriangle();
        closeTriangle();

        telemetry.addData("Front Left Power", lfMotor.getPower());
        telemetry.addData("Front Right Power", rfMotor.getPower());
        telemetry.addData("Back Left Power", lrMotor.getPower());
        telemetry.addData("Back Right Power", rrMotor.getPower());
        telemetry.addData("Intake Left Power", intakeMotorL.getPower());
        telemetry.addData("Intake Right Power", intakeMotorR.getPower());
        telemetry.addData("Perpendicular Encoders", perpendicularEncoder);
        telemetry.addData("Parallel Encoders", parallelEncoder);
        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetry.update();
    }

    public void movement(){
        if (gamepad1.dpad_up){
            slowModeActive = true;
        }
        if (gamepad1.dpad_down){
            slowModeActive = false;
        }

        if (!slowModeActive) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );

        }
        if (slowModeActive){
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * adjustSpeed,
                    -gamepad1.left_stick_x * adjustSpeed,
                    -gamepad1.right_stick_x * adjustSpeed,
                    true
            );
        }
    }

    public void intake(){
        intakeDirection = gamepad1.left_trigger - gamepad1.right_trigger;

        if (intakeDirection > 0) {
                intakeMotorR.setPower(1.0);
            intakeMotorL.setPower(-1.0);
        } else if (intakeDirection < 0) {
            intakeMotorL.setPower(1.0);
            intakeMotorR.setPower(-1.0);
        } else {
            intakeMotorR.setPower(0);
            intakeMotorL.setPower(0);
        }
    }

    public void farTriangle(){
        if (gamepad2.y){
            farScore.start();
        }

    }

    public void mediumTriangle(){
        if(gamepad2.x){
            scoreMedium.start();
        }
    }
    public void closeTriangle(){
        if (gamepad2.a){
            scoreClose.start();
        }

    }
}