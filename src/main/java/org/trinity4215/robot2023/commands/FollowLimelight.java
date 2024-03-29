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

public class FollowLimelight extends CommandBase {
  private Limelight limelight;
  private Drivetrain drivetrain;

  private double limelightZeroAngle = 0;

  /** Creates a new FollowLimelight. */
  public FollowLimelight(Limelight limelight, Drivetrain drivetrain) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.limelight = limelight;
    this.drivetrain = drivetrain;
    addRequirements(limelight, drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }
  //luke is the best programmer ever
  

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    PhotonPipelineResult r = limelight.getLatestResult();
    boolean c = r.hasTargets();
    SmartDashboard.putBoolean("HasTag", c);
    if (c) {
      PhotonTrackedTarget t = r.getBestTarget();
      double tagYaw = t.getYaw();
      double rotSpeed = 0;
      double fwdSpeed = 0;
      double finalLeft = 0;
      double finalRight = 0;
      double tagMax = 0.3;
      double tagMin = 0.1;
      SmartDashboard.putNumber("Tag Yaw", tagYaw);
      double area = t.getArea();
      if ((-30 < tagYaw) && (tagYaw < -3)) {
        rotSpeed = -(Math.abs((tagYaw - 3) / 25) * (tagMax - tagMin) + tagMin);
      } else if ((30 > tagYaw) && (tagYaw > 3)) {
        rotSpeed = Math.abs((tagYaw - 3) / 25) * (tagMax - tagMin) + tagMin;
      } else {
        rotSpeed = 0;
      }
      if (area < DriveConstants.kOneMeterArea - DriveConstants.kAreaDeadzone) {
        fwdSpeed = 0.2;
      } else if (area > DriveConstants.kOneMeterArea - DriveConstants.kAreaDeadzone) {
        fwdSpeed = 0;
      } else {
        fwdSpeed = 0;
      }

      SmartDashboard.putNumber("rotSpeed", rotSpeed);
      SmartDashboard.putNumber("fwdSpeed", fwdSpeed);

      finalLeft = rotSpeed + fwdSpeed;
      finalRight = -rotSpeed + fwdSpeed;

      // SmartDashboard.putNumber("Speed", (curAngle - tagYaw - limelightZeroAngle));
      // SmartDashboard.putNumber("sind(angle)", rotSpeed);
      // int deadzoneScalar = Math.abs(rotSpeed) <= DriveConstants.kDeadzone ? 0 : 1;
      // SmartDashboard.putBoolean("in dead zone", deadzoneScalar == 0);
      // double output = -1 * rotSpeed
      // * deadzoneScalar
      // * DriveConstants.kMaxSpeedPercent
      // * (1 - DriveConstants.kMinTurnSpeed) + DriveConstants.kMinTurnSpeed;

      SmartDashboard.putNumber("OutputL", finalLeft);
      SmartDashboard.putNumber("OutputR", finalRight);
      drivetrain.driveTankPercent(finalLeft, finalRight);

    } else {
      SmartDashboard.putString("Targets", "");
      SmartDashboard.putString("Best Target", "");
      SmartDashboard.putNumber("Best Target Yaw", 0);
      SmartDashboard.putNumber("Best Target Pitch", 0);
      drivetrain.driveTankPercent(0, 0);
    }

    // System.out.println(r);
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
