// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.Constants;
import org.trinity4215.robot2023.Constants.GripperConstants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gripper extends SubsystemBase {
    private static Gripper subsystemInst = null;

    private DoubleSolenoid squeeze = new DoubleSolenoid(PneumaticsModuleType.REVPH,
            GripperConstants.kSqeezeSolenoidPort0, GripperConstants.kSqeezeSolenoidPort1);
    private DoubleSolenoid raise = new DoubleSolenoid(PneumaticsModuleType.REVPH, GripperConstants.kRaiseSolenoidPort0,
            GripperConstants.kRaiseSolenoidPort1);
    private Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);

    /** Creates a new Gripper. */
    public Gripper() {
        compressor.enableDigital();
    }

    public static Gripper getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new Gripper();
        }

        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    public void raise() {
        raise.set(Value.kForward);
    }
    public void lower() {
        raise.set(Value.kReverse);
    }
    public void off() {
        raise.set(Value.kOff);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
