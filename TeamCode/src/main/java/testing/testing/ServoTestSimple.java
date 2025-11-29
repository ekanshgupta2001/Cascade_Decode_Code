package testing.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name="ServoTestSimple")
public class ServoTestSimple extends LinearOpMode {
    private Servo hood;

    @Override
    public void runOpMode() throws InterruptedException {
        hood = hardwareMap.get(Servo.class, "AH"); // use your servo name here

        waitForStart();

// move to 0
        hood.setPosition(0.0);
        sleep(1000);

// move to halfway
        hood.setPosition(0.5);
        sleep(1000);

// move to max
        hood.setPosition(1.0);
        sleep(1000);

// keep last position
        while (opModeIsActive()) {
            idle();
        }
    }
}
