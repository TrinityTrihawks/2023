// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins.commands;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public final class Autos {
    
    public static Command level(Drivetrain drive) {
        return new AutoLevel(drive);
    }

    public static Command doA180(Drivetrain drive) {
        return new TurnDegrees(180, drive).andThen(new WaitCommand(1));
    }

    private Autos() {
        throw new UnsupportedOperationException("This is a utility class!");
    }
}
