package testing.testing;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class movementTest extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private TelemetryManager telemetryM;

    private DcMotorEx parallelEncoder;
    private DcMotorEx perpendicularEncoder;
    private double adjustSpeed = 0.5;
    private boolean slowModeActive = false;

    @Override
    public void init() {

//        follower = Constants.createFollower(hardwareMap);

        rrMotor = hardwareMap.get(DcMotor.class, "FL");

        rrMotor.setDirection(DcMotor.Direction.FORWARD);

//        follower.setStartingPose(new Pose(0,0,0));

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

    }

    @Override
    public void loop() {
        movement();

//        telemetry.addData("Front Left Power", lfMotor.getPower());
//        telemetry.addData("Front Right Power", rfMotor.getPower());
//        telemetry.addData("Back Left Power", lrMotor.getPower());
        telemetry.addData("Back Right Power", lfMotor.getPower());
//        telemetry.addData("Perpendicular Encoders", perpendicularEncoder);
//        telemetry.addData("Parallel Encoders", parallelEncoder);
//        telemetryM.debug("position", follower.getPose());
//        telemetryM.debug("velocity", follower.getVelocity());
        telemetry.update();
//        telemetryM.update();
    }

    public void  movement(){
        double y = -gamepad1.left_stick_y; // Forward/backward
        double x = gamepad1.left_stick_x; // Left/right
        double rotation = gamepad1.right_stick_x; // Rotation

        double frontLeftPower = y + x + rotation;
        double frontRightPower = y - x - rotation;
        double backLeftPower = y - x + rotation;
        double backRightPower = y + x - rotation;

        double maxPower = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        if (maxPower > 1.0) {

            backRightPower /= maxPower;
        }

        lfMotor.setPower(backRightPower);

    }

}