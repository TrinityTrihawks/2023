// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.trinity4215.robot2023.Constants.DriveConstants;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.LimelightPhotonVision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class FollowLimelight extends CommandBase {
  private LimelightPhotonVision limelight;
  private Drivetrain drivetrain;

  /** Creates a new FollowLimelight. */
  public FollowLimelight(LimelightPhotonVision limelight, Drivetrain drivetrain) {
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
    SmartDashboard.putBoolean("HasTargets", c);
    if (c) {
      SmartDashboard.putString("Targets", r.getTargets().toString());
      PhotonTrackedTarget t = r.getBestTarget();
      SmartDashboard.putNumber("Best Target Yaw", t.getYaw());
      SmartDashboard.putNumber("Best Target Pitch", t.getPitch());

      double target = t.getYaw();

      SmartDashboard.putNumber("Target Angle", target);
      double curAngle = drivetrain.getGyroAngle();
      SmartDashboard.putNumber("input angle", curAngle);
      double speed = Math.sin((curAngle - target) * Math.PI / 180 / 2);
      SmartDashboard.putNumber("sind(angle)", speed);
      int deadzoneScalar = Math.abs(speed) <= DriveConstants.kDeadzone? 0 : 1;
      SmartDashboard.putBoolean("in dead zone", deadzoneScalar == 0);
      double output = 
          -1 * speed 
          * deadzoneScalar 
          * DriveConstants.kMaxSpeedPercent
          * (1 - DriveConstants.kMinTurnSpeed) + DriveConstants.kMinTurnSpeed;
          
      SmartDashboard.putNumber("output", output);
      drivetrain.driveDualJoystickPercent(output, -output);

    } else {
      SmartDashboard.putString("Targets", "");
      SmartDashboard.putString("Best Target", "");
      SmartDashboard.putNumber("Best Target Yaw", 0);
      SmartDashboard.putNumber("Best Target Pitch", 0);
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
