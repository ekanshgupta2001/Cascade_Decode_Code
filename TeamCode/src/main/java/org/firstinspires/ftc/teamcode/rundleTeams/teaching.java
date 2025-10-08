package org.firstinspires.ftc.teamcode.rundleTeams;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name = "Teaching", group = "Robot")
public class teaching extends OpMode{
    private DcMotor lfMotor = null;
    private DcMotor lrMotor = null;
    private DcMotor rfMotor = null;
    private DcMotor rrMotor = null;
    private DcMotor intakeMotorL = null;
    private DcMotor intakeMotorR = null;
    double intakeDirection = 0;

    public void init(){
        //This is explaining the hardware mapping
        rfMotor = hardwareMap.dcMotor.get("frontRightMotor");
        lfMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        rrMotor = hardwareMap.dcMotor.get("backRightMotor");
        lrMotor = hardwareMap.dcMotor.get("backLeftMotor");

        intakeMotorL = hardwareMap.get(DcMotor.class, "intakeMotorLeft");
        intakeMotorR = hardwareMap.get(DcMotor.class, "intakeMotorRight");
        intakeMotorL.setDirection(DcMotor.Direction.FORWARD);
        intakeMotorR.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void loop(){
        movement();
        intake();
    }

    public void movement(){
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        double lf = drive + strafe + turn;
        double lr = drive - strafe + turn;
        double rf = drive - strafe - turn;
        double rr = drive + strafe - turn;

        double max = Math.max(Math.abs(lf), Math.max(Math.abs(lr), Math.max(Math.abs(rf), Math.abs(rr))));
        if (max > 1) {
            lf /= max;
            lr /= max;
            rf /= max;
            rr /= max;
        }
    }

    public void intake(){
        //Test pressing down left trigger
        //gamepad1.left_trigger = 1.0
        //gamepad1.right_trigger = 0.0
        //Therefore intakeDirection = 1.0
        intakeDirection = gamepad1.left_trigger - gamepad1.right_trigger;

        if (intakeDirection > 0) {
            //Move Forward
            intakeMotorR.setPower(1.0);
            intakeMotorL.setPower(-1.0);
        } else if (intakeDirection < 0) {
            //Move Reverse
            intakeMotorL.setPower(1.0);
            intakeMotorR.setPower(-1.0);
        } else {
            //Stop
            intakeMotorR.setPower(0);
            intakeMotorL.setPower(0);
        }
    }
}
