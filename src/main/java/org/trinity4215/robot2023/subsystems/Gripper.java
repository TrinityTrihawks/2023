// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.GripperConstants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Gripper extends SubsystemBase {

    private static Gripper subsystemInst = null;

    private DoubleSolenoid squeezeA;
    private DoubleSolenoid squeezeB;

    private DoubleSolenoid raise;
    private Compressor compressor;

    private boolean hasPneumatics = true;


    private Gripper() {
        
        try {
            squeezeA = new DoubleSolenoid(PneumaticsModuleType.REVPH,
                GripperConstants.kSqueezeSolenoidAPort0, GripperConstants.kSqeezeSolenoidAPort1);
            squeezeB = new DoubleSolenoid(PneumaticsModuleType.REVPH,
                GripperConstants.kSqueezeSolenoidBPort0, GripperConstants.kSqueezeSolenoidBPort1);
            raise = new DoubleSolenoid(PneumaticsModuleType.REVPH, GripperConstants.kRaiseSolenoidPort0,
                GripperConstants.kRaiseSolenoidPort1);
            compressor = new Compressor(PneumaticsModuleType.REVPH);
            compressor.enableDigital();
        } catch(Exception e) {
            squeezeA = null;
            squeezeB = null;
            raise = null;
            compressor = null;
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
    public void off() {
        if (!hasPneumatics) {
            return;
        }
        raise.set(Value.kOff);
        squeezeA.set(Value.kOff);
        squeezeB.set(Value.kOff);
    }

    public void grab() {
        if (!hasPneumatics) {
            return;
        }
        squeezeA.set(Value.kForward);
        squeezeB.set(Value.kForward);
    }
    public void drop() {
        if (!hasPneumatics) {
            return;
        }
        squeezeA.set(Value.kReverse);
        squeezeB.set(Value.kReverse);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
