// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;


import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.DriveConstants.AutoLevelState;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoLevel extends CommandBase {
    /** Creates a new AutoLevel. */
    private Drivetrain drivetrain;
    private AutoLevelState state;
    private final double initialClimbThreshold = 18;
    private final double initialDriveSpeed = 0.7;
    private final double climbSpeed = 0.7;
    private final double endClimbThreshold = 10;
    private final double fallForwardThreshold = 0.0;
    private final double kickbackThreshold = 0.0;
    private final double kickbackSpeed = 0.0;
    private final double levelDeadzone = 1;
    private final double pauseTime = 1.0;
    private final boolean endCondition = false;
    private double maxDegreesUpY = 0.0;

    public AutoLevel(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        state = AutoLevelState.LEVEL_GROUND;
        SmartDashboard.putString("AutoLevelState", state.toString());

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // drivetrain.driveDualJoystickPercent(initialDriveSpeed, initialDriveSpeed);
        // System.out.println(initialDriveSpeed);

        double currentAngleY = drivetrain.getGyroY();
        CombinedLogging.putNumber("CurrentAngley", currentAngleY);

        switch (state) {
            case LEVEL_GROUND:
                if (currentAngleY > initialClimbThreshold) {
                    state = AutoLevelState.INITIAL_CLIMB;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveTankPercent(0, 0);
                } else {
                    drivetrain.driveTankPercent(initialDriveSpeed, initialDriveSpeed);
                }
                break;
            case INITIAL_CLIMB:
                CombinedLogging.putNumber("currentangley + endckibthreshold",
                        Math.abs(currentAngleY) + endClimbThreshold);
                CombinedLogging.putNumber("MaxDegreesUpY", maxDegreesUpY);
                CombinedLogging.putBoolean("threshold", Math.abs(currentAngleY) + endClimbThreshold < maxDegreesUpY);
                if (Math.abs(currentAngleY) + endClimbThreshold < maxDegreesUpY) {
                    state = AutoLevelState.FALL_FORWARD;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveTankPercent(0, 0);
                } else {
                    drivetrain.driveTankPercent(climbSpeed, climbSpeed);
                }
                break;
            case FALL_FORWARD:
                Timer.delay(pauseTime);
                state = AutoLevelState.KICKBACK;
                CombinedLogging.putString("AutoLevelState", state.toString());
                drivetrain.driveTankPercent(0, 0);
                break;
            case KICKBACK:
                if (endCondition) {
                    if (Math.abs(currentAngleY) - levelDeadzone > 0) {
                        state = AutoLevelState.OVERCORRECTED;
                    } else {
                        state = AutoLevelState.END;
                    }
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveTankPercent(0, 0);
                } else {
                    if (currentAngleY > 0) {
                        drivetrain.driveTankPercent(kickbackSpeed, kickbackSpeed);
                    } else {

                    }

                }
                break;
            case OVERCORRECTED:
                if (endCondition) {
                    state = AutoLevelState.FINAL_ADJUST;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveTankPercent(0, 0);
                } else {

                }
                break;
            case FINAL_ADJUST:
                if (endCondition) {
                    state = AutoLevelState.END;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveTankPercent(0, 0);
                } else {

                }
                break;
            case END:
                drivetrain.driveTankPercent(0, 0);
                break;
        }
        if (maxDegreesUpY < currentAngleY) {
            maxDegreesUpY = currentAngleY;
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