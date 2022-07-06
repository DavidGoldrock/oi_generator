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

public class test extends OI {
   private final String DASHBOARD_PATH  = "test/";
   //Outputs
   private BooleanSmartEntry test1;
   private StringSmartEntry test2;
   //Inputs
   private StringSmartEntry test3;
   private BooleanSmartEntry test4;
   private BooleanSmartEntry test5;


   @Override
   public void initDashboard() {
       test1= new BooleanSmartEntry(DASHBOARD_PATH  + "test1");
       test2= new StringSmartEntry(DASHBOARD_PATH  + "test2");
       test3= new StringSmartEntry(DASHBOARD_PATH  + "test3");
       test4= new BooleanSmartEntry(DASHBOARD_PATH  + "test4");
       test5= new BooleanSmartEntry(DASHBOARD_PATH  + "test5");
   }


   @Override
   public void periodic() {
       test1.update(m_robotContainer.m_fireControl.isReadyToShoot() && RobotState.getInstance().getCurrentRobotState().equals(RobotStates.PREPARE_TO_SHOOT));
       test2.update(None); //TODO: add a real function here
   }


   @Override
   public void bind() {
       new Trigger(()-> ControlMap.getInstance().getDriverKeyMap().punch()).whileActiveOnce(new Punch());
       new Trigger(()-> ControlMap.getInstance().getOperatorKeyMap().lift()).whileActiveOnce(new Lift());
       new Trigger(()-> test1.get()).whileActiveOnce(new instantCommand(()->{})); // TODO: add a real command here
   }
}