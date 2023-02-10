// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import org.trinity4215.bilbotbaggins.subsystems.drivetrains.BilbotDrivetrain;
import org.trinity4215.bilbotbaggins.subsystems.drivetrains.FenrisDrivetrain;

import edu.wpi.first.wpilibj.RuntimeType;

/**
 * This class handles switching `Drivetrain`s,
 * etc. that are dependent on which robot the code is 
 * deployed to.
 */
public class BotSwitcher {

    private static final RobotType type = RobotType.kBilbotBaggins;

    public enum RobotType { 
        kFenris,
        kBilbotBaggins;

        public RuntimeType toRuntimeType() {
            switch (this) {
                case kFenris:
                    return RuntimeType.kRoboRIO;
                case kBilbotBaggins:
                    return RuntimeType.kRoboRIO2;
                default:
                    return null;
            }
        }
    }

    public static void assertCorrectRobot() {
        if (Robot.getRuntimeType() != type.toRuntimeType()) {
           throw new IllegalStateException("Cannot run one robot's code on the other!!");
        }
    }

    public static Drivetrain getDrivetrain() {
        switch (type) {
            case kFenris:
                return FenrisDrivetrain.getInstance();
            
            case kBilbotBaggins:
                return BilbotDrivetrain.getInstance();
        
            default:
                return null;
        }
    }
    
}
