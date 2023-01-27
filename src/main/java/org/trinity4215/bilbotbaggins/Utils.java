// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.trinity4215.bilbotbaggins;


public final class Utils {
    public static double constr(double val, double min, double max) {
        return val > max? max : (val < min? min : val);
    }
}
