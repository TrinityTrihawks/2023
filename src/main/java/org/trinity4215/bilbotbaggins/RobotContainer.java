// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins;

import org.trinity4215.bilbotbaggins.Constants.OperatorConstants;
import org.trinity4215.bilbotbaggins.commands.DriveJoystick;
import org.trinity4215.bilbotbaggins.commands.TurnDegrees;
import org.trinity4215.bilbotbaggins.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
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
    private final Drivetrain drivetrain = BotSwitcher.getDrivetrain();

    // ==================== CONTROLLERS =====================
    private final CommandXboxController gollum = new CommandXboxController(
            OperatorConstants.kXboxPort);

    private final ZeroableJoystick samwiseGamgee = new ZeroableJoystick(OperatorConstants.kLeftStickPort,
            "Samwise Gamgee");
    private final ZeroableJoystick frodoBaggins = new ZeroableJoystick(OperatorConstants.kRightStickPort,
            "Frodo Baggins");

    // ==================== COMMANDS ========================
    private final DriveJoystick defaultDrive = new DriveJoystick(
            drivetrain,
            samwiseGamgee::getZeroedY,
            frodoBaggins::getZeroedY,
            frodoBaggins::getZeroedTwist);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        configureBindings();
        configureDefaultCommands();
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
        // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
        // new Trigger(exampleSubsystem::exampleCondition)
        // .onTrue(new ExampleCommand(exampleSubsystem));

        // Schedule `exampleMethodCommand` when the Xbox controller's B button is
        // pressed,
        // cancelling on release.
        // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

        frodoBaggins.zero();
        samwiseGamgee.zero();
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new TurnDegrees(180, drivetrain);
    }
}
