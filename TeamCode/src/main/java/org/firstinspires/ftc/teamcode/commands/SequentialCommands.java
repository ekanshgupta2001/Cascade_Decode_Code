package org.firstinspires.ftc.teamcode.commands;

public class SequentialCommands implements Command{
    private final Command[] commands;
    private int index = 0;

    public SequentialCommands(Command... commands) {
        this.commands = commands;
    }



    @Override
    public void init() {
        if (commands.length > 0){
            commands[0].init();
        }
    }

    @Override
    public void update() {
        if (index >= commands.length) return;

        Command current = commands[index];
        current.update();

        if (current.isFinished()){
            current.end();
            index++;
            if (index < commands.length){
                commands[index].init();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return index >= commands.length;
    }

    @Override
    public void end() {

    }
}
