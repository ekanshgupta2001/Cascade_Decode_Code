package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

@TeleOp(name = "Shooter PIDF Tuner Manual", group = "Tuning")
public class shooterPIDF extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Intake intakeSubsystem = new Intake(hardwareMap);
        Shooter shooterSubsystem = new Shooter(hardwareMap, intakeSubsystem);

        waitForStart();

        while (opModeIsActive()) {
            shooterSubsystem.setVelocity(Shooter.targetVelocityTuning);
            shooterSubsystem.periodic();
            shooterSubsystem.getTelemetryData(telemetry);
            telemetry.update();
        }
    }
}
