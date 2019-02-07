package com.eightycats.litterbox.state;

import java.util.*;

/**
 *
 * @author Matt
 */
public class State
{

   private String name;

   private Map actions = new Hashtable();

   public State(String name)
   {
      this.name = name;
   }

   public void addAction( Action action, State next )
   {
      actions.put( action, next );
   }

   public Action[] getActions()
   {

      Action[] array = new Action[ actions.size() ];
      return (Action[]) actions.keySet().toArray( array );

   }

   public State perform(Action action)
   {
      action.perform( this );

      State state = (State) actions.get( action );

      if( state == null )
      {
         state = this;
      }

      return state;
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
