// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimelightPhotonVision extends SubsystemBase {
    private static LimelightPhotonVision subsystemInst = null;

    PhotonCamera photonCamera = new PhotonCamera("visioncam");

    /** Creates a new LimelightPhotonVision. */
    public static LimelightPhotonVision getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new LimelightPhotonVision();
        } 
        return subsystemInst;
    }
    public LimelightPhotonVision() {
        
    }
    
    public PhotonPipelineResult getLatestResult() {
        return photonCamera.getLatestResult();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
