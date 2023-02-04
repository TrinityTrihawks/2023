// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins.commands;

import java.util.function.DoubleSupplier;

import org.trinity4215.bilbotbaggins.Constants.OperatorConstants;
import org.trinity4215.bilbotbaggins.Constants.OperatorConstants.DriveType;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain.DrivetrainConstants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DriveJoystick extends CommandBase {
    private final Drivetrain drivetrain;
    private final DrivetrainConstants constants;
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
        constants = drivetrain.getConstants();
        leftYSupplier = leftY;
        rightYSupplier = rightY;
        rightTwistSupplier = rightTwist;
        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (OperatorConstants.kDriveType == DriveType.DUAL) {
            drivetrain.driveTank(
                -leftYSupplier.getAsDouble() * constants.kMaxSpeedPercent(),
                -rightYSupplier.getAsDouble() * constants.kMaxSpeedPercent()
            );
        } else {
            drivetrain.driveArcade(
                -rightYSupplier.getAsDouble() * constants.kMaxSpeedPercent(), 
                -rightTwistSupplier.getAsDouble() * constants.kMaxSpeedPercent()
            );
        }

        SmartDashboard.putNumber("ly", -leftYSupplier.getAsDouble());
        SmartDashboard.putNumber("ry", -rightYSupplier.getAsDouble());
        SmartDashboard.putNumber("rt", -rightTwistSupplier.getAsDouble());

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
