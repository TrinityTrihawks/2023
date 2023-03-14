// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023.subsystems;

import org.trinity4215.robot2023.CombinedLogging;
import org.trinity4215.robot2023.Constants.IntakeConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

enum IntakeDriveDirection {
  kForward, kBackwards
}

public class Intake extends SubsystemBase {

  private static Intake subsystemInst = null;
  private CANSparkMax intakeRunSparkMax = new CANSparkMax(IntakeConstants.kRunSparkID, MotorType.kBrushed);
  private CANSparkMax intakeRaiseSparkMax = new CANSparkMax(IntakeConstants.kRaiseSparkID, MotorType.kBrushed);
  private RelativeEncoder intakeRaiseEncoder = intakeRaiseSparkMax.getEncoder(Type.kQuadrature, IntakeConstants.kRevEncoderCountsPerRevolution);

  /** Creates a new Intake. */
  public Intake() {

    intakeRunSparkMax.restoreFactoryDefaults();
    intakeRunSparkMax.setIdleMode(IdleMode.kBrake);

    intakeRaiseSparkMax.restoreFactoryDefaults();
    // intakeRaiseSparkMax.setIdleMode(IdleMode.kBrake);

    intakeRaiseEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);

    System.out.println("Intake initialized!");

  }

  public static Intake getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Intake();
    }
    return subsystemInst; // Ensures that only one Drivetrain instance exists at once
  }

  public void runIntake(double speed) {
    intakeRunSparkMax.set(speed);
  }

  public double getAbsoluteEncoderPosition() {
    return intakeRaiseEncoder.getPosition();
  }

  public void driveToDegrees(double targetPosition, double speed) {
    double currentPosition = intakeRaiseEncoder.getPosition();
    CombinedLogging.putNumber("IntakeRaiseEncoderPosition", currentPosition);

    if (currentPosition > 10000) {
      intakeRaiseSparkMax.set(speed);
      return;
    }

    if ((Math.abs(currentPosition) - Math.abs(targetPosition) < IntakeConstants.kOuterDeadzoneArea/2 ) && ( Math.abs(currentPosition) - Math.abs(targetPosition) > -IntakeConstants.kOuterDeadzoneArea/2)) {
      speed = .3  *speed;
      SmartDashboard.putBoolean("5deg", true); 
    } else {
      SmartDashboard.putBoolean("5deg", false);
    }

    if (currentPosition < targetPosition - (IntakeConstants.kInnerDeadzoneArea/2)) {
      intakeRaiseSparkMax.set(speed);
    } else if (currentPosition > targetPosition + (IntakeConstants.kInnerDeadzoneArea/2)) {
      intakeRaiseSparkMax.set(-speed);
    } else {
      stop();
    }
  }

  public void spit() {
    intakeRunSparkMax.set(IntakeConstants.kIntakeSpitSpeed);
  }
  public void suck() {
    intakeRunSparkMax.set(IntakeConstants.kIntakeSuckSpeed);
  }

  public void stop() {
    intakeRunSparkMax.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
