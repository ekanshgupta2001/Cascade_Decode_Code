package org.firstinspires.ftc.teamcode.decode_auto;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class red extends LinearOpMode {
    private Follower follower;
    Intake i;
    Shooter s;
    Webcam w;
    private final int RED_SCORE_ZONE_ID = 24;
    private final int BLUE_SCORE_ZONE_ID = 20;
    private TelemetryManager telemetryM;
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    AprilTagDetection target = null;


    @Override
    public void runOpMode(){
        follower = Constants.createFollower(hardwareMap);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        w = new Webcam(hardwareMap, telemetry, "Webcam 1");

        i = new Intake(hardwareMap);
        s = new Shooter(hardwareMap, i);

        w.setTargetTagID(RED_SCORE_ZONE_ID);
        target = w.getTargetTag();

        lfMotor = hardwareMap.get(DcMotor.class, "FL");
        lrMotor = hardwareMap.get(DcMotor.class, "BL");
        rfMotor = hardwareMap.get(DcMotor.class, "FR");
        rrMotor = hardwareMap.get(DcMotor.class, "BR");

        lfMotor.setDirection(DcMotor.Direction.FORWARD);
        lrMotor.setDirection(DcMotor.Direction.FORWARD);
        rfMotor.setDirection(DcMotor.Direction.REVERSE);
        rrMotor.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Ready");
        telemetry.update();

        waitForStart();

        auto();
        sleep(100000);
        telemetry.addData("Auto phase", "Done");
        telemetry.update();
    }

    public void auto(){
        movement(-0.5, 1300);
        sleep(1500);
        s.CloseAuto();
        moveSideways(-0.5, 500);
        sleep(3000);
        rotate(400, -0.5);
        i.inCommand();
        sleep(1000);
        movement(0.7, 1000);
        sleep(1000);
        movement(-0.7, 1000);
        i.stopCommand();
        sleep(1000);
        rotate(400, 0.5);
        s.CloseAuto();
        sleep(5000);
        moveSideways(-0.5, 1000);

    }

    public void movement(double speed, long movement){
        telemetry.addData("Current Status", "Moving");
        telemetry.update();
        double frontLeftPower;
        double backLeftPower;
        double frontRightPower;
        double backRightPower;

        frontLeftPower = speed;
        backLeftPower = speed;
        frontRightPower = speed;
        backRightPower = speed;

        lfMotor.setPower(frontLeftPower);
        lrMotor.setPower(backLeftPower);
        rfMotor.setPower(frontRightPower);
        rrMotor.setPower(backRightPower);

        sleep(movement);
        lfMotor.setPower(0);
        lrMotor.setPower(0);
        rfMotor.setPower(0);
        rrMotor.setPower(0);
    }
    public void moveSideways(double speed, long time){
        lfMotor.setPower(-speed);
        rfMotor.setPower(speed);
        lrMotor.setPower(speed);
        rrMotor.setPower(-speed);

        sleep(time);

        lfMotor.setPower(0.0);
        rfMotor.setPower(0.0);
        lrMotor.setPower(0.0);
        rrMotor.setPower(0.0);

    }

    public void rotate(long degrees, double power) {
        telemetry.addData("Current Status", "Rotate");
        telemetry.update();

        lfMotor.setPower(-power);
        rfMotor.setPower(-power);
        lrMotor.setPower(power);
        rrMotor.setPower(power);

        sleep(degrees);

        lfMotor.setPower(0);
        rfMotor.setPower(0);
        lrMotor.setPower(0);
        rrMotor.setPower(0);

    }
}
