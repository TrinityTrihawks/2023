// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.Constants.DriveConstants;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  private static Drivetrain subsystemInst = null;

  // TODO: Figure out robot pose and how it integrates with vision tracking -- especially PhotonVision
  private Pose2d robotPose = null;

  // Initialize Spark Max's
  private final CANSparkMax leftSparkMax = new CANSparkMax(DriveConstants.kLeftMotorId, MotorType.kBrushless);
  private final CANSparkMax rightSparkMax = new CANSparkMax(DriveConstants.kRightMotorId, MotorType.kBrushless);

  // Initialize Spark Max encoders
  private final RelativeEncoder leftEncoder = leftSparkMax.getEncoder(Type.kHallSensor, DriveConstants.kEncoderCPR);
  private final RelativeEncoder rightEncoder = rightSparkMax.getEncoder(Type.kHallSensor, DriveConstants.kEncoderCPR);

  // TODO: Choose an encoder to use
  private final WPI_PigeonIMU pigeon = new WPI_PigeonIMU(DriveConstants.kPigeonId);

  // Initialize slew rate limiters
  private SlewRateLimiter ylimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
  private SlewRateLimiter xlimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
  private SlewRateLimiter zlimiter = new SlewRateLimiter(DriveConstants.kSlewValue);

  private final DifferentialDrive drive = new DifferentialDrive(leftSparkMax, rightSparkMax);
  // TODO: This line requires you to set encoder.setPositionConversionFactor to a value that will cause the encoder to return its position in meters
  private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(pigeon.getRotation2d(), leftEncoder.getPosition(), rightEncoder.getPosition());

  /** Creates a new Drivetrain. */
  public Drivetrain() {
  }

  public static Drivetrain getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Drivetrain();
    }

    return subsystemInst; // Ensures that only one Drivetrain instance exists at once
  }

  public void driveSingleJoystickPercent(double speed, double twist) {
    // TODO: Implement Slew Rate Limiters
    drive.arcadeDrive(speed, twist, DriveConstants.kSquareJoystickValues);
  }

  public void driveDualJoystickPercent(double left, double right) {
    // TODO: Implement Slew Rate Limiters
    drive.tankDrive(left, right);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    var gyro_angle = pigeon.getRotation2d();

    // Update the robot pose periodically with encoder values and gyro angle
    // TODO: This line requires you to set encoder.setPositionConversionFactor to a value that will cause the encoder to return its position in meters
    robotPose = odometry.update(gyro_angle, leftEncoder.getPosition(), rightEncoder.getPosition());
  }
}