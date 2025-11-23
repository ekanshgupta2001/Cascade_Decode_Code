package testing.testing;

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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class intakeTest extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor intakeMotor = null;
    private DcMotor scoreMotor = null;
    private TelemetryManager telemetryM;
    private double intakeDirection = 0.0;
    @Override
    public void init() {

//        follower = Constants.createFollower(hardwareMap);

        intakeMotor = hardwareMap.get(DcMotor.class, "IM");
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        scoreMotor = hardwareMap.get(DcMotor.class, "SM");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        pathTimer = new Timer();
        actionTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        telemetry.addLine("Auto complete");
        telemetry.update();

    }

    @Override
    public void loop() {
        intake();
    }


    public void intake(){
        intakeDirection = gamepad1.left_trigger - gamepad1.right_trigger;

        if (intakeDirection > 0) {
            intakeMotor.setPower(1.0);
            scoreMotor.setPower(-0.1);
        } else if (intakeDirection < 0) {
            intakeMotor.setPower(-1.0);
            scoreMotor.setPower(0.1);
        } else {
            intakeMotor.setPower(0.0);
            scoreMotor.setPower(0.0);
        }
    }
}