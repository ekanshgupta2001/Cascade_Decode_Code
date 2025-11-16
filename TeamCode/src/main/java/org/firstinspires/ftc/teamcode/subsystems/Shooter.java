package org.firstinspires.ftc.teamcode.subsystems;

// Remove non-standard imports
// import static com.bylazar.panels.Panels.stop;
// import org.firstinspires.ftc.teamcode.commands.Command;
// import org.firstinspires.ftc.teamcode.commands.InstantCommands;
// import org.firstinspires.ftc.teamcode.commands.SequentialCommands;
// import org.firstinspires.ftc.teamcode.commands.WaitCommand;

// Import the correct FTCLib command classes
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
public class Shooter extends SubsystemBase {
    private Servo AH;
    private DcMotorEx S;
    public static double close = 1250;
    public static double far = 1400;
    public static double HUp = 0.45;
    public static double HDown = 0.25;
    private final Intake intakeSubsystem;

    public Shooter(HardwareMap hardwareMap, Intake intakeSubsystem) {
        S = hardwareMap.get(DcMotorEx.class, "SM");
        AH = hardwareMap.get(Servo.class, "AH");
        this.intakeSubsystem = intakeSubsystem;


        S.setDirection(DcMotorSimple.Direction.FORWARD);
        S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        AH.setPosition(HDown);
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

    public void stopMotor(){
        S.setVelocity(0);
    }

    public void feedUp(){
        AH.setPosition(HUp);
    }

    public void feedDown(){
        AH.setPosition(HDown);
    }

    public Command spinCloseCommand(){
        return new InstantCommand(this::spinClose, this);
    }
    public Command spinFarCommand(){
        return new InstantCommand(this::spinFar, this);
    }

    public Command stopCommand(){
        return new InstantCommand(this::stopMotor, this);
    }

    public Command feedUpCommand(){
        return new InstantCommand(this::feedUp);
    }

    public Command feedDownCommand(){
        return new InstantCommand(this::feedDown);
    }
    public Command scoreFarCommand(){
        return new SequentialCommandGroup(
                spinFarCommand(),
                new WaitCommand(600),
                intakeSubsystem.inCommand(),
                new WaitCommand(400),
                stopCommand(),
                intakeSubsystem.idleCommand()
        );
    }

    public Command scoreCloseCommand(){
        return new SequentialCommandGroup(
                spinCloseCommand(),
                new WaitCommand(400),
                intakeSubsystem.inCommand(),
                new WaitCommand(400),
                stopCommand(),
                intakeSubsystem.idleCommand()
        );
    }
}
