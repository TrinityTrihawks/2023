// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.robot2023;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Add your docs here. */
public class CombinedLogging {

    private static enum LoggingLevel {
        ALL,
        PRIORITY,
        NONE
    };

    private static enum LoggingDestination {
        SMART_DASHBOARD,
        COMMAND_LINE,
        BOTH
    }

    private static final LoggingLevel loggingLevel = LoggingLevel.NONE;
    private static final LoggingDestination loggingDestination = LoggingDestination.SMART_DASHBOARD;
    private static final List<String> priorityKeys = Arrays.asList(
            "driveType" // Add priority keys here
            , "Error");

    private static void print(String key, Object value) {
        if (loggingDestination == LoggingDestination.BOTH | loggingDestination == LoggingDestination.COMMAND_LINE) {
            System.out.println(key + ": " + value.toString());
        }
    }

    public static void putNumber(String key, double number) {
        if (loggingLevel == LoggingLevel.NONE) {
            return;
        } else if (loggingLevel == LoggingLevel.PRIORITY) {
            if (priorityKeys.stream().anyMatch(s -> s.equals(key))) {
                SmartDashboard.putNumber(key, number);
                print(key, number);
            } else {
                return;
            }
        } else {
            SmartDashboard.putNumber(key, number);
            print(key, number);
        }
    }

    public static void putString(String key, String string) {
        if (loggingLevel == LoggingLevel.NONE) {
            return;
        } else if (loggingLevel == LoggingLevel.PRIORITY) {
            if (priorityKeys.stream().anyMatch(s -> s.equals(key))) {
                SmartDashboard.putString(key, string);
                print(key, string);
            } else {
                return;
            }
        } else {
            SmartDashboard.putString(key, string);
            print(key, string);
        }
    }

    public static void putBoolean(String key, boolean value) {
        if (loggingLevel == LoggingLevel.NONE) {
            return;
        } else if (loggingLevel == LoggingLevel.PRIORITY) {
            if (priorityKeys.stream().anyMatch(s -> s.equals(key))) {
                SmartDashboard.putBoolean(key, value);
                print(key, value);
            } else {
                return;
            }
        } else {
            SmartDashboard.putBoolean(key, value);
            print(key, value);
        }
    }
}
