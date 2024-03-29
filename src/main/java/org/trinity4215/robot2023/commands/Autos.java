// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;

public final class Autos {

    public static Command mobility(Drivetrain drive) {
        return new RepeatCommand(
                new InstantCommand(
                        () -> drive.driveArcadePercent(0.5, 0),
                        drive))
                .withTimeout(2).andThen(
                        new InstantCommand(
                                () -> drive.stop(),
                                drive));
    }

    // public static Command chirpyBit(Drivetrain drive) {
    // return new RepeatCommand(
    // new InstantCommand(
    // () -> drive.driveArcadePercent(-1, 0),
    // drive))
    // .withTimeout(0.1).andThen(
    // new InstantCommand(
    // () -> drive.stop(),
    // drive));
    // }

    public static Command balance(Drivetrain drive) {
        return new AutoLevel(drive);
    }

    public static Command mobilityBackAndBalance(Drivetrain drive) {
        return new RepeatCommand(
                new InstantCommand(
                        () -> drive.driveArcadePercent(-0.75, 0),
                        drive))
                .withTimeout(1.7).andThen(
                        new InstantCommand(
                                () -> drive.stop(),
                                drive),
                        new AutoLevel(drive));
    }

    public static Command testPositionalIntakeRaise(Intake intake) {
        return new PositionalIntakeTest(intake);
    }

    private Autos() {
        throw new UnsupportedOperationException(this.toString() + " is a utility class!");
    }
}
