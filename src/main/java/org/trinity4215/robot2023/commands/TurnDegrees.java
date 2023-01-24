// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.Constants.Bilbot;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurnDegrees extends CommandBase {

    private final Drivetrain drive;
    private final double target;

    private double curAngle = 0;


    public TurnDegrees(double targetAngle, Drivetrain drivetrain) {
        drive = drivetrain;
        target = targetAngle;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        drive.resetGyro();
    }

    @Override
    public void execute() {
        curAngle = drive.getGyroAngle();
        SmartDashboard.putNumber("input angle", curAngle);
        double speed = Math.sin((curAngle - target) * Math.PI / 180 / 2);
        SmartDashboard.putNumber("sind(angle)", speed);
        int deadzoneScalar = Math.abs(speed) <= Bilbot.DriveConstants.kDeadzone? 0 : 1;
        SmartDashboard.putBoolean("in dead zone", deadzoneScalar == 0);
        double output = 
            -1 * speed 
            * deadzoneScalar 
            * Bilbot.DriveConstants.kMaxSpeedPercent
            * (1 - Bilbot.DriveConstants.kMinTurnSpeed) + Bilbot.DriveConstants.kMinTurnSpeed;
            
        SmartDashboard.putNumber("output", output);
        drive.driveTank(output, output);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return Math.abs(Math.sin((curAngle - target) * Math.PI / 180 / 2)) <= Bilbot.DriveConstants.kDeadzone;
    }
}
