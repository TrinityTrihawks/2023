// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.fenris.subsystems;

import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.MecanumDriveMotorVoltages;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class FenrisDrivetrain extends Drivetrain {
    

    private static FenrisDrivetrain subsystemInst = null;
    private static DriveConstants constants = null;
    
    private final CANSparkMax frontLeftSparkMax = new CANSparkMax(DriveConstants.kFrontLeftMotorId, MotorType.kBrushless);
    private final CANSparkMax rearLeftSparkMax = new CANSparkMax(DriveConstants.kBackLeftMotorId, MotorType.kBrushless);
    private final CANSparkMax frontRightSparkMax = new CANSparkMax(DriveConstants.kFrontRightMotorId, MotorType.kBrushless);
    private final CANSparkMax rearRightSparkMax = new CANSparkMax(DriveConstants.kBackRightMotorId, MotorType.kBrushless);
    
    private final MecanumDrive mecanumDrive = new MecanumDrive(frontLeftSparkMax, rearLeftSparkMax, frontRightSparkMax,rearRightSparkMax);
    
    private final RelativeEncoder frontLeftEncoder = frontLeftSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    private final RelativeEncoder frontRightEncoder = frontRightSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    private final RelativeEncoder backLeftEncoder = rearLeftSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    private final RelativeEncoder backRightEncoder = rearRightSparkMax.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, DriveConstants.kEncoderCPR);
    
    private SlewRateLimiter ylimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter xlimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter zlimiter = new SlewRateLimiter(DriveConstants.kSlewValue);

    
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
    }

    @Override
    public void periodic() {
        
    }

    /**
     * Drives the robot at given x, y and theta speeds. Speeds range from [-1, 1]
     * and the linear
     * speeds have no effect on the angular speed.
     *
     * @param xSpeed        Speed of the robot in the x direction
     *                      (sideways).
     * @param ySpeed        Speed of the robot in the y direction (forward/backwards).
     * @param rot           Angular rate of the robot.
     * @param fieldRelative Whether the provided x and y speeds are relative to the
     *                      field.
     */
    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
        double ySlewSpeed = ylimiter.calculate(ySpeed);
        double xSlewSpeed = xlimiter.calculate(xSpeed);
        double rotSlew = zlimiter.calculate(rot);

        if (fieldRelative) {
            //mecanumDrive.driveCartesian(xSlewSpeed, ySlewSpeed, rotSlew, pigeon.getAngle());
        } else {
            mecanumDrive.driveCartesian(xSlewSpeed, ySlewSpeed, rotSlew);
        }
    }

    public void setDriveMotorControllersVolts(MecanumDriveMotorVoltages volts) {
        frontLeftSparkMax.setVoltage(volts.frontLeftVoltage);
        rearLeftSparkMax.setVoltage(volts.rearLeftVoltage);
        frontRightSparkMax.setVoltage(volts.frontRightVoltage);
        rearRightSparkMax.setVoltage(volts.rearRightVoltage);
    }

    // Resets the drive encoders to currently read a position of 0. 
    public void resetEncoders() {
        frontLeftEncoder.setPosition(0);
        backLeftEncoder.setPosition(0);
        frontRightEncoder.setPosition(0);
        backRightEncoder.setPosition(0);
    }

    public MecanumDriveWheelSpeeds getCurrentWheelSpeeds() {
        return new MecanumDriveWheelSpeeds(
                frontLeftEncoder.getVelocity() * DriveConstants.kMetersPerMotorRotation / 60, //rotations per minute * meters per rotation * minute per seconds
                frontRightEncoder.getVelocity() * DriveConstants.kMetersPerMotorRotation / 60,
                backLeftEncoder.getVelocity() * DriveConstants.kMetersPerMotorRotation / 60,
                backRightEncoder.getVelocity() * DriveConstants.kMetersPerMotorRotation / 60);
    }
    

    /**
     * Sets the max output of the drive. Useful for scaling the drive to drive more
     * slowly.
     *
     * @param maxOutput the maximum output to which the drive will be constrained
     */
    public void setMaxOutput(double maxOutput) {
        mecanumDrive.setMaxOutput(maxOutput);
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
        
        // Double joystick math (courtesy of Peter, Veronica, Luke & Michael)
        double y = (left + right) / 2;
        double twist = (right - left) / 2;

        // for whatever reason, forward is not forward
        drive(-y, 0, twist, false);
        
    }

    @Override
    public DrivetrainConstants getConstants() {
        if (constants == null) {
            constants = new DriveConstants();
        }

        return constants; 
    }

    
    public static final class DriveConstants implements DrivetrainConstants {
        // Spark IDs
        public static final int kFrontLeftMotorId = 11;
        public static final int kFrontRightMotorId = 12;
        public static final int kBackLeftMotorId = 13;
        public static final int kBackRightMotorId = 14;
    
    
        // https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/mecanumcontrollercommand/Constants.java
    
    
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
        
        public static final double kStaticThrottleScalar = 1; // multiple inputs values by this

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