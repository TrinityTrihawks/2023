// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.fenris;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;

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

    public static final double kOptimalVoltage = 12.8;
    
    /**
     * Drive Constants
     */
    public static final class DriveConstants {
        // Spark IDs
        public static final int kFrontLeftMotorId = 11;
        public static final int kFrontRightMotorId = 12;
        public static final int kBackLeftMotorId = 13;
        public static final int kBackRightMotorId = 14;

        public static final int kPigeonId = 15;

        // https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/mecanumcontrollercommand/Constants.java

        // Encoder IDs
        public static final int[] kFrontLeftEncoderIds = new int[] { 0, 1 };
        public static final int[] kBackLeftEncoderIds = new int[] { 2, 3 };
        public static final int[] kFrontRightEncoderIds = new int[] { 4, 5 };
        public static final int[] kBackRightEncoderIds = new int[] { 6, 7 };

        // Encoder direction
        public static final boolean kFrontLeftEncoderReversed = false;
        public static final boolean kBackLeftEncoderReversed = true;
        public static final boolean kFrontRightEncoderReversed = false;
        public static final boolean kBackRightEncoderReversed = true;

        // Distance between centers of right and left wheels on robot
        public static final double kTrackWidth = 0.56; // 22in
        // Distance between centers of front and back wheels on robot
        public static final double kWheelBase = 0.5; // 20in

        public static final MecanumDriveKinematics kDriveKinematics = new MecanumDriveKinematics(
                new Translation2d(kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
                new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

        public static final double kGearRatio = 10.71; // 10.1:1
        public static final int kEncoderCPR = 42; // counts per revolution
        public static final double kWheelDiameterMeters = 0.15; // 6in
        public static final double kMetersPerMotorRotation = (kWheelDiameterMeters * Math.PI) / kGearRatio;

        public static final double kSlewValue = 1.5;

        public static final double kGyroResetWaitTime = 4;
        public static final double kZStaticSlewScalar = 0.8;

        public static final double kFeetToMetersConversionFactor = 0.3048;
    }

    /**
     * Joystick Constants
     */
    public static final class JoystickConstants {

        public static final int kRightJoystickPort = 0;
        public static final int kLeftJoystickPort = 1;
        public static final int kXboxControllerPort = 2;

        public static final double kXDeadZone = 0.1; // Left-Right
        public static final double kYDeadZone = 0.1; // Front-Back
        public static final double kTwistDeadZone = 0.1; // Twist

        public static final double kStaticThrottleScalar = 1; // multiple inputs values by this

        public static final int kZeroButtonId = 7;
        public static final int kSwitchDriveModeButtonId = 9;
    }

    public static final class ShootyBitsConstants {
        public static final int kIntakeMotorPort = 17;
        public static final int kMiddleMotorPort = 16;
        public static final int kShooterMotorPort = 18;

        public static final int kLowBeamPort = 5;
        public static final int kHighBeamPort = 4;

        public static final double kShooterRunSpeed = -0.5;
        public static final double kShooterSlowSpeed = 0.1;
		public static final double kShooterOverheadSpeed = 0.8;
        public static final double kMiddleRunSpeed = -0.4;
        public static final double kIntakeRunSpeed = -1;
        public static final Color kTargetColor = null;
        public static final Color kRejectColor = null;
        public static final int kArmMotorPort = 19;
        public static final int kLimitPort = 2;
        public static final double kArmDownSpeed = 0.3;
        public static final double kArmDownTime = 0.6;
        public static final double kArmUpSpeed = 0.5;
        public static final double kArmUpTime = 1.5;
    }

    /**
     * Network Constants (IP addreses and related info)
     */
    public static final class NetworkConstants {
        public static final String kFrontCameraIP = "10.42.15.00";
    }

    public enum BeamState {
        OPEN(true),
        CLOSED(false);

        public final boolean state;

        BeamState(boolean state) {
            this.state = state;
        }

        public BeamState negate() {
            return state == OPEN.state ? CLOSED : OPEN;
        }

        public String toString() {
            return state == OPEN.state? "OPEN" : "CLOSED";
        }

        public static BeamState fromBoolean(boolean bool) {
            return bool == OPEN.state ? OPEN : CLOSED;
        }
    }

    public enum Color {
        RED,
        BLUE,
        NONE;

        private static final edu.wpi.first.wpilibj.util.Color red = new edu.wpi.first.wpilibj.util.Color(255, 0, 0);//TODO set colors
        private static final edu.wpi.first.wpilibj.util.Color blue = new edu.wpi.first.wpilibj.util.Color(0, 0, 255);

        public static edu.wpi.first.wpilibj.util.Color toWpiColor(Color c) {
            if (c == RED) {
                return red;
            } else if (c == BLUE) {
                return blue;
            } else {
                throw new IllegalArgumentException("cannot convert NONE to wpi Color");
            }
        }
    }
    public interface GamepadInterface {
        public int a();
        public int b();
        public int x();
        public int y();
        public int lb();
        public int rb();
        public int lt();
        public int rt();
        public int select();
        public int start();
    }
    public static class XboxPortProvider implements GamepadInterface {
        public int a()      { return 1; }
        public int b()      { return 2; }
        public int x()      { return 3; }
        public int y()      { return 4; }
        public int lb()     { return 5; }
        public int rb()     { return 6; }
        public int lt()     { return 2; }
        public int rt()     { return 3; }
        public int select() { return 9; }
        public int start()  { return 10; }
    }
}