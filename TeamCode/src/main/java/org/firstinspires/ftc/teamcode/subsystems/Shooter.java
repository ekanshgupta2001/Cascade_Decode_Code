package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter extends SubsystemBase {
    private Servo AH;
    private DcMotorEx S;
    public static double close = 1250;
    public static double far = 1400;
    public static double HUp = 0.5;
    public static double HDown = 0.1;
    public static double intakePower = -150;
    private final Intake intakeSubsystem;
    private TelemetryManager telemetryM;
    private Timer actionTimer;
    private boolean actionIsRunning = false;

    public Shooter(HardwareMap hardwareMap, Intake intakeSubsystem) {
        S = hardwareMap.get(DcMotorEx.class, "SM");
        AH = hardwareMap.get(Servo.class, "AH");
        this.intakeSubsystem = intakeSubsystem;


        S.setDirection(DcMotorSimple.Direction.FORWARD);
        S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
    public void intake(){
        setVelocity(intakePower);
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
        return new RunCommand(this::spinClose, this);
    }
    public Command intakein(){
        return new RunCommand(this::intake, this);
    }
    public Command spinFarCommand(){
        return new RunCommand(this::spinFar, this);
    }

    public Command stopCommand(){
        return new RunCommand(this::stopMotor, this);
    }

    public Command feedUpCommand(){
        return new RunCommand(this::feedUp);
    }

    public Command feedDownCommand(){
        return new RunCommand(this::feedDown);
    }
    public boolean isAtVelocity(double targetVelocity) {
        double tolerance = 0.05 * Math.abs(targetVelocity);
        return Math.abs(S.getVelocity() - targetVelocity) < tolerance;
    }
    public Command scoreFarCommand(){
        return new SequentialCommandGroup(
                spinFarCommand(),
                new WaitUntilCommand(() -> isAtVelocity(far)),
                intakeSubsystem.inCommand(),
                new WaitCommand(4000),
                stopCommand(),
                intakeSubsystem.idleCommand()
        );
    }

    public Command scoreCloseCommand(){
        return new SequentialCommandGroup(
                spinCloseCommand(),
                new WaitUntilCommand(() -> isAtVelocity(close)),
                intakeSubsystem.inCommand(),
                new WaitCommand(4000),
                stopCommand(),
                intakeSubsystem.idleCommand()
        );
    }

    public void getTelemetryData(Telemetry telemetry) {
        telemetry.addData("Shooter Velocity (actual)", S.getVelocity());
        telemetry.addData("Target Close Velocity", close);
        telemetry.addData("Target Far Velocity", far);
    }


    public Command CloseAuto(){
        return new SequentialCommandGroup(
               spinCloseCommand(),
                new WaitUntilCommand(() -> isAtVelocity(close)),
               intakeSubsystem.inCommand(),
               new WaitCommand(1250),
               intakeSubsystem.stopCommand(),
               spinCloseCommand(),
               intakeSubsystem.inCommand(),
               new WaitCommand(1250),
               intakeSubsystem.stopCommand(),
               stopCommand(),
               new InstantCommand(() -> {
                   actionIsRunning = false;
               })
        );
    }

    public Command FarAuto(){
        return new SequentialCommandGroup(
                spinFarCommand(),
                new WaitUntilCommand(() -> isAtVelocity(far)),
                intakeSubsystem.inCommand(),
                new WaitCommand(1250),
                intakeSubsystem.stopCommand(),
                spinFarCommand(),
                intakeSubsystem.inCommand(),
                new WaitCommand(1250),
                intakeSubsystem.stopCommand(),
                stopCommand(),
                new InstantCommand(() -> {

                    actionIsRunning = false;
                })
        );
    }

    public boolean isAutoActionRunning() {
        return actionIsRunning;
    }
}
