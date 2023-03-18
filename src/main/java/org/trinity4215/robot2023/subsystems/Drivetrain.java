// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.DriveConstants;
import org.trinity4215.robot2023.Constants.DriveConstants.DriveType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    private static Drivetrain subsystemInst = null;

    private Pose2d robotPose = null;

    private final CANSparkMax leftLeader = new CANSparkMax(DriveConstants.kLeftLeaderId, MotorType.kBrushless);
    private final CANSparkMax leftFollower = new CANSparkMax(DriveConstants.kLeftFollowerId,
            MotorType.kBrushless);
    private final CANSparkMax rightLeader = new CANSparkMax(DriveConstants.kRightLeaderId,
            MotorType.kBrushless);
    private final CANSparkMax rightFollower = new CANSparkMax(DriveConstants.kRightFollowerId,
            MotorType.kBrushless);

    private final CANSparkMax[] 
    sparks = new CANSparkMax[] 
            { leftLeader,   rightLeader,
              leftFollower, rightFollower };

    private final MotorControllerGroup leftMotorControllerGroup = new MotorControllerGroup(leftLeader, leftFollower);
    private final MotorControllerGroup rightMotorControllerGroup = new MotorControllerGroup(rightLeader, rightFollower);


    // Initialize Spark Max encoders
    private final RelativeEncoder leftEncoder = leftLeader.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor,
            DriveConstants.kEncoderCPR); // IF REV
    private final RelativeEncoder rightEncoder = rightLeader.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor,
            DriveConstants.kEncoderCPR); // IF REV

    private final ADIS16470_IMU gyro = new ADIS16470_IMU();
    private edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis axis = null;

    // Initialize slew rate limiters
    private SlewRateLimiter rightLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter leftLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);

    private SlewRateLimiter speedLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);
    private SlewRateLimiter twistLimiter = new SlewRateLimiter(DriveConstants.kSlewValue);

    private final DifferentialDrive drive = new DifferentialDrive(leftMotorControllerGroup, rightMotorControllerGroup);

    private double leftTargetVelocity = 0;
    private double rightTargetVelocity = 0;
    private boolean velocityMode = false;

    // TODO: This line requires you to set encoder.setPositionConversionFactor to a
    // value that will cause the encoder to return its position in meters
    // private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d(getGyroZ()),
    //         leftEncoder.getPosition(), rightEncoder.getPosition());

    private DriveType driveType = null;

    public DriveType getDriveType() {
        return driveType;
    }

    public void setDriveType(DriveType driveType) {
        this.driveType = driveType;
    }

    /** Creates a new Drivetrain. */
    private Drivetrain() {
        axis = IMUAxis.kY;
        leftEncoder.setPositionConversionFactor(DriveConstants.kPositionConversionFactor);
        rightEncoder.setPositionConversionFactor(DriveConstants.kPositionConversionFactor);
        leftMotorControllerGroup.setInverted(true);
    }

    public static Drivetrain getInstance() {
        if (subsystemInst == null) {
            subsystemInst = new Drivetrain();
        }

        return subsystemInst; // Ensures that only one Drivetrain instance exists at once
    }

    /**
     * n. b. this calles configVelocityMode(false)
     */
    public void driveArcadePercent(double speed, double twist) {
        configVelocityMode(false);
        drive.arcadeDrive(speedLimiter.calculate(speed), twistLimiter.calculate(twist));
    }

    /**
     * n. b. this calles configVelocityMode(false)
     */
    public void driveTankPercent(double left, double right) {
        configVelocityMode(false);
        drive.tankDrive(leftLimiter.calculate(left), rightLimiter.calculate(right));
        CombinedLogging.putNumber("inputs", left);
    }

    /**
     * sets the target wheel speeds for the drivetrain.
     * PLEASE NOTE THIS IS A SET-AND-FORGET FUNCTION. WHEN CALLED
     * IT WILL CONFIGURE THE DRIVETRAIN TO TARGET THeSe VELOCITies
     * continuously until `configVelocityMode(boolean)` is called 
     * with `false`.
     * inputs are in RPM (at least for the time being)
     */
    public void driveTankVelocity(double left, double right) {
        configVelocityMode(true);
        leftTargetVelocity = left;
        rightTargetVelocity = right;
    }

    public void configVelocityMode(boolean on) {
        velocityMode = on;
    }

    public void stop() {
        drive.stopMotor();
        CombinedLogging.putNumber("inputs", Double.NaN);
    }

    public void resetGyro() {
        gyro.reset();
    }


    public void brakeIdle() {
        for (CANSparkMax spark : sparks) {
            spark.setIdleMode(IdleMode.kBrake);
        }
    }

    public void releaseBrake() {
        for (CANSparkMax spark : sparks) {
            spark.setIdleMode(IdleMode.kCoast);
        }
    }


    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(
            -leftEncoder.getVelocity() * DriveConstants.kMotorRPMToMetersPerMinute / 60,
            rightEncoder.getVelocity() * DriveConstants.kMotorRPMToMetersPerMinute / 60);
    }


    public double getGyroY() {
        IMUAxis curAxis = gyro.getYawAxis();
        CombinedLogging.putString("CurrentAxis", curAxis.toString());
        if (curAxis == IMUAxis.kY) {
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        } else {
            axis = IMUAxis.kY;
            gyro.setYawAxis(axis);
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        }
    }

    public double getGyroX() {
        IMUAxis curAxis = gyro.getYawAxis();
        CombinedLogging.putString("CurrentAxis", curAxis.toString());
        if (curAxis == IMUAxis.kX) {
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        } else {
            axis = IMUAxis.kX;
            gyro.setYawAxis(axis);
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        }
    }

    public double getGyroZ() {
        IMUAxis curAxis = gyro.getYawAxis();
        CombinedLogging.putString("CurrentAxis", curAxis.toString());
        if (curAxis == IMUAxis.kZ) {
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        } else {
            axis = IMUAxis.kZ;
            gyro.setYawAxis(axis);
            double angle = gyro.getAngle();
            CombinedLogging.putNumber("CurrentAngle", angle);
            return angle;
        }
    }

    public IMUAxis getCurrentGyroAxis() {
        return gyro.getYawAxis();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // var gyro_angle = new Rotation2d(getGyroZ());

        // Update the robot pose periodically with encoder values and gyro angle
        // TODO: This line requires you to set encoder.setPositionConversionFactor to a
        // value that will cause the encoder to return its position in meters (if using
        // spark encoders)
        // More information:
        // https://github.com/wpilibsuite/allwpilib/tree/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/differentialdrivebot
        // https://docs.wpilib.org/en/stable/docs/software/kinematics-and-odometry/differential-drive-odometry.html
        // robotPose = odometry.update(gyro_angle, leftEncoder.getPosition(),
        //         rightEncoder.getPosition());

        // be careful. e-stop judiciously...
        if (velocityMode) {
            updateWheelSpeeds();
        }
    }

    private void updateWheelSpeeds() {
        double distanceToTargetLeft = leftTargetVelocity - leftEncoder.getVelocity();
        double distanceToTargetRight = rightTargetVelocity - rightEncoder.getVelocity();
        
        driveTankPercent(
            distanceToTargetLeft 
                * DriveConstants.kPorportionalDriveFeedback
                * DriveConstants.kRPMToPercentage, 
            distanceToTargetRight 
                * DriveConstants.kPorportionalDriveFeedback
                * DriveConstants.kRPMToPercentage
        );
    }
}