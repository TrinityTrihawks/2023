// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023;

import com.ctre.phoenix.motorcontrol.InvertType;

import edu.wpi.first.wpilibj.DigitalSource;

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
        public static final int kXboxPort = 2;
        public static final int kLeftStickPort = 1;
        public static final int kRightStickPort = 0;
    }

    public static class DriveConstants {

        // TODO: Make these constants the actual CAN IDs of the components

        public static enum MotorTypeInstalled {
            REV_SPARK_MAX,
            CTRE_TALON_SRX
        }

        public static enum AutoLevelState {
            LEVEL_GROUND,
            INITIAL_CLIMB,
            FALL_FORWARD,
            KICKBACK,
            OVERCORRECTED,
            FINAL_ADJUST,
            END
        }

        public class TALONSRX {
            public static final int kLeftLeaderId = 12;
            public static final int kLeftFollowerId = 14;
            public static final int kRightLeaderId = 11;
            public static final int kRightFollowerId = 13;
        }
        public class SPARKMAX {
            public static final int kLeftLeaderId = 12;
            public static final int kLeftFollowerId = 14;
            public static final int kRightLeaderId = 11;
            public static final int kRightFollowerId = 13;
        }

        public static final int kPigeonId = 0;

        // TODO: Tune these
        public static final int kEncoderCPR = 0;
        public static final double kSlewValue = 0.1;

        

        public static final boolean kSquareJoystickValues = true;
        public static final boolean kLeftMotorsInverted = false;
        public static final boolean kRightMotorsInverted = true;

        public static final int kLeftEncoderChannelA = 0;

        public static final int kLeftEncoderChannelB = 0;

        public static final int kRightEncoderChannelA = 0;

        public static final int kRightEncoderChannelB = 0;

        public static final double kMaxSpeedPercent = 0.3;

        public static final double kMinTurnSpeed = 0.2;

        public static final double kOneMeterArea = 2.24;
        public static final double kAreaDeadzone = 0;

        public static final double kPositionConversionFactor = 0;

        public static enum DriveType {
            SINGLE,
            DUAL
        }

        public static double kDeadzone;
    }

    public static class GripperConstants {
        public static final int kSqeezeSolenoidPort0 = 0;
        public static final int kSqeezeSolenoidPort1 = 0;
        public static final int kRaiseSolenoidPort0 = 0;
        public static final int kRaiseSolenoidPort1 = 0;
    }
}
