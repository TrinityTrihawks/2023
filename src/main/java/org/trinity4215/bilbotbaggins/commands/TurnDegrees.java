// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins.commands;

import org.trinity4215.bilbotbaggins.Constants;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain.DrivetrainConstants;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurnDegrees extends CommandBase {

    private final Drivetrain drive;
    private final DrivetrainConstants constants;

    private final double target;
    private double curAngle = 0;
    private boolean isInDeadzone = false;

    private double output = 0;


    public TurnDegrees(double targetAngle, Drivetrain drivetrain) {
        drive = drivetrain;
        constants = drive.getConstants();
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
        isInDeadzone = Math.abs(curAngle - target) <= constants.kAngularDeadZone();

        if (projectedStopPointIsInDeadZone()){

            output = 0;

        } else {

            double speed = Math.sin((curAngle - target) * Math.PI / 180 / 2);
            int deadzoneScalar = isInDeadzone? 0 : 1;
            output = 
                -1 * speed 
                * deadzoneScalar 
                * constants.kMaxSpeedPercent()
                * (1 - constants.kMinTurnSpeed()) + constants.kMinTurnSpeed();

        }

        drive.driveTank(-output, output);
    }

    @Override
    public void end(boolean interrupted) {
        drive.driveTank(0, 0);
    }

    @Override
    public boolean isFinished() {
        return isInDeadzone;
    }



    private boolean projectedStopPointIsInDeadZone() {

        double timeToStop = Math.abs(output / constants.kSlewValue());
        double distToStop = timeToStop * Constants.kAllOutTurnRateDegreesPerSecond;

        return Math.abs((curAngle - target)) - distToStop <= constants.kAngularDeadZone();
    }
}
