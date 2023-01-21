// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotGyro extends SubsystemBase {
    private ADIS16470_IMU gyro = new ADIS16470_IMU();
    private SlewRateLimiter slewRateLimiter = new SlewRateLimiter(0);
    private TalonSRX rightTalonSRX = new TalonSRX(Constants.DriveConstants.TALONSRX.kRightTest);
    private TalonSRX leftTalonSRX = new TalonSRX(Constants.DriveConstants.TALONSRX.kLeftTest);

    private static RobotGyro instance = null;

    public static RobotGyro getInstance() {
        if (instance == null) {
            return instance = new RobotGyro();
        } else {
            return instance;
        }
    }

    /** Creates a new Gyro. */
    private RobotGyro() {
        gyro.calibrate();
        gyro.reset();
    }

    public double getAngleY() {
        return gyro.getYFilteredAccelAngle();
    }

    public double getAngleX() {
        return gyro.getXFilteredAccelAngle();
    }

    public double getAngleYaw() {
        return gyro.getAngle();
    }

    public void drive(double percentOutput) {
    }

    public void drive(double rightSpeed, double leftSpeed) {
        rightTalonSRX.set(ControlMode.PercentOutput, /*slewRateLimiter.calculate(*/rightSpeed/*)*/);
        leftTalonSRX.set(ControlMode.PercentOutput, /*slewRateLimiter.calculate(*/leftSpeed/*)*/);
        
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    
}
