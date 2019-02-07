package com.eightycats.litterbox.state;

/**
 * @author Matt
 *
 */
public class Action
{

   private String name;

   public Action(String name)
   {
      this.name = name;
   }

   public void perform( State currentState )
   {
   }

   /**
    * @return Returns the name.
    */
   public String getName()
   {
      return name;
   }

   public String toString()
   {
      return getName();
   }

}
