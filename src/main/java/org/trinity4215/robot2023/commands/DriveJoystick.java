// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.Constants.DriveConstants.DriveType;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveJoystick extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;

    /**
     * Creates a new DriveJoystick.
     *
     * @param drivetrain The subsystem used by this command.
     */
    public DriveJoystick(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        drivetrain.setDriveType(DriveType.DUAL); // Init drive mode
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (drivetrain.getDriveType() == DriveType.DUAL) {
            // TODO: Write dual joystick drive code
            System.out.println("Dual mode active");
        } else {
            // TODO: Write single joystick drive code
            System.out.println("Single mode active");
        }
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
