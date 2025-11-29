package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Configurable
public class ShooterWait extends SubsystemBase {
    private Servo AH;
    private DcMotorEx S;
    private double targetVelocity = 0;
    public static double close = 1100;
    public static double far = 1375;
    public static double HUp = 0.40;
    public static double HDown = 0.20;
    public static double HZero = 0.0;
    public static double intakePower = -150;;
    private final Intake intakeSubsystem;
    private double currentHoodPosition = 0.0;
    private TelemetryManager telemetryM;
    private final double VELOCITY_TOLERANCE_MAGNITUDE = 0.05;
    private boolean actionIsRunning = false;

    public ShooterWait(HardwareMap hardwareMap, Intake intakeSubsystem) {
        S = hardwareMap.get(DcMotorEx.class, "SM");
        AH = hardwareMap.get(Servo.class, "AH");
        AH.setDirection(Servo.Direction.REVERSE);
        this.intakeSubsystem = intakeSubsystem;
        S.setDirection(DcMotorSimple.Direction.FORWARD);
        S.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        S.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        AH.setPosition(HDown);
    }

    public void setVelocity(double velocity){
        S.setVelocity(velocity);
        this.targetVelocity = velocity;
    }

    public double getVelocity() {
        return S.getVelocity();
    }

    public double getMotorPower() {
        return S.getPower();
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

    public void feedZero(){
        AH.setPosition(HZero);
    }

    public Command spinCloseCommand(){
        return new InstantCommand(this::spinClose, this);
    }

    public Command intakein(){
        return new RunCommand(this::intake, this);
    }

    public Command spinFarCommand(){
        return new InstantCommand(this::spinFar, this);
    }

    public Command stopCommand(){
        return new RunCommand(this::stopMotor, this);
    }

    public Command feedUpCommand(){
        return new InstantCommand(this::feedUp);
    }

    public Command feedDownCommand(){
        return new InstantCommand(this::feedDown);
    }

    public Command feedZeroCommand(){
        return new InstantCommand(this::feedZero);
    }

    public boolean isAtVelocity(double targetVelocity) {
        return Math.abs(S.getVelocity() - targetVelocity) < VELOCITY_TOLERANCE_MAGNITUDE;
    }

    public Command scoreFarCommand(){
        return new SequentialCommandGroup(
                spinFarCommand(),
                new WaitUntilCommand(() -> isAtVelocity(far)),
                new WaitCommand(300),
                intakeSubsystem.inCommand(),
                new WaitCommand(1000),
                stopCommand(),
                intakeSubsystem.idleCommand()
        );
    }

    public Command scoreCloseCommand(){
        return new SequentialCommandGroup(
                spinCloseCommand(),
                new WaitUntilCommand(() -> isAtVelocity(close)),
                new WaitCommand(300),
                intakeSubsystem.inCommand(),
                new WaitCommand(1000),
                stopCommand(),
                intakeSubsystem.idleCommand()
        );
    }

    public void getTelemetryData(Telemetry telemetry) {
        telemetry.addData("Shooter Velocity (actual)", S.getVelocity());
        telemetry.addData("Target Close Velocity", close);
        telemetry.addData("Target Far Velocity", far);
    }

    public Command closeAuto(){
        return new SequentialCommandGroup(
                feedUpCommand(),
                spinCloseCommand(),
                new WaitUntilCommand(() -> isAtVelocity(close)),
                intakeSubsystem.inCommand(),
                new WaitCommand(300),
                intakeSubsystem.stopCommand(),
                spinCloseCommand(),
                new WaitUntilCommand(() -> isAtVelocity(close)),
                intakeSubsystem.inCommand(),
                new WaitCommand(750),
                intakeSubsystem.stopCommand(),
                stopCommand(),
                new InstantCommand(() -> {
                    actionIsRunning = false;
                })
        );
    }

    public Command stop(){
        return new SequentialCommandGroup(
                intakein(),
                new WaitCommand(500),
                stopCommand()
        );
    }

    public Command farAuto(){
        return new SequentialCommandGroup(
                feedUpCommand(),
                spinFarCommand(),
                new WaitUntilCommand(() -> isAtVelocity(far)),
                intakeSubsystem.inCommand(),
                new WaitCommand(300),
                intakeSubsystem.stopCommand(),
                spinFarCommand(),
                new WaitUntilCommand(() -> isAtVelocity(far)),
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
