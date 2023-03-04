// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;


import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.DriveConstants.AutoLevelState;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoLevel extends CommandBase {
    /** Creates a new AutoLevel. */
    private Drivetrain drivetrain;
    private AutoLevelState state;

    private static final double initialClimbThreshold = 11;
    private static final double initialDriveSpeed = 0.4;
    private static final double climbSpeed = 0.4;
    private static final double kMinDriveSpeed = 0.2;
    private static final double kClimbPorportionalCoefficient = 0.07;
    private static final double endClimbThreshold = 10;
    private static final double levelDeadzone = 1;

    private double maxDegreesUpX = 0.0;

    public AutoLevel(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        state = AutoLevelState.LEVEL_GROUND;
        maxDegreesUpX = 0;
        SmartDashboard.putString("AutoLevelState", state.toString());
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // drivetrain.driveDualJoystickPercent(initialDriveSpeed, initialDriveSpeed);
        // System.out.println(initialDriveSpeed);

        double currentAngleX = -drivetrain.getGyroX();
        CombinedLogging.putNumber("CurrentAngleX", currentAngleX);

        switch (state) {
            case LEVEL_GROUND:
                if (currentAngleX > initialClimbThreshold) {
                    state = AutoLevelState.CLIMB;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveTankPercent(0, 0);
                } else {
                    drivetrain.driveTankPercent(initialDriveSpeed, initialDriveSpeed);
                }
                break;
            case CLIMB:
                CombinedLogging.putNumber("currentangley + endckibthreshold",
                        Math.abs(currentAngleX) + endClimbThreshold);
                CombinedLogging.putNumber("MaxDegreesUpX", maxDegreesUpX);
                CombinedLogging.putBoolean("threshold", Math.abs(currentAngleX) + endClimbThreshold < maxDegreesUpX);
                
                double curSpeed = 
                    climbSpeed 
                    * kClimbPorportionalCoefficient 
                    * currentAngleX;

                curSpeed 
                    = Math.abs(curSpeed) > 1
                    ? 1 
                    : curSpeed;

                curSpeed 
                    = Math.abs(curSpeed) < kMinDriveSpeed
                    ? kMinDriveSpeed * Math.signum(curSpeed)
                    : curSpeed;

                if (Math.abs(currentAngleX) < levelDeadzone) {
                    curSpeed = 0;
                }

                drivetrain.driveTankPercent(
                    curSpeed,
                    curSpeed
                );
                
                break;
            }
        if (maxDegreesUpX < currentAngleX) {
            maxDegreesUpX = currentAngleX;
        }


    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.driveTankPercent(0, 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // return state == AutoLevelState.END;
        return false;
    }
}