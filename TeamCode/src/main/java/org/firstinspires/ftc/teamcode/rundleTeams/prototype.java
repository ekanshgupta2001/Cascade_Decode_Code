package org.firstinspires.ftc.teamcode.rundleTeams;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

public class prototype extends OpMode {
    private DcMotor scoreMotorL = null;
    private DcMotor scoreMotorR = null;
    private CRServo intakeMotorL = null;
    private CRServo intakeMotorR = null;

    @Override
    public void init() {

        intakeMotorL = hardwareMap.get(CRServo.class, "intakeMotorLeft");
        intakeMotorR = hardwareMap.get(CRServo.class, "intakeMotorRight");
        intakeMotorL.setDirection(DcMotor.Direction.FORWARD);
        intakeMotorR.setDirection(DcMotor.Direction.REVERSE);

        scoreMotorL = hardwareMap.get(DcMotor.class, "ScoreMotorLeft");
        scoreMotorL.setDirection(DcMotor.Direction.REVERSE);
        scoreMotorR = hardwareMap.get(DcMotor.class, "ScoreMotorRight");
        scoreMotorR.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addLine("Auto complete");
        telemetry.update();

    }

    @Override
    public void loop() {
        intake();
    }

    public void intake(){
        if(gamepad1.right_bumper){
            scoreMotorL.setPower(1.0);
            scoreMotorR.setPower(1.0);
        }
        else if(gamepad1.left_bumper){
            scoreMotorL.setPower(-1.0);
            scoreMotorR.setPower(-1.0);
        }
        else {
            scoreMotorR.setPower(0.0);
            scoreMotorL.setPower(0.0);
        }

    }
}
