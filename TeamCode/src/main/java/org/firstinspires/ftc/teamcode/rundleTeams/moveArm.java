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
public class moveArm extends OpMode{
    private DcMotor armMotor = null;
    int armMotorPosition = 0;
    public void init(){
        //This is arm motor existing
        armMotor = hardwareMap.dcMotor.get("armMotor");
    }

    @Override
    public void loop(){
        //set arm motor power to 1.0
        //If A is being pressed set power to 1.0
        //If B is being pressed set power to -1.0
        if(gamepad1.a){
            armMotor.setPower(1.0);
        } else if (gamepad1.b) {
            armMotor.setPower(-1.0);
        } else {
            armMotor.setPower(0.0);
        }

        //Finds the current arm position
        //Takes the current position and stores it in the variable
        armMotorPosition = armMotor.getCurrentPosition();
        //Sets the current arm position as the target position we want to keep it at
        armMotor.setTargetPosition(armMotorPosition);
        //
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Provides information to the driver hub
        telemetry.addData("Arm Motor Position", armMotor.getCurrentPosition());
        telemetry.update();
    }

}
