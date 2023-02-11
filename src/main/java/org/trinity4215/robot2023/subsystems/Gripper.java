// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.Constants;
import org.trinity4215.robot2023.Constants.GripperConstants;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gripper extends SubsystemBase {

    private DoubleSolenoid squeeze = new DoubleSolenoid(PneumaticsModuleType.REVPH, GripperConstants.kSqeezeSolenoidPort0, GripperConstants.kSqeezeSolenoidPort1);
    private DoubleSolenoid raise = new DoubleSolenoid(PneumaticsModuleType.REVPH, GripperConstants.kRaiseSolenoidPort0, GripperConstants.kRaiseSolenoidPort1);

    /** Creates a new Gripper. */
    public Gripper() {
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
