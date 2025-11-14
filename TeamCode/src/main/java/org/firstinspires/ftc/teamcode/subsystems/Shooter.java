package org.firstinspires.ftc.teamcode.subsystems;

import static com.bylazar.panels.Panels.stop;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.commands.InstantCommands;
import org.firstinspires.ftc.teamcode.commands.SequentialCommands;
import org.firstinspires.ftc.teamcode.commands.WaitCommand;

public class Shooter {
    private Servo AH;
    private DcMotorEx S;
    public static double close = 1250;
    public static double far = 1400;
    public static double HUp = 0.45;
    public static double HDown = 0.1;
    Intake i;

    public Shooter(HardwareMap hardwareMap) {
        S = hardwareMap.get(DcMotorEx.class, "SM");
        AH = hardwareMap.get(Servo.class, "AH");

        S.setDirection(DcMotorSimple.Direction.FORWARD);
        S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        AH.setPosition(HDown);

        i = new Intake(hardwareMap);
    }

    public void setVelocity(double velocity){
        S.setVelocity(velocity);
    }
    public void spinClose(){
        setVelocity(close);
    }
    public void spinFar(){
        setVelocity(far);
    }

    public void stop(){
        S.setVelocity(0);
    }

    public void feedUp(){
        AH.setPosition(HUp);
    }

    public void feedDown(){
        AH.setPosition(HDown);
    }

    public Command spinCloseCommand(){
        return new InstantCommands(this::spinClose);
    }
    public Command spinFarCommand(){
        return new InstantCommands(this::spinFar);
    }

    public Command stopCommand(){
        return new InstantCommands(this::stop);
    }

    public Command feedUpCommand(){
        return new InstantCommands(this::feedUp);
    }

    public Command feedDownCommand(){
        return new InstantCommands(this::feedDown);
    }

    public Command scoreFarCommand(){
        return new SequentialCommands(
                spinFarCommand(),
                new WaitCommand(6),
                i.in(),
                new WaitCommand(4),
                stopCommand(),
                i.stop()
        );
    }

    public Command scoreCloseCommand(){
        return new SequentialCommands(
                spinCloseCommand(),
                new WaitCommand(4),
                i.in(),
                new WaitCommand(4),
                stopCommand(),
                i.stop()
        );
    }

}
