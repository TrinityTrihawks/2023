// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.Constants.DriveConstants.AutoLevelState;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoLevel extends CommandBase {
    /** Creates a new AutoLevel. */
    private Drivetrain drivetrain;
    private AutoLevelState state;
    private double initialDriveSpeed = 0.4;
    private double initialClimbThreshold = 18;
    private double climbSpeed = 0.3;
    private double endClimbThreshold = 10;
    private double fallForwardThreshold = 0.0;
    private double kickbackThreshold = 0.0;
    private double kickbackSpeed = 0.0;
    private double pauseTime = 0.0;
    private boolean endCondition = false;
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
        switch (state) {
            case LEVEL_GROUND:
                System.out.println(drivetrain.getGyroY());
                System.out.println(drivetrain.getGyroY() > initialClimbThreshold);
                if (drivetrain.getGyroY() > initialClimbThreshold) {
                    state = AutoLevelState.INITIAL_CLIMB;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                    System.out.println("-----------");
                } else {
                    drivetrain.driveDualJoystickPercent(initialDriveSpeed, initialDriveSpeed);
                }
                break;
            case INITIAL_CLIMB:
                System.out.println(drivetrain.getGyroY() + endClimbThreshold);
                System.out.println(maxDegreesUpY);
                System.out.println(drivetrain.getGyroY() + endClimbThreshold < maxDegreesUpY);
                if (drivetrain.getGyroY() + endClimbThreshold < maxDegreesUpY) {
                    state = AutoLevelState.FALL_FORWARD;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {
                    drivetrain.driveDualJoystickPercent(climbSpeed, climbSpeed);
                }
                break;
            case FALL_FORWARD:
                if (endCondition) {
                    state = AutoLevelState.KICKBACK;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {

                }
                break;
            case KICKBACK:
                if (endCondition) {
                    state = AutoLevelState.PAUSE;
                    SmartDashboard.putString("AutoLevelState", state.toString());
                    drivetrain.driveDualJoystickPercent(0, 0);
                } else {

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
        if (maxDegreesUpY < drivetrain.getGyroY()) {
            maxDegreesUpY = drivetrain.getGyroY();
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
        //return state == AutoLevelState.END;
        return false;
    }
}
