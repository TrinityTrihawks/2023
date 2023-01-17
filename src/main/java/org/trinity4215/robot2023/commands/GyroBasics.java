// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.subsystems.RobotGyro;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class GyroBasics extends CommandBase {
    private final RobotGyro robotgyro;

    /** Creates a new GyroBasics. */
    public GyroBasics(RobotGyro robotgyro) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.robotgyro = robotgyro;
        addRequirements(robotgyro);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        double a = robotgyro.getAngleX();
        System.out.println(a);

        if (10 < a && a < 170) {

            double x = Math.abs(
                    Math.abs((a - 10) / 80 - 1)
                            - 1); // todo: someone who knows what this math does document it
            System.out.println(x);
            robotgyro.drive(x);

        } else if (190 < a && a < 350) {

            double x = Math.abs((a - 190) / 80 - 1) - 1; // todo: someone who knows what this math does document it
            System.out.println(x);
            robotgyro.drive(x);

        } else {
            robotgyro.drive(0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        robotgyro.drive(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
