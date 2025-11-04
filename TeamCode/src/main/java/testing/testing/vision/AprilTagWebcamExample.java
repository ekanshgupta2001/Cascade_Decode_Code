package testing.testing.vision;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class AprilTagWebcamExample extends OpMode {
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();

    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagBySpecificID(20);
        if (id20 != null) {
            aprilTagWebcam.displayTelemetry(id20);
            telemetry.addData("Status", "Tag ID 20 detected and data displayed.");
        } else {
            telemetry.addData("Status", "Tag ID 20 not found in current frame.");
        }
        telemetry.update();
    }

    public void stop(){
        aprilTagWebcam.stop();
    }
}
