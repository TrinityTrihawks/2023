// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.Constants;
import org.trinity4215.robot2023.subsystems.RobotGyro;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurnToPoint extends CommandBase {
    private final RobotGyro robotgyro;

    /** Creates a new GyroBasics. */
    public TurnToPoint(RobotGyro robotgyro) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.robotgyro = robotgyro;
        addRequirements(robotgyro);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double angle = robotgyro.getAngleYaw();
        double speed = Math.sin(angle);
        int deadzoneScalar = Math.abs(speed) <= Constants.DriveConstants.kDeadzone? 0 : 1;
        double output = -1 * speed * deadzoneScalar * Constants.DriveConstants.kSanityLimit;
        robotgyro.drive(output, output);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
