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
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp
public class score extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private DcMotor scoreMotor = null;
    private final ElapsedTime runtime = new ElapsedTime();
    private double lastTime;
    private int lastPosition = 0;
    private double velocity;
    private double timeCheck;
    private TelemetryManager telemetryM;

    @Override
    public void init() {

//        follower = Constants.createFollower(hardwareMap);

        scoreMotor = hardwareMap.get(DcMotor.class, "SM");
        scoreMotor.setDirection(DcMotorSimple.Direction.FORWARD);

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
        scoreArt();
    }



    public void scoreArt(){
        if (gamepad1.a) {
            scoreMotor.setPower(1.0);
            telemetry.addData("Score Power", scoreMotor.getPower());
            telemetry.update();

        } else if (gamepad1.b) {
            scoreMotor.setPower(-0.1);
        } else {
            scoreMotor.setPower(0.0);
        }
    }
}