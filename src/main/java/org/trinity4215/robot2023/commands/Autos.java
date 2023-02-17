// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;

public final class Autos { 

    public static Command justDrive(Drivetrain drive) {
        return lowerArm(drive).andThen(
            new RepeatCommand(
                new InstantCommand(
                    () -> drive.driveArcadePercent(-1, 0),
                    drive
                )
            ).withTimeout(0.4).andThen(
                new InstantCommand(
                    () -> drive.stop(),
                    drive
                )
            )
        );
    }

    public static Command lowerArm(Drivetrain drive) {
        return new RepeatCommand(
            new InstantCommand(
                () -> drive.driveArcadePercent(-1, 0),
                drive
            )
        ).withTimeout(0.1).andThen(
            new InstantCommand(
                () -> drive.stop(),
                drive
            )
        );
    }

    public static Command balance(Drivetrain drive) {
        return lowerArm(drive).andThen(
            new AutoLevel(drive)
        );
    }

    private Autos() {
        throw new UnsupportedOperationException("This is a utility class!");
    }
}
