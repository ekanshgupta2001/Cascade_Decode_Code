package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCommand implements Command{
    private final double timeSeconds;
    private final ElapsedTime timer = new ElapsedTime();
    public WaitCommand(double timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    @Override
    public void init() {
        timer.reset();
    }

    @Override
    public void update() {  }

    @Override
    public boolean isFinished() {
        return timer.seconds() >= timeSeconds;
    }

    @Override
    public void end() { }
}
