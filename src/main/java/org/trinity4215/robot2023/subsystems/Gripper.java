// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.GripperConstants;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
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
    private boolean hasPneumatics = true;

    public Gripper() {
        try {
            squeeze = new DoubleSolenoid(PneumaticsModuleType.REVPH,
                GripperConstants.kSqeezeSolenoidPort0, GripperConstants.kSqeezeSolenoidPort1);
            raise = new DoubleSolenoid(PneumaticsModuleType.REVPH, GripperConstants.kRaiseSolenoidPort0,
                GripperConstants.kRaiseSolenoidPort1);
        } catch(Exception e) {
            squeeze = null;
            raise = null;
            hasPneumatics = false;
            CombinedLogging.putString(
                "Error",
                "no PCM: no pneumatics will work. AS A MATTER OF FACT, IT WOULD HAVE CRASHED WHEN YOU PRESS THE PNEUMATICS BUTTONS IF I HADN'T FIXED IT SO DON'T DO IT I KNOW YOU WANT TO BUT DON'T IM TALKING TO YOU NERD DON'T DO IT YOU IDIOT -- The Code Genii"
            );
        }
    }

    public static Gripper getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new Gripper();
        }

        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    public void raise() {
        if (!hasPneumatics) {
            return; // save (us from) the nullrefs!
        }
        raise.set(Value.kForward);
    }
    public void lower() {
        if (!hasPneumatics) {
            return;
        }
        raise.set(Value.kReverse);
    }
    public void raise_off() {
        if (!hasPneumatics) {
            return;
        }
        raise.set(Value.kOff);
        squeeze.set(Value.kOff);
    }

    public void grab() {
        if (!hasPneumatics) {
            return;
        }
        squeeze.set(Value.kForward);
    }
    public void drop() {
        if (!hasPneumatics) {
            return;
        }
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
