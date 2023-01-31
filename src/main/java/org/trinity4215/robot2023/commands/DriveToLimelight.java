// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.trinity4215.robot2023.Constants.DriveConstants;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.Limelight;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveToLimelight extends CommandBase {

    private Limelight limelight;
    private Drivetrain drivetrain;

    /** Creates a new DriveToLimelight. */
    public DriveToLimelight(Limelight limelight, Drivetrain drivetrain) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.limelight = limelight;
        this.drivetrain = drivetrain;
        addRequirements(limelight, drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        PhotonPipelineResult r = limelight.getLatestResult();
        boolean c = r.hasTargets();
        SmartDashboard.putBoolean("HasTag", c);
        if (c) {
            PhotonTrackedTarget t = r.getBestTarget();
            double area = t.getArea();
            if (area < DriveConstants.kOneMeterArea - DriveConstants.kAreaDeadzone) {
                drivetrain.driveDualJoystickPercent(0.2, 0.2);
            }
            else if (area > DriveConstants.kOneMeterArea - DriveConstants.kAreaDeadzone) {
                drivetrain.driveDualJoystickPercent(0, 0);
            }
            else {
                drivetrain.driveDualJoystickPercent(0, 0);
            }
        } else {
            drivetrain.driveDualJoystickPercent(0, 0);
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
