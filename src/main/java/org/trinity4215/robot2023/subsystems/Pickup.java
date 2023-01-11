// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pickup extends SubsystemBase {
  private static Pickup subsystemInst = null;

  public static Pickup getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Pickup();
    }

    return subsystemInst; // Ensures that only one Drivetrain instance exists at once
  }

  /** Creates a new Pickup. */
  public Pickup() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

}
