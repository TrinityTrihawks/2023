// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins.subsystems.drivetrains;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;

import edu.wpi.first.math.filter.SlewRateLimiter;

public class FenrisDrivetrain extends Drivetrain {
    

    private static FenrisDrivetrain subsystemInst = null;
    private static DriveConstants constants = null;
    
    private final CANSparkMax frontLeftSparkMax = new CANSparkMax(DriveConstants.kFrontLeftMotorId, MotorType.kBrushless);
    private final CANSparkMax rearLeftSparkMax = new CANSparkMax(DriveConstants.kBackLeftMotorId, MotorType.kBrushless);
    private final CANSparkMax frontRightSparkMax = new CANSparkMax(DriveConstants.kFrontRightMotorId, MotorType.kBrushless);
    private final CANSparkMax rearRightSparkMax = new CANSparkMax(DriveConstants.kBackRightMotorId, MotorType.kBrushless);
        
    private final RelativeEncoder frontLeftEncoder = frontLeftSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    private final RelativeEncoder frontRightEncoder = frontRightSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    private final RelativeEncoder backLeftEncoder = rearLeftSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    private final RelativeEncoder backRightEncoder = rearRightSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    
    private SlewRateLimiter leftLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter rightLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    
    /**
     * Use this method to create a drivetrain instance. This method, in conjunction with a private constructor,
     * ensures that the
     * drivetrain class is a singleton, aka, that only one drivetrain object ever
     * gets created
     * 
     * 
     */
    public static Drivetrain getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new FenrisDrivetrain();
        } 
        return subsystemInst;
    }

    private FenrisDrivetrain() {
        // We need to invert one side of the drivetrain so that positive voltages
        // result in both sides moving forward.
        frontRightSparkMax.setInverted(true);
        rearRightSparkMax.setInverted(true);
        
        frontLeftSparkMax.setIdleMode(IdleMode.kBrake);
        frontRightSparkMax.setIdleMode(IdleMode.kBrake);
        rearLeftSparkMax.setIdleMode(IdleMode.kBrake);
        rearRightSparkMax.setIdleMode(IdleMode.kBrake);

        rearRightSparkMax.follow(frontRightSparkMax);
        rearLeftSparkMax.follow(frontLeftSparkMax);
    }

    @Override
    public void periodic() {
        
    }


    // Resets the drive encoders to currently read a position of 0. 
    public void resetEncoders() {
        frontLeftEncoder.setPosition(0);
        backLeftEncoder.setPosition(0);
        frontRightEncoder.setPosition(0);
        backRightEncoder.setPosition(0);
    }

    

    /**
     * Configures the wheels to brake on idle
     */
    public void brakeIdle() {
        frontLeftSparkMax.setIdleMode(IdleMode.kBrake);
        frontRightSparkMax.setIdleMode(IdleMode.kBrake);
        rearLeftSparkMax.setIdleMode(IdleMode.kBrake);
        rearRightSparkMax.setIdleMode(IdleMode.kBrake);
    }

    /**
     * Configures the wheels to coast on idle
     */
    public void releaseBrake() {
        frontLeftSparkMax.setIdleMode(IdleMode.kCoast);
        frontRightSparkMax.setIdleMode(IdleMode.kCoast);
        rearLeftSparkMax.setIdleMode(IdleMode.kCoast);
        rearRightSparkMax.setIdleMode(IdleMode.kCoast);
    }

    @Override
    public void driveTank(double left, double right) {
        
        // we must make sure to initialize the constants
        // before we use them.
        setConstants();

        frontLeftSparkMax.set(
            leftLimiter.calculate(left)
        );

        frontRightSparkMax.set(
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

    
    public static final class DriveConstants implements DrivetrainConstants {
        // Spark IDs
        public static final int kFrontLeftMotorId = 11;
        public static final int kFrontRightMotorId = 12;
        public static final int kBackLeftMotorId = 13;
        public static final int kBackRightMotorId = 14;
        
    
        // Encoder direction
        public static final boolean kFrontLeftEncoderReversed = false;
        public static final boolean kBackLeftEncoderReversed = true;
        public static final boolean kFrontRightEncoderReversed = false;
        public static final boolean kBackRightEncoderReversed = true;
    
    
        public static final double kGearRatio = 10.71; // 10.1:1
        public static final int kEncoderCPR = 42; // counts per revolution
        public static final double kWheelDiameterMeters = 0.15; // 6in
        public static final double kMetersPerMotorRotation = (kWheelDiameterMeters * Math.PI) / kGearRatio;
    
        public static final double kSlewValue = 1.5;
        
        public static final double kStaticThrottleScalar = 0.4; // multiple inputs values by this

        @Override
        public double kMaxSpeedPercent() {
            return kStaticThrottleScalar;
        }

        @Override
        public double kMinTurnSpeed() {
            return 0.1;
        }
    }
}