package org.firstinspires.ftc.teamcode.rundleTeams;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Prototype", group = "Robot")
public class help extends OpMode {
    private DcMotor scoreMotorL = null;
    private DcMotor scoreMotorR = null;

    @Override
    public void init() {
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
