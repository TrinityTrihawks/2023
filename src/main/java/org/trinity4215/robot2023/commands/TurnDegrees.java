// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.DriveConstants;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurnDegrees extends CommandBase {

    private final Drivetrain drive;
    private final double target;

    private double curAngle = 0;

    /** Creates a new TurnDegrees. */
    public TurnDegrees(double targetAngle, Drivetrain drivetrain) {
        drive = drivetrain;
        target = targetAngle;
        addRequirements(drive);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        drive.resetGyro();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        CombinedLogging.putNumber("Target Angle", target);
        curAngle = drive.getGyroY();
        CombinedLogging.putNumber("input angle", curAngle);
        double speed = Math.sin((curAngle - target) * Math.PI / 180 / 2);
        CombinedLogging.putNumber("sind(angle)", speed);
        int deadzoneScalar = Math.abs(speed) <= DriveConstants.kDeadzone? 0 : 1;
        CombinedLogging.putBoolean("in dead zone", deadzoneScalar == 0);
        double output = 
            -1 * speed 
            * deadzoneScalar 
            * DriveConstants.kMaxSpeedPercent
            * (1 - DriveConstants.kMinTurnSpeed) + DriveConstants.kMinTurnSpeed;
            
        CombinedLogging.putNumber("output", output);
        drive.driveTankPercent(output, -output);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Math.abs(Math.sin((curAngle - target) * Math.PI / 180 / 2)) <= DriveConstants.kDeadzone;
    }
}
