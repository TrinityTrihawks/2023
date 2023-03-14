// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023;

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
        public static final int kGollumDrivePort = 2;
        public static final int kSamwiseLeftStickPort = 1;
        public static final int kFrodoRightStickPort = 0;

        public static final double kSlowWheelSpeedPercent = 0.4;

    }

    public static class DriveConstants {


        public static enum AutoLevelState {
            LEVEL_GROUND,
            CLIMB
        }

        public static final double kPerMinuteToPerSecond = 1; // TODO
        public static final double kGearboxRatio = 1 / 12.75;
        public static final double kWheelDiameterInches = 6;
        public static final double kInchesPerMeter = 2.54 / 100;
        public static final double kMotorRPMToMetersPerMinute = (Math.PI * kWheelDiameterInches) * kPerMinuteToPerSecond * kGearboxRatio * kInchesPerMeter;     

        public static final int kPigeonId = 0;
        public static final int kEncoderCPR = 0;
        public static final double kSlewValue = 2.5;

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

        public static final int kLeftLeaderId = 12;
        public static final int kLeftFollowerId = 14;
        public static final int kRightLeaderId = 11;
        public static final int kRightFollowerId = 13;
        public static final double kBasicallyStoppedSpeed = 0.1;

        public static final double kDriveFeedbackCoefficient = 0.1;
    }

    public static class IntakeConstants {

        public static final int kRaiseEncCountsPerRev = 8192;

    }

    public static class IntakeConstants {
         public static final double kIntakeSpitSpeed = 0.5;
         public static final double kIntakeSuckSpeed = 0.5;
         public static final double kInnerDeadzoneArea = 1; //deg
         public static final double kOuterDeadzoneArea = 10; //deg
         public static final int kRaiseSparkID = 18;
         public static final int kRunSparkID = 17;
         public static final int kRevEncoderCountsPerRevolution = 8192;
         public static final int kPositionConversionFactor =180; // "pi-radians" to degrees;
    }
}
