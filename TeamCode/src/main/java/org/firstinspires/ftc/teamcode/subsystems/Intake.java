package org.firstinspires.ftc.teamcode.subsystems;

// Import the necessary FTCLib components
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake extends SubsystemBase {
    private final DcMotorEx i;
    public static double idle = 0;
    public static double in = 1;
    public static double out = -1;

    public Intake(HardwareMap hardwareMap) {
        i = hardwareMap.get(DcMotorEx.class, "IM");
        i.setDirection(DcMotorSimple.Direction.REVERSE);
        set(0);
    }

    public void set(double power) {
        i.setPower(power);
    }

    public void spinIn() {
        set(in);
    }

    public void spinOut() {
        set(out);
    }

    public void spinIdle() {
        set(idle);
    }

    public Command idleCommand() {
        // Use the correct FTCLib class
        return new InstantCommand(this::spinIdle, this);
    }

    public Command inCommand() {
        return new RunCommand(this::spinIn, this);
    }


    public Command outCommand() {
        return new RunCommand(this::spinOut, this);
    }

    public Command stopCommand() {
        // Use the correct FTCLib class
        return new InstantCommand(this::spinIdle, this);
    }
}
