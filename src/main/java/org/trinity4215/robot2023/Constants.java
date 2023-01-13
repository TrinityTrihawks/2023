// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023;

import com.ctre.phoenix.motorcontrol.InvertType;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class OperatorConstants {
        public static final int kDriverControllerPort = 0;
    }

    public static class DriveConstants {

        // TODO: Make these constants the actual CAN IDs of the components

        public static enum MotorTypeInstalled {
            REV_SPARK_MAX,
            CTRE_TALON_SRX
        }

        public class TALONSRX {
            public static final int kLeftLeaderId = 0;
            public static final int kLeftFollowerId = 0;
            public static final int kRightLeaderId = 0;
            public static final int kRightFollowerId = 0;
        }
        public class SPARKMAX {
            public static final int kLeftLeaderId = 0;
            public static final int kLeftFollowerId = 0;
            public static final int kRightLeaderId = 0;
            public static final int kRightFollowerId = 0;
        }

        public static final int kPigeonId = 0;

        // TODO: Tune these
        public static final int kEncoderCPR = 0;
        public static final double kSlewValue = 0;

        public static final boolean kSquareJoystickValues = true;
        public static final boolean kLeftMotorsInverted = true;
        public static final boolean kRightMotorsInverted = true;

        public static enum DriveType {
            SINGLE,
            DUAL
        }
    }
}
