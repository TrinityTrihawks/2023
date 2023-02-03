package org.trinity4215.bilbotbaggins.commands;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class GetRotVel extends CommandBase {

    private Drivetrain drive;

    public GetRotVel(Drivetrain drivetrain) {
        drive = drivetrain;
        addRequirements(drive);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        drive.driveArcade(0, 0.5);
        SmartDashboard.putNumber("angular velocity", drive.getGyroVel());

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        for (int i = 1; i < 200; ++i) {
            drive.driveTank(0, 0);
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
