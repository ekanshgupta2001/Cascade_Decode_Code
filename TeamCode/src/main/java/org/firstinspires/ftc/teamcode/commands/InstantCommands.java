package org.firstinspires.ftc.teamcode.commands;

public class InstantCommands implements Command{
    private final Runnable action;
    private boolean done = false;

    public InstantCommands(Runnable action) {
        this.action = action;
    }

    @Override
    public void init() {
        action.run();
        done = true;
    }

    @Override
    public void update() {  }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public void end() { }
}
