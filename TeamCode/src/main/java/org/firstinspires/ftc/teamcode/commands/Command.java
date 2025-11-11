package org.firstinspires.ftc.teamcode.commands;

public interface Command {
    void init();
    void update();
    boolean isFinished();
    void end();
}


