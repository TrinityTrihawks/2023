// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.Constants.GripperConstants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gripper extends SubsystemBase {
    private static Gripper subsystemInst = null;

    private DoubleSolenoid squeeze = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
            GripperConstants.kSqeezeSolenoidPort0, GripperConstants.kSqeezeSolenoidPort1);
    private DoubleSolenoid raise = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, GripperConstants.kRaiseSolenoidPort0, GripperConstants.kRaiseSolenoidPort1);
    // private Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

    private boolean isGrabbing = false;
    private boolean isUp = false;

    public Gripper() {
        // compressor.enableDigital();
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
    public void raise_off() {
        raise.set(Value.kOff);
        squeeze.set(Value.kOff);
    }

    public void grab() {
        squeeze.set(Value.kForward);
    }
    public void drop() {
        squeeze.set(Value.kReverse);
    }
    public void close() {
        squeeze.set(Value.kForward);
    }
    public void open() {
        squeeze.set(Value.kReverse);
    }
    public void grip_off() {
        squeeze.set(Value.kOff);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void toggleUp() {
        if (isUp) {
            lower();
        } else {
            raise();
        }
        isUp = !isUp;
    }

    public void toggleGrab() {
        if (isGrabbing) {
            open();
        } else {
            close();
        }
        isGrabbing = !isGrabbing;
    }
}
