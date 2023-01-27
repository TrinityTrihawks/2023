package org.trinity4215.fenris.subsystems;

import org.trinity4215.fenris.Constants.BeamState;
import org.trinity4215.fenris.Constants.Color;

import edu.wpi.first.wpilibj2.command.Subsystem;

public interface IntakeBits {
    public void setIntakeVoltage(double percentOutput);

    public void setMiddleVoltage(double percentOutput);

    public void setArmVoltage(double percentOutput);

    public BeamState getLowBeamState();

    public BeamState getHighBeamState();

    public Color getDetectedColor();

    public Subsystem getAsSubsystem();
    
}
