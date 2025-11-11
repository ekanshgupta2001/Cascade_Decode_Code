package org.firstinspires.ftc.teamcode.decode_teleop;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp
public class tele extends OpMode {
    private Follower follower;
    Intake i;
    Shooter s;
    Webcam w;
    private final int RED_SCORE_ZONE_ID = 24;
    private final int BLUE_SCORE_ZONE_ID = 20;
    public static Pose startingPose;
    double distance = 0;
    boolean isIntakeOn = false;
    private TelemetryManager telemetryM;
    private boolean slowModeActive = false;
    private double adjustSpeed = 0.5;

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        w = new Webcam(hardwareMap, telemetry, "Webcam 1");
        s = new Shooter(hardwareMap);
        i = new Intake(hardwareMap);

    }

    @Override
    public void init_loop(){
        if (gamepad1.dpad_up){
            w.setTargetTagID(BLUE_SCORE_ZONE_ID);
        }
        if (gamepad1.dpad_down){
            w.setTargetTagID(RED_SCORE_ZONE_ID);
        }
    }

    @Override
    public void loop() {
        hood();
        intake();
        drive();
    }

    public void drive(){
        follower.startTeleopDrive();

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
        if (gamepad1.dpadRightWasPressed() && adjustSpeed >= 1.0) {
            adjustSpeed += 0.2;
        }

        //Optional way to change slow mode strength
        if (gamepad2.dpadLeftWasPressed()) {
            adjustSpeed -= 0.2;
        }
    }

    public void intake(){
        if (gamepad1.left_bumper){
            i.in();
            isIntakeOn = true;
        }
        if (gamepad1.right_bumper){
            i.out();
            isIntakeOn = true;
        }
        if (gamepad1.right_bumper && gamepad1.left_bumper){
            isIntakeOn = false;
        }
        if (!isIntakeOn){
            i.stop();
        }
    }

    public void hood(){
        w.periodic();
        AprilTagDetection target = w.getTargetTag();
        w.displayTagTelemetry(target);

        int currentID = w.getTargetTagID();
        if (currentID >= 0){
            distance = w.getDistancetoTagId(currentID);
        }

        if (distance >= 200){
            s.feedUpCommand();
        }
        else{
            s.feedDownCommand();
        }

        if (gamepad1.a){
            s.scoreCommand();
        }
    }
}
