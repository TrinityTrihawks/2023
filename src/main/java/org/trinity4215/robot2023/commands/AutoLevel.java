// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import javax.swing.ComboBoxEditor;


import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.DriveConstants.AutoLevelState;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoLevel extends CommandBase {
    /** Creates a new AutoLevel. */
    private Drivetrain drivetrain;
    private AutoLevelState state;
    private final double initialClimbThreshold = 18;
    private final double initialDriveSpeed = 0.4;
    private final double climbSpeed = 0.3;
    private final double endClimbThreshold = 10;
    private final double fallForwardThreshold = 0.0;
    private final double kickbackThreshold = 0.0;
    private final double kickbackSpeed = 0.0;
    private final long pauseTime = 1;
    private final boolean endCondition = false;
    private double maxDegreesUpY = 0.0;
    private boolean timer_over = false;

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
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {
                    drivetrain.driveDualJoystickPercent(initialDriveSpeed, initialDriveSpeed);
                }
                break;
            case INITIAL_CLIMB:
                CombinedLogging.putNumber("currentangley + endckibthreshold", Math.abs(currentAngleY) + endClimbThreshold);
                CombinedLogging.putNumber("MaxDegreesUpY", maxDegreesUpY);
                CombinedLogging.putBoolean("threshold", Math.abs(currentAngleY) + endClimbThreshold < maxDegreesUpY);
                if (Math.abs(currentAngleY) + endClimbThreshold < maxDegreesUpY) {
                    state = AutoLevelState.FALL_FORWARD;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {
                    drivetrain.driveDualJoystickPercent(climbSpeed, climbSpeed);
                }
                break;
            case FALL_FORWARD:
                if (timer_over) {
                    state = AutoLevelState.KICKBACK;
                    CombinedLogging.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {
                    //wait
                }
                break;
            case KICKBACK:
                if (Math.abs(currentAngleY) < 2) {
                    state = AutoLevelState.PAUSE;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {
                    if (currentAngleY > 0) {
                        drivetrain.driveDualJoystickPercent(kickbackSpeed, kickbackSpeed);
                    } else {

                    }

                }
                break;
            case PAUSE:
                if (endCondition) {
                    state = AutoLevelState.ADJUST;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {

                }
                break;
            case ADJUST:
                if (endCondition) {
                    state = AutoLevelState.END;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {

                }
                break;
            case END:
                drivetrain.driveDualJoystickPercent(0, 0);
                break;
        }
        if (maxDegreesUpY < currentAngleY) {
            maxDegreesUpY = currentAngleY;
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.driveDualJoystickPercent(0, 0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // return state == AutoLevelState.END;
        return false;
    }
}