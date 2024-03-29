// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.subsystems.Intake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class PositionalIntakeTest extends CommandBase {

  private Intake intake;

  /** Creates a new AbsoluteEncoderMove. */
  public PositionalIntakeTest(Intake intake) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.intake = intake;

    addRequirements(intake);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    SmartDashboard.putNumber("IntakeAbsolutEncoderPosition",intake.getAbsoluteEncoderPosition());
    intake.driveToDegrees(115, 0.1);

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
