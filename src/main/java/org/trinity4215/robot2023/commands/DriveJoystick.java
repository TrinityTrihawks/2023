// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import java.util.function.DoubleSupplier;

import org.trinity4215.robot2023.Constants.DriveConstants.DriveType;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveJoystick extends CommandBase {
    private static final double kStaticJoystickScalar = 0.4;
    private final Drivetrain drivetrain;
    private final DoubleSupplier leftYSupplier;
    private final DoubleSupplier rightYSupplier;
    private final DoubleSupplier rightTwistSupplier;

    /**
     * Creates a new DriveJoystick.
     *
     * @param drivetrain The subsystem used by this command.
     */
    public DriveJoystick(Drivetrain drivetrain,
                         DoubleSupplier leftY,
                         DoubleSupplier rightY,
                         DoubleSupplier rightTwist) {
                            
        this.drivetrain = drivetrain;
        leftYSupplier = leftY;
        rightYSupplier = rightY;
        rightTwistSupplier = rightTwist;
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
            drivetrain.driveDualJoystickPercent(
                leftYSupplier.getAsDouble()* kStaticJoystickScalar,
                rightYSupplier.getAsDouble()* kStaticJoystickScalar);
        } else {
            drivetrain.driveSingleJoystickPercent(
                rightYSupplier.getAsDouble(), 
                rightTwistSupplier.getAsDouble());
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
