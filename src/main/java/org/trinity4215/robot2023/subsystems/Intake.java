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

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

enum IntakeDriveDirection {
  kForward, kBackwards
}

public class Intake extends SubsystemBase {

  private static Intake subsystemInst = null;

  private CANSparkMax spinMotor = new CANSparkMax(IntakeConstants.kRunSparkID, MotorType.kBrushed);
  private RelativeEncoder spinEncoder = spinMotor.getEncoder(Type.kQuadrature,
      IntakeConstants.kRevEncoderCountsPerRevolution);

  private CANSparkMax raiseMotor = new CANSparkMax(IntakeConstants.kRaiseSparkID, MotorType.kBrushed);
  private RelativeEncoder raiseEncoder = raiseMotor.getEncoder(Type.kQuadrature,
      IntakeConstants.kRevEncoderCountsPerRevolution);

  private DigitalInput topLimitSwitch = new DigitalInput(8);
  private DigitalInput bottomLimitSwitch = new DigitalInput(9);

  /** Creates a new Intake. */
  public Intake() {

    spinMotor.restoreFactoryDefaults();
    spinMotor.setIdleMode(IdleMode.kBrake);
    spinEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);

    raiseMotor.restoreFactoryDefaults();
    raiseMotor.setIdleMode(IdleMode.kCoast);
    raiseEncoder.setPositionConversionFactor(IntakeConstants.kPositionConversionFactor);

    System.out.println("Intake initialized!");

  }

  public static Intake getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Intake();
    }
    return subsystemInst; // Ensures that only one Drivetrain instance exists at once
  }

  public void runIntake(double speed) {
    spinMotor.set(speed);
  }

  public double getAbsoluteEncoderRawPositionDegrees() {
    return raiseEncoder.getPosition();
  }

  public double getConditionedAbsoluteEncoderPosition() {
    double rawPos = getAbsoluteEncoderRawPositionDegrees();
    double outputPos;
    if (rawPos > 10000) {
      outputPos = rawPos - 98876;
    } else {
      outputPos = rawPos;
    }
    return outputPos;
  }

  public void driveToDegrees(double targetPosition, double speed) {
    double currentPosition = getConditionedAbsoluteEncoderPosition();
    CombinedLogging.putNumber("IntakeRaiseEncoderPosition", currentPosition);

    boolean goingUp = !((targetPosition - currentPosition) > 0);
    SmartDashboard.putBoolean("GoingUp", goingUp);
    
    if (goingUp && getTopLimitSwitch()) {
      raiseMotor.stopMotor();
      return;
    }
    if ((!goingUp) && getBottomLimitSwitch()) {
      raiseMotor.stopMotor();
      return;
    }

    if ((currentPosition - targetPosition < IntakeConstants.kOuterDeadzoneArea / 2)
        && (currentPosition - targetPosition > -IntakeConstants.kOuterDeadzoneArea / 2)) {
      speed = IntakeConstants.kInnerSlowzoneSpeedReductionScalar * speed;
      SmartDashboard.putBoolean("5deg", true);
    } else {
      SmartDashboard.putBoolean("5deg", false);
    }

    // if (currentPosition < targetPosition - (IntakeConstants.kInnerSlowzoneArea / 2)) {
    //   raiseMotor.set(-speed);
    // } else if (currentPosition > targetPosition + (IntakeConstants.kInnerSlowzoneArea / 2)) {
    //   raiseMotor.set(speed);
    // } else {
    //   stop();
    // }
  }

  enum IntakeType {
    CONE, CUBE
  }

  public void spit(IntakeType intakeType) {
    spinMotor.set(IntakeConstants.kIntakeSpitSpeed * (intakeType == IntakeType.CONE ? 1 : -1));
  }

  public void suck(IntakeType intakeType) {
    spinMotor.set(IntakeConstants.kIntakeSuckSpeed * (intakeType == IntakeType.CONE ? 1 : -1));
  }

  public boolean hasCone(IntakeType intakeType) {

    if (intakeType == IntakeType.CONE) {

    }
    return false;
  }

  public void stop() {
    spinMotor.set(0);
  }

  public boolean getTopLimitSwitch() {
    return !topLimitSwitch.get();
  }

  public boolean getBottomLimitSwitch() {
    return bottomLimitSwitch.get();
  }

  @Override
  public void periodic() {

    // This method will be called once per scheduler run
    SmartDashboard.putNumber("IntakeAbsolutEncoderPosition", getConditionedAbsoluteEncoderPosition());
    SmartDashboard.putBoolean("TopLimitSwitch", getTopLimitSwitch());
    SmartDashboard.putBoolean("BottomLimitSwitch", getBottomLimitSwitch());
  }
}
