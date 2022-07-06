package Code;

import java.io.FileWriter;
import java.io.IOException;

public class Generator {
    public static String name = "test";
    public static String path = "test/";
    public static String[][] outputs = {
            { "Boolean", "test1",
                    "m_robotContainer.m_fireControl.isReadyToShoot() && RobotState.getInstance().getCurrentRobotState().equals(RobotStates.PREPARE_TO_SHOOT)" },
            { "String", "test2" } 
        };
    public static String[][] inputs = {
            { "String", "test3" },
            { "Boolean", "test4" },
            { "Boolean", "test5" } 
        };
    public static String[][] triggers = {
            { "D.punch()", "Punch()" },
            { "O.lift()", "Lift()" },
            { "test1" }
         };

    public static void generate(){
        // general imports, package and class declaration
        String toCreate = """
                // Copyright (c) FIRST and other WPILib contributors.
                // Open Source Software; you can modify and/or share it under the terms of
                // the WPILib BSD license file in the root directory of this project.

                package frc.robot.test.ois;

                import static frc.bumblelib.framework.BumbleRobot.m_robotContainer;
                import static frc.robot.profiles.InitProfiles.ROBOT_PROFILE;

                import edu.wpi.first.wpilibj2.command.InstantCommand;
                import edu.wpi.first.wpilibj2.command.button.Trigger;
                import frc.bumblelib.framework.OI;
                import frc.bumblelib.util.dashboard_utils.BooleanSmartEntry;
                import frc.bumblelib.util.dashboard_utils.StringSmartEntry;
                import frc.bumblelib.util.dashboard_utils.NumberSmartEntry;
                import frc.robot.ControlMap;
                """;

        toCreate += "\npublic class " + name + " extends OI {\n";
        toCreate += "   private final String DASHBOARD_PATH  = \"" + path + "\";\n";
        // for every item, create it and get a new line
        toCreate += "   //Outputs\n";
        for (String[] item : outputs) {
            toCreate += "   private " + item[0] + "SmartEntry " + item[1] + ";\n";
        }
        toCreate += "   //Inputs\n";
        for (String[] item : inputs) {
            toCreate += "   private " + item[0] + "SmartEntry " + item[1] + ";\n";
        }
        // for every item, initialize it with its type, name and path and get a new line
        toCreate += "\n\n   @Override\n   public void initDashboard() {\n";
        for (String[] item : outputs) {
            toCreate += "       " + item[1] + "= new " + item[0] + "SmartEntry(DASHBOARD_PATH  + \"" + item[1] + "\");\n";
        }
        for (String[] item : inputs) {
            toCreate += "       " + item[1] + "= new " + item[0] + "SmartEntry(DASHBOARD_PATH  + \"" + item[1] + "\");\n";
        }
        toCreate = toCreate.substring(0,toCreate.length()-1); // remove last \n
        // for every output, update it. if there is no function to update it, update it to None and get a new line
        toCreate += "\n   }\n\n\n   @Override\n   public void periodic() {\n";
        for (String[] output : outputs) {
            toCreate += "       " + output[1] + ".update(";
            if (output.length > 2) { // is there a custom function?
                toCreate += output[2] + ");\n";
            } else {
                toCreate += "None); //TODO: add a real function here\n";
            }
        }
        toCreate = toCreate.substring(0,toCreate.length()-1); // remove last \n
        toCreate += "\n   }\n\n\n   @Override\n   public void bind() {\n";
        /*
         * for every input. create a trigger with it's activator and command. if the
         * command does not exist, create an empty command
         * the activation has a shortening:
         * D. for ControlMap.getInstance().getDriverKeyMap().
         * O. for ControlMap.getInstance().getOperatorKeyMap().
         * and anything else gets a .get() at the end assuming it is a shuffleboard item
         */
        for (String[] trigger : triggers) {
            toCreate += "       new Trigger(()-> ";
            if (trigger[0].substring(0, 2).toUpperCase().equals("D.")) { // are the first 2 letters D.?
                toCreate += "ControlMap.getInstance().getDriverKeyMap()." + trigger[0].substring(2); // replace with long shit
            } else if (trigger[0].substring(0, 2).toUpperCase().equals("O.")) { //saצe as D.
                toCreate += "ControlMap.getInstance().getOperatorKeyMap()." + trigger[0].substring(2);
            } else {
                toCreate += trigger[0] + ".get()";
            }
            toCreate += ").whileActiveOnce(new ";
            if (trigger.length > 1) { // does the trigger contain a custom command?
                toCreate += trigger[1] + ");\n";
            } else {
                toCreate += "instantCommand(()->{})); // TODO: add a real command here\n";
            }
        }
        toCreate = toCreate.substring(0,toCreate.length()-1); // remove last \n
        toCreate += "\n   }\n}";
        // Write To File
        try (FileWriter code = new FileWriter (name + ".java")) {
            code.write(toCreate);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        generate();
    }

}