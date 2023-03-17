// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;


public class MoveIntakeArmDegreesRelative extends CommandBase {

    // flebphjk mjnhbogfvdcx
    // ng'i^lut i^ch'u i^yu

    private double targetPosition = Double.NaN;
    private final double kArmSpeed = .5;

    private final Intake intake;

    /**
     * @param relativeDegrees -- + is towards deployed, - towards retracted
     */
    public MoveIntakeArmDegreesRelative(double relativeDegrees, Intake intake) {
        targetPosition = relativeDegrees;
        this.intake = intake;
        addRequirements(intake);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // convert target from relative to absolute
        targetPosition = targetPosition + intake.getAbsoluteEncoderPosition();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        intake.driveToDegrees(targetPosition, kArmSpeed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intake.stopRaise();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return intake.isArmInDeadzone() || intake.isArmDown();
    }
}
