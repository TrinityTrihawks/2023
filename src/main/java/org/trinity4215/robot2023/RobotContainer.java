// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023;

import org.trinity4215.robot2023.Constants.OperatorConstants;
import org.trinity4215.robot2023.commands.Autos;
import org.trinity4215.robot2023.commands.DriveJoystick;
import org.trinity4215.robot2023.subsystems.Drivetrain;
import org.trinity4215.robot2023.subsystems.Intake;

import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
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
    private final Intake intake = Intake.getInstance();

    // ==================== CONTROLLERS =====================
    private final CommandXboxController xbox = new CommandXboxController(
            OperatorConstants.kGollumDrivePort);


    private final SendableChooser<Command> autonSwitch = new SendableChooser<>();

    // ==================== COMMANDS ========================
    private final DriveJoystick defaultDrive = new DriveJoystick(
            drivetrain,
            xbox::getLeftY,
            xbox::getRightY,
            xbox::getLeftX);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        configureBindings();
        configureDefaultCommands();
        configureAutonomoi();
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

        configGollum();
    }


    private void configGollum() {

        xbox.leftTrigger().whileTrue(new StartEndCommand(() -> {intake.spit();}, () -> {intake.stop();}, intake));
        xbox.rightTrigger().whileTrue(new StartEndCommand(() -> {intake.suck();}, () -> {intake.stop();}, intake));
   
    }

    private void configureAutonomoi() {

        autonSwitch.setDefaultOption(
                "Simple Mobility (2pts)",
                Autos.mobility(drivetrain));

        autonSwitch.addOption(
                "Auto-Balance (12pts)",
                Autos.balance(drivetrain));

        autonSwitch.addOption(
                "Mobility & Auto-Balance (15pts)",
                Autos.mobilityBackAndBalance(drivetrain));
        autonSwitch.addOption("Test Intake Raising", Autos.testPositionalIntakeRaise(intake));

        SmartDashboard.putData("Autonomoi", autonSwitch);

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autonSwitch.getSelected();
    }
}
