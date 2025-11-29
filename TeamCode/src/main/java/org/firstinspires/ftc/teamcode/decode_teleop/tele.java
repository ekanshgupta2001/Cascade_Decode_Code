package org.firstinspires.ftc.teamcode.decode_teleop;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp
public class tele extends OpMode{
    private Follower follower;
    Intake i;
    Shooter s;
    Webcam w;
    private GamepadEx driverGamepad;
    private final int RED_SCORE_ZONE_ID = 24;
    private final int BLUE_SCORE_ZONE_ID = 20;
    public static Pose startingPose;
    double distance = -1;
    private TelemetryManager telemetryM;
    private boolean slowModeActive = false;
    private double adjustSpeed = 0.25;

    private boolean lastRightBumperState = false;
    private boolean lastLeftBumperState = false;
    private boolean isIntakeInward = false;
    private boolean isIntakeOutward = false;
    private boolean isShooterActive = false;


    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        w = new Webcam(hardwareMap, telemetry, "Webcam 1");

        i = new Intake(hardwareMap);
        s = new Shooter(hardwareMap, i);

        driverGamepad = new GamepadEx(gamepad1);
    }

    @Override
    public void init_loop(){
        driverGamepad.readButtons();

        if (gamepad1.dpadUpWasPressed()){
            w.setTargetTagID(BLUE_SCORE_ZONE_ID);
            telemetry.addLine("Blue Tag Found");
        }
        if (gamepad1.dpadDownWasPressed()){
            w.setTargetTagID(RED_SCORE_ZONE_ID);
            telemetry.addLine("Red Tag Found");
        }
    }

    @Override
    public void start(){
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        driverGamepad.readButtons();

        follower.update();
        telemetryM.update();
        hood();
        intake();
        drive();

        lastRightBumperState = driverGamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER);
        lastLeftBumperState = driverGamepad.isDown(GamepadKeys.Button.LEFT_BUMPER);

        if (gamepad1.aWasPressed()) {
            isShooterActive = true;
        }
        if (gamepad1.xWasPressed()){
            CommandScheduler.getInstance().schedule(s.stop());
            isShooterActive = false;
        }

        if (gamepad1.xWasPressed()){
            CommandScheduler.getInstance().schedule(s.stopCommand());
            CommandScheduler.getInstance().schedule(s.stopCommand());
            CommandScheduler.getInstance().schedule(s.feedDownCommand());
            isShooterActive = false;
        }

        w.displayTagTelemetry(w.getTargetTag());
        s.getTelemetryData(telemetry);
        i.getTelemetryData(telemetry);
        telemetry.addData("Distance to April Tag", distance);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }

    public void drive(){
        if (gamepad1.dpadUpWasPressed()){
            slowModeActive = true;
            telemetry.addLine("Slow Mode On");
        }
        if (gamepad1.dpadDownWasPressed()){
            slowModeActive = false;
            telemetry.addLine("Slow Mode off");
        }

        if (!slowModeActive)
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    true
            );

        if (slowModeActive)
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y * adjustSpeed,
                    -gamepad1.left_stick_x * adjustSpeed,
                    -gamepad1.right_stick_x * adjustSpeed,
                    true
            );

        if (gamepad1.dpadRightWasPressed() && adjustSpeed <= 1.0) {
            adjustSpeed += 0.2;
        }

        if (gamepad2.dpadLeftWasPressed() && adjustSpeed >= 0.0) {
            adjustSpeed -= 0.2;
        }

        telemetry.addLine("Should move");
    }
    public void intake(){
        boolean currentRightBumper = driverGamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER);
        boolean currentLeftBumper = driverGamepad.isDown(GamepadKeys.Button.LEFT_BUMPER);

        if (currentRightBumper && currentLeftBumper) {
            CommandScheduler.getInstance().schedule(i.stopCommand());
            isIntakeInward = false;
            isIntakeOutward = false;
        }
        else if (currentRightBumper && !lastRightBumperState) {
            isIntakeInward = !isIntakeInward;
            isIntakeOutward = false;
        }

        else if (currentLeftBumper && !lastLeftBumperState) {
            isIntakeOutward = !isIntakeOutward;
            isIntakeInward = false;
        }

        if (isIntakeInward) {
            CommandScheduler.getInstance().schedule(i.inCommand());
            CommandScheduler.getInstance().schedule(s.intakein());
        } else if (isIntakeOutward) {
            CommandScheduler.getInstance().schedule(i.outCommand());
        } else {
            if (i.getDefaultCommand() == null) {
                i.setDefaultCommand(i.idleCommand());
            }
        }
    }

    public void hood(){
        w.periodic();

        int currentID = w.getTargetTagID();
        if (currentID >= 0){
            distance = w.getDistancetoTagId();
        }
        if (isShooterActive && distance > 0) {
            if (distance >= 200) {
                CommandScheduler.getInstance().schedule(s.feedUpCommand());
                CommandScheduler.getInstance().schedule(s.scoreFarCommand());
            } else if (distance < 200) {
                CommandScheduler.getInstance().schedule(s.feedDownCommand());
                CommandScheduler.getInstance().schedule(s.scoreCloseCommand());
            }
        }

//        telemetry.addData("Distance to April Tag", distance);
//        telemetry.update();
    }
}
