// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.fenris.commands;

import org.trinity4215.fenris.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveZero extends CommandBase {

	private Drivetrain drive;

	public DriveZero(Drivetrain drivetrain) {
		drive = drivetrain;
		addRequirements(drivetrain);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		System.out.println(this + ": beginning to not drive");
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		System.out.println(this + ": not driving");
		drive.drive(0, 0, 0, false);
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