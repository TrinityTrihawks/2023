// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotGyro extends SubsystemBase {
  private ADIS16470_IMU gyro = new ADIS16470_IMU();
  private CANSparkMax sparkMax = new CANSparkMax(13, MotorType.kBrushless);
  private SlewRateLimiter slewRateLimiter = new SlewRateLimiter(0.2); 
  private RelativeEncoder encoder = sparkMax.getEncoder();
  /** Creates a new Gyro. */
  public RobotGyro() {
    gyro.calibrate();
    gyro.reset();
    encoder.setPositionConversionFactor(42);
  }
  public double getAngleY() {
    return gyro.getYFilteredAccelAngle();
  }
  public double getAngleX() {
    return gyro.getXFilteredAccelAngle();
  }
  

  public void drive(double percentOutput) {
      double slewedSpeed = slewRateLimiter.calculate(percentOutput);
      sparkMax.set(slewedSpeed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
