package testing.testing;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.function.Supplier;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class automatedDrive extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    private TelemetryManager telemetryM;
    private Supplier<PathChain> pathChain;
    public static Pose startingPose;
    private boolean goToObservation = false;
    private double adjustSpeed = 0.5;
    private boolean slowModeActive = false;


    @Override
    public void init() {

        follower = Constants.createFollower(hardwareMap);

        rfMotor = hardwareMap.dcMotor.get("frontRightMotor");
        lfMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        rrMotor = hardwareMap.dcMotor.get("backRightMotor");
        lrMotor = hardwareMap.dcMotor.get("backLeftMotor");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        parallelEncoder = hardwareMap.get(DcMotorEx.class, "parallelEncoder");
        perpendicularEncoder = hardwareMap.get(DcMotorEx.class, "perpendicularEncoder");

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        pathChain = () -> follower.pathBuilder()
                .addPath(new Path(new BezierLine(follower::getPose, new Pose(9, 8))))
                .setHeadingInterpolation(HeadingInterpolator.linearFromPoint(follower::getHeading, Math.toRadians(90), 0.8))
                .build();


        telemetry.addLine("Auto complete");
        telemetry.update();

    }

    @Override
    public void start(){
        loop();
    }

    @Override
    public void loop() {
        movement();

        telemetry.addData("Front Left Power", lfMotor.getPower());
        telemetry.addData("Front Right Power", rfMotor.getPower());
        telemetry.addData("Back Left Power", lrMotor.getPower());
        telemetry.addData("Back Right Power", rrMotor.getPower());
        telemetry.addData("Perpendicular Encoders", perpendicularEncoder);
        telemetry.addData("Parallel Encoders", parallelEncoder);
        telemetryM.debug("position", follower.getPose());
        telemetryM.debug("velocity", follower.getVelocity());
        telemetry.update();
    }

    public void movement(){

        follower.startTeleopDrive();
        follower.update();
        telemetryM.update();

        if (!goToObservation){
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

        if (gamepad1.dpad_right){
            goToObservation = true;
            follower.followPath(pathChain.get());
        }

        if (gamepad1.dpad_up){
            slowModeActive = true;
        }
        if (gamepad1.dpad_down){
            slowModeActive = false;
        }

        if (gamepad1.dpadRightWasPressed() && adjustSpeed >= 1.0) {
            adjustSpeed += 0.2;
        }

        if (gamepad2.dpadLeftWasPressed()) {
            adjustSpeed -= 0.2;
        }
    }
}