package org.firstinspires.ftc.teamcode.subsystems;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.commands.InstantCommands;

@Configurable
public class Intake {
    private final DcMotorEx i;
    public static double idle = 0;
    public static double in = 1;
    public static double out = -1;


    public Intake(HardwareMap hardwareMap) {
        i = hardwareMap.get(DcMotorEx.class, "IM");
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

    public Command idle() {
        return new InstantCommands(() -> set(idle));
    }

    public Command in() {
        return new InstantCommands(() -> set(in));
    }

    public Command out() {
        return new InstantCommands(() -> set(out));
    }

    public Command stop() {
        return new InstantCommands(() -> set(0));
    }
}
