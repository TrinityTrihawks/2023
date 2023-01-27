// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.Constants.DriveConstants;
import org.trinity4215.robot2023.Constants.OperatorConstants;
import org.trinity4215.robot2023.Constants.DriveConstants.DriveType;
import org.trinity4215.robot2023.Constants.DriveConstants.MotorTypeInstalled;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    private static Drivetrain subsystemInst = null;
    private static MotorTypeInstalled motorTypeInstalled = MotorTypeInstalled.CTRE_TALON_SRX; // Change this to switch to a spark max robot


    // TODO: Figure out robot pose and how it integrates with vision tracking -- especially PhotonVision
    private Pose2d robotPose = null;


    // Initialize Spark Max's
    private final TalonSRX leftLeader = new TalonSRX(DriveConstants.TALONSRX.kLeftLeaderId);
    private final TalonSRX leftFollower = new TalonSRX(DriveConstants.TALONSRX.kLeftFollowerId);
    private final TalonSRX rightLeader = new TalonSRX(DriveConstants.TALONSRX.kRightLeaderId);
    private final TalonSRX rightFollower = new TalonSRX(DriveConstants.TALONSRX.kRightFollowerId);

    // private final CANSparkMax leftLeader = new CANSparkMax(DriveConstants.SPARKMAX.kLeftLeaderId, MotorType.kBrushless);         // IF REV
    // private final CANSparkMax leftFollower = new CANSparkMax(DriveConstants.SPARKMAX.kLeftFollowerId, MotorType.kBrushless);     // IF REV
    // private final CANSparkMax rightLeader = new CANSparkMax(DriveConstants.SPARKMAX.kRightLeaderId, MotorType.kBrushless);       // IF REV
    // private final CANSparkMax rightFollower = new CANSparkMax(DriveConstants.SPARKMAX.kRightFollowerId, MotorType.kBrushless);   // IF REV
   
    // private final MotorControllerGroup leftMotorControllerGroup = new MotorControllerGroup(leftLeader, leftFollower);            // IF REV
    // TODO: we have talon breakouts for the encs
    // private final Encoder leftEncoder = 
    //     new Encoder(DriveConstants.kLeftEncoderChannelA, DriveConstants.kLeftEncoderChannelB);
    // private final Encoder rightEncoder = 
    //     new Encoder(DriveConstants.kRightEncoderChannelA, DriveConstants.kRightEncoderChannelB);
    
    // Initialize Spark Max encoders
    // private final RelativeEncoder leftEncoder = leftLeader.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR); // IF REV
    // private final RelativeEncoder rightEncoder = rightLeader.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR); // IF REV

    private final ADIS16470_IMU gyro = new ADIS16470_IMU();
    // Initialize slew rate limiters
    private SlewRateLimiter rightLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter leftLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);

    // private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);                                    // IF REV
    // TODO: This line requires you to set encoder.setPositionConversionFactor to a value that will cause the encoder to return its position in meters (if using spark encoders)
    // private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d(gyro.getAngle()),
    // //         leftEncoder.getPosition(), rightEncoder.getPosition());                                                          // IF REV
    //         leftEncoder.getDistance(), rightEncoder.getDistance());

    private DriveType driveType = null;

    public DriveType getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveType driveType) {
        this.driveType = driveType;
    }

    /** Creates a new Drivetrain. */
    public Drivetrain() {

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
            subsystemInst = new Drivetrain();
        }

        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    public void driveSingleJoystickPercent(double speed, double twist) {
        // TODO: Implement Slew Rate Limiters
        double right = (2 * speed + twist) / 2;

        double left = 2 * speed - right;

        driveDualJoystickPercent(left, right);
        SmartDashboard.putString("driveMode", driveType.toString());

        // drive.arcadeDrive(speed, twist, DriveConstants.kSquareJoystickValues);   // IF REV
    }

    public void driveDualJoystickPercent(double left, double right) {

        leftLeader.set(TalonSRXControlMode.PercentOutput, left);
        rightLeader.set(TalonSRXControlMode.PercentOutput, right);
        // leftLeader.set(TalonSRXControlMode.PercentOutput, 0.2);
        SmartDashboard.putString("driveMode", driveType.toString());

        // drive.tankDrive(left, right);                                            // IF REV
    }

    public void resetGyro() {
        gyro.reset();
    }
    
    public double getGyroAngle() {
        return gyro.getAngle();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        var gyro_angle = new Rotation2d(gyro.getAngle());

        // Update the robot pose periodically with encoder values and gyro angle
        // TODO: This line requires you to set encoder.setPositionConversionFactor to a value that will cause the encoder to return its position in meters (if using spark encoders)
        // More information:
        // https://github.com/wpilibsuite/allwpilib/tree/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/differentialdrivebot
        // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/differential-drive-odometry.html
        // robotPose = odometry.update(gyro_angle, leftEncoder.getPosition(), rightEncoder.getPosition());  // if rev
        // robotPose = odometry.update(gyro_angle, leftEncoder.getDistance(), rightEncoder.getDistance());
    }
}