package org.firstinspires.ftc.teamcode.decode_auto;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class blueFarAuto extends LinearOpMode {
    private Webcam webcam;
    private DriveTrain driveTrain;
    private Intake intake;
    private Shooter shooter;

    private final int BLUE_SCORE_ZONE_ID = 20;
    private static final double DESIRED_DISTANCE_CM = 100.0;
    private static final double ALIGNMENT_THRESHOLD_DEG = 2.0;
    private static final double DISTANCE_THRESHOLD_CM = 5.0;

    private static final double TURN_GAIN = 0.02;
    private static final double FORWARD_GAIN = 0.03;
    private static final double MAX_AUTO_SPEED = 0.5;

    @Override
    public void runOpMode() {
        webcam = new Webcam(hardwareMap, telemetry, "Webcam 1");
        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap, intake);

        webcam.setTargetTagID(BLUE_SCORE_ZONE_ID);

        telemetry.addData("Status", "Ready. Waiting for Start.");
        telemetry.update();

        waitForStart();

        driveTrain.drive(0.4, 0, 0);
        CommandScheduler.getInstance().schedule(shooter.feedUpCommand());
        sleep(500);
        driveTrain.stop();

        driveToTag(DESIRED_DISTANCE_CM);

        CommandScheduler.getInstance().schedule(shooter.scoreCloseCommand());
        sleep(2000);
        CommandScheduler.getInstance().run();

        turnDegrees(1800);

        driveTrain.drive(0.5, 0, 0);
        CommandScheduler.getInstance().schedule(intake.inCommand());
        sleep(3000);
        driveTrain.stop();
        CommandScheduler.getInstance().run();

        driveTrain.drive(-0.7, 0, 0);
        sleep(2000);

        turnDegrees(-1800);

        driveToTag(DESIRED_DISTANCE_CM);

        CommandScheduler.getInstance().schedule(shooter.scoreCloseCommand());
        sleep(2000);
        CommandScheduler.getInstance().run();

        turnDegrees(1800);
        driveTrain.drive(0.5, 0, 0);
        CommandScheduler.getInstance().schedule(intake.inCommand());
        sleep(3000);
        driveTrain.stop();

        telemetry.addData("Auto phase", "Done");
        telemetry.update();
        webcam.stop();
    }

    public void driveToTag(double desiredDistanceCM) {
        while (opModeIsActive()) {
            webcam.periodic();
            AprilTagDetection targetTag = webcam.getTargetTag();

            if (targetTag != null) {
                double distanceError = targetTag.ftcPose.range - desiredDistanceCM;
                double headingError = targetTag.ftcPose.bearing;

                if (Math.abs(distanceError) < DISTANCE_THRESHOLD_CM && Math.abs(headingError) < ALIGNMENT_THRESHOLD_DEG) {
                    driveTrain.stop();
                    return;
                }

                double forwardPower = distanceError * FORWARD_GAIN;
                double turnPower = headingError * TURN_GAIN;
                double strafePower = 0.0;

                forwardPower = Math.copySign(Math.min(Math.abs(forwardPower), MAX_AUTO_SPEED), forwardPower);
                turnPower = Math.copySign(Math.min(Math.abs(turnPower), MAX_AUTO_SPEED), turnPower);

                driveTrain.drive(forwardPower, strafePower, turnPower);
                webcam.displayTagTelemetry(targetTag);

            } else {
                driveTrain.stop();
                telemetry.addLine("Tag lost. Stopping movement.");
            }
            telemetry.update();
        }
    }

    public void turnDegrees(int milliseconds) {
        driveTrain.drive(0, 0, Math.copySign(0.4, milliseconds));
        sleep(Math.abs(milliseconds));
        driveTrain.stop();
    }
}
