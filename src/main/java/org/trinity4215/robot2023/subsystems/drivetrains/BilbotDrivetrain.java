// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems.drivetrains;

import org.trinity4215.robot2023.Constants.Bilbot;
import org.trinity4215.robot2023.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class BilbotDrivetrain extends Drivetrain {
    private static BilbotDrivetrain subsystemInst = null;
    private DrivetrainConstants constants = null;
    


    // TODO: Figure out robot pose and how it integrates with vision tracking -- especially PhotonVision
    private Pose2d robotPose = null;


    // Initialize Spark Max's
    private final TalonSRX leftLeader = new TalonSRX(DriveConstants.TALONSRX.kLeftLeaderId);
    private final TalonSRX leftFollower = new TalonSRX(DriveConstants.TALONSRX.kLeftFollowerId);
    private final TalonSRX rightLeader = new TalonSRX(DriveConstants.TALONSRX.kRightLeaderId);
    private final TalonSRX rightFollower = new TalonSRX(DriveConstants.TALONSRX.kRightFollowerId);

    // TODO: we have talon breakouts for the encs
    // private final Encoder leftEncoder = 
    //     new Encoder(DriveConstants.kLeftEncoderChannelA, DriveConstants.kLeftEncoderChannelB);
    // private final Encoder rightEncoder = 
    //     new Encoder(DriveConstants.kRightEncoderChannelA, DriveConstants.kRightEncoderChannelB);
    
    
    
    // Initialize slew rate limiters
    private SlewRateLimiter rightLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter leftLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);


    public BilbotDrivetrain() {

        // ctre setup
        leftLeader.configFactoryDefault();
        leftFollower.configFactoryDefault();
        rightLeader.configFactoryDefault();
        rightFollower.configFactoryDefault();

        leftFollower.follow(leftLeader);
        rightFollower.follow(rightLeader);

        leftLeader.setInverted(DriveConstants.kLeftMotorsInverted);
        leftFollower.setInverted(InvertType.FollowMaster);

        rightLeader.setInverted(DriveConstants.kRightMotorsInverted);
        rightFollower.setInverted(InvertType.FollowMaster);

    }

    public static Drivetrain getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new BilbotDrivetrain();
        }

        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    @Override
    public void driveTank(double left, double right) {

        leftLeader.set(TalonSRXControlMode.PercentOutput, leftLimiter.calculate(left) * DriveConstants.kMaxSpeedPercent);
        rightLeader.set(TalonSRXControlMode.PercentOutput, rightLimiter.calculate(right) * DriveConstants.kMaxSpeedPercent);


    }


    @Override
    public DrivetrainConstants getConstants() {
        if (constants == null) {
            constants = new DriveConstants();
        }

        return constants; 
    }


    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        var gyro_angle = new Rotation2d(getGyroAngle());

        // Update the robot pose periodically with encoder values and gyro angle
        // TODO: This line requires you to set encoder.setPositionConversionFactor to a value that will cause the encoder to return its position in meters (if using spark encoders)
        // More information:
        // https://github.com/wpilibsuite/allwpilib/tree/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/differentialdrivebot
        // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/differential-drive-odometry.html
        // robotPose = odometry.update(gyro_angle, leftEncoder.getDistance(), rightEncoder.getDistance());
    }



    public static class DriveConstants implements DrivetrainConstants {
        
        // TODO: Make these constants the actual CAN IDs of the components
    
        public static enum MotorTypeInstalled {
            REV_SPARK_MAX,
            CTRE_TALON_SRX
        }
    
        public class TALONSRX {
            public static final int kLeftLeaderId = 0; // TODO: set motor ids
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
    
    
        // TODO: Tune these
        public static final int kEncoderCPR = 0;
        public static final double kSlewValue = 0.2;
    
        public static final boolean kSquareJoystickValues = true;
        public static final boolean kLeftMotorsInverted = false;
        public static final boolean kRightMotorsInverted = true;
    
        // TODO: set enc channels -- is this necessary for talon breakouts?
        public static final int kLeftEncoderChannelA = 0;
    
        public static final int kLeftEncoderChannelB = 0;
    
        public static final int kRightEncoderChannelA = 0;
    
        public static final int kRightEncoderChannelB = 0;
    
        public static final double kMaxSpeedPercent = 0.4;
    
        public static final double kMinTurnSpeed = 0.2;
    
        public static final double kDeadzone = 0.015;
    
        public static enum DriveType {
            SINGLE,
            DUAL
        }

        public final DriveType kDriveType = DriveType.DUAL;
    
    }
}