// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins.subsystems.drivetrains;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;


public class BilbotDrivetrain extends Drivetrain {
    private static BilbotDrivetrain subsystemInst = null;
    private static DrivetrainConstants constants = null;


    // TODO: Figure out robot pose and how it integrates with vision tracking --
    // especially PhotonVision
    private Pose2d robotPose = null;

    private final CANSparkMax leftLeader = new CANSparkMax(DriveConstants.kLeftLeaderId, 
            MotorType.kBrushless);
    private final CANSparkMax leftFollower = new CANSparkMax(DriveConstants.kLeftFollowerId,
            MotorType.kBrushless);
    private final CANSparkMax rightLeader = new CANSparkMax(DriveConstants.kRightLeaderId,
            MotorType.kBrushless);
    private final CANSparkMax rightFollower = new CANSparkMax(DriveConstants.kRightFollowerId,
            MotorType.kBrushless);

    private final MotorControllerGroup leftMotorControllerGroup = new MotorControllerGroup(leftLeader, leftFollower);
    private final MotorControllerGroup rightMotorControllerGroup = new MotorControllerGroup(rightLeader, rightFollower);

    // Initialize Spark Max encoders
    private final RelativeEncoder leftEncoder = leftLeader.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor,
            DriveConstants.kEncoderCPR); // IF REV
    private final RelativeEncoder rightEncoder = rightLeader.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor,
            DriveConstants.kEncoderCPR);
    
    
    // Initialize slew rate limiters
    private SlewRateLimiter rightLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter leftLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);


    private final DifferentialDrive drive = new DifferentialDrive(leftMotorControllerGroup, rightMotorControllerGroup);

    // TODO: This line requires you to set encoder.setPositionConversionFactor to a
    // value that will cause the encoder to return its position in meters
    private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d(getGyroZ()),
            leftEncoder.getPosition(), rightEncoder.getPosition());


    public BilbotDrivetrain() {

        leftEncoder.setPositionConversionFactor(DriveConstants.kPositionConversionFactor);
        rightEncoder.setPositionConversionFactor(DriveConstants.kPositionConversionFactor);

        leftLeader.setInverted(DriveConstants.kLeftMotorsInverted);
        leftFollower.setInverted(DriveConstants.kLeftMotorsInverted);

        rightLeader.setInverted(DriveConstants.kRightMotorsInverted);
        rightFollower.setInverted(DriveConstants.kRightMotorsInverted);

    }

    public static Drivetrain getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new BilbotDrivetrain();
        }

        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    @Override
    public void driveTank(double left, double right) {

        setConstants();

        drive.tankDrive( 
            leftLimiter.calculate(left),
            rightLimiter.calculate(right)
        );
    }


    @Override
    public DrivetrainConstants getConstants() {
        setConstants();
        return constants; 
    }

    @Override
    public void setConstants() {
        if (constants == null) {
            constants = new DriveConstants();
        }

    }


    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        var gyro_angle = new Rotation2d(getGyroZ());

        // Update the robot pose periodically with encoder values and gyro angle
        // TODO: This line requires you to set encoder.setPositionConversionFactor to a
        // value that will cause the encoder to return its position in meters (if using
        // spark encoders)
        // More information:
        // https://github.com/wpilibsuite/allwpilib/tree/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/differentialdrivebot
        // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/differential-drive-odometry.html
        robotPose = odometry.update(gyro_angle, leftEncoder.getPosition(),
                rightEncoder.getPosition());
    }

    private static final class DriveConstants implements DrivetrainConstants {    
    
        // TODO: Tune this
        public static final int kEncoderCPR = 0;
        public static final double kSlewValue = 3;
    
        public static final boolean kLeftMotorsInverted = false;
        public static final boolean kRightMotorsInverted = true;
    
        
        public static final double kMaxSpeedPercent = 0.2;
    
        public static final double kMinTurnSpeed = 0.2;


        public static final int kRightFollowerId = 13;
        public static final int kRightLeaderId = 11;
        public static final int kLeftFollowerId = 14;
        public static final int kLeftLeaderId = 12;
    

        public static final double kPositionConversionFactor = 0;


        @Override
        public double kMaxSpeedPercent() {
            return kMaxSpeedPercent;
        }

        @Override
        public double kMinTurnSpeed() {
            return kMinTurnSpeed;
        }

        @Override
        public double kSlewValue() {
            return kSlewValue;
        }

    
    }

}