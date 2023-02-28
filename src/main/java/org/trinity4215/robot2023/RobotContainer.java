// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023;


import org.trinity4215.robot2023.Constants.DriveConstants.DriveType;
import org.trinity4215.robot2023.Constants.OperatorConstants;
import org.trinity4215.robot2023.commands.Autos;
import org.trinity4215.robot2023.commands.DriveJoystick;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.Gripper;
import org.trinity4215.robot2023.subsystems.Limelight;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    // ==================== SUBSYSTEMS ======================
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Limelight limelight = Limelight.getInstance();
    private Gripper gripper = Gripper.getInstance();

    // ==================== CONTROLLERS =====================
    private final CommandXboxController gollum_subsys = new CommandXboxController(
            OperatorConstants.kGollumSubsysPort);

    private final CommandJoystick samwiseGamgee_left = new CommandJoystick(OperatorConstants.kSamwiseLeftStickPort);
    private final CommandJoystick frodoBaggins_right = new CommandJoystick(OperatorConstants.kFrodoRightStickPort);

    // ==================== COMMANDS ========================
    private final DriveJoystick defaultDrive = new DriveJoystick(
            drivetrain,
            samwiseGamgee_left::getY,
            frodoBaggins_right::getY,
            frodoBaggins_right::getTwist);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        configureBindings();
        configureDefaultCommands();
        drivetrain.setDriveType(DriveType.DUAL);
        PortForwarder.add(5800, "10.42.15.11", 5800);
    }


    private void configureDefaultCommands() {
        drivetrain.setDefaultCommand(defaultDrive);
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be
     * created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
     * an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
     * {@link
     * CommandXboxController
     * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or
     * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    private void configureBindings() {

        configFrodo();
        configGollum();
    }

    private void configFrodo() {
        frodoBaggins_right.povUp().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(-OperatorConstants.kSlowWheelSpeedPercent, 0),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povDown().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(OperatorConstants.kSlowWheelSpeedPercent, 0),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povLeft().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(0, OperatorConstants.kSlowWheelSpeedPercent), // ccw is +
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povRight().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(0, -OperatorConstants.kSlowWheelSpeedPercent),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povUpLeft().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(
                        OperatorConstants.kSlowWheelSpeedPercent, 
                        OperatorConstants.kSlowWheelSpeedPercent
                    ),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povUpRight().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(
                        OperatorConstants.kSlowWheelSpeedPercent, 
                        -OperatorConstants.kSlowWheelSpeedPercent
                    ),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povDownLeft().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(
                        -OperatorConstants.kSlowWheelSpeedPercent, 
                        OperatorConstants.kSlowWheelSpeedPercent
                    ),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );

        frodoBaggins_right.povDownRight().debounce(0.25).whileTrue(
            new RepeatCommand(
                new InstantCommand(
                    () -> drivetrain.driveArcadePercent(
                        -OperatorConstants.kSlowWheelSpeedPercent, 
                        -OperatorConstants.kSlowWheelSpeedPercent
                    ),
                    drivetrain
                )
            ).andThen(
                new InstantCommand(
                    () -> drivetrain.stop(),
                    drivetrain
                )
            )
        );
    }

    private void configGollum() {
        
        gollum_subsys.a().onTrue(
            new InstantCommand(
                () -> gripper.toggleGrab(),
                gripper
            )
        );


        gollum_subsys.y().onTrue(
            new InstantCommand(
                () -> {
                    gripper.toggleUp();
                },
                gripper
            )
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An example command will be run in autonomous
        // return new TurnDegrees(180, drivetrain);
        // return new AutoLevel(drivetrain);
        return Autos.balance(drivetrain);
        // return new SequentialCommandGroup(
        // new RepeatCommand(
        // new InstantCommand(() -> {
        // SmartDashboard.putNumber("GyroX", drivetrain.getGyroX());
        // SmartDashboard.putString("CurrentGyroAxis",
        // drivetrain.getCurrentGyroAxis().toString());
        // }, drivetrain)).withTimeout(10),
        // new WaitCommand(1),
        // new RepeatCommand(
        // new InstantCommand(() -> {
        // SmartDashboard.putNumber("GyroY", drivetrain.getGyroY());
        // SmartDashboard.putString("CurrentGyroAxis",
        // drivetrain.getCurrentGyroAxis().toString());
        // }, drivetrain)).withTimeout(10),
        // new WaitCommand(1),
        // new RepeatCommand(
        // new InstantCommand(() -> {
        // SmartDashboard.putNumber("GyroZ", drivetrain.getGyroZ());
        // SmartDashboard.putString("CurrentGyroAxis",
        // drivetrain.getCurrentGyroAxis().toString());
        // }, drivetrain)).withTimeout(10));
    }
}
