// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.commands;

import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {

    public static Command mobility(Drivetrain drive) {
        return new RepeatCommand(
                new InstantCommand(
                        () -> drive.driveArcadePercent(-0.5, 0),
                        drive))
                .withTimeout(2.5).andThen(
                        new InstantCommand(
                                () -> drive.stop(),
                                drive));
    }

    public static Command cubeAndMobility(Drivetrain drive, Intake intake) {

        return cube(intake).andThen(mobility(drive));
    }

    public static Command cube(Intake intake) {
        return new RepeatCommand(
                new InstantCommand(
                        () -> intake.spit(null),
                        intake))
                .withTimeout(0.5).andThen(
                        new InstantCommand(
                                intake::stopSpinMotor,
                                intake));
    }

    public static Command balance(Drivetrain drive) {
        return new AutoLevel(drive);
    }

    public static Command mobilityBackAndBalance(Drivetrain drive) {
        return new RepeatCommand(
                new InstantCommand(
                        () -> drive.driveArcadePercent(-0.5, 0),
                        drive))
                .withTimeout(3.8).andThen(
                        new InstantCommand(
                                () -> drive.stop(),
                                drive),

                        new WaitCommand(0.3),
                        new AutoLevel(drive));
    }

    public static Command cubeMobilityAndBalance(Drivetrain drive, Intake intake) {
        return new SequentialCommandGroup(

                cube(intake),

                mobilityBackAndBalance(drive));
    }

    public static Command testPositionalIntakeRaise(Intake intake) {
        return new PositionalIntakeTest(intake);
    }

    private Autos() {
        throw new UnsupportedOperationException(this.toString() + " is a utility class!");
    }
}
