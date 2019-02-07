/**
 *
 */
package com.eightycats.litterbox.state;

import java.util.EventObject;

/**
 * @author Matt
 *
 */
public class StateChangeEvent extends EventObject
{

   /**
    *
    */
   private static final long serialVersionUID = 2352910892232949211L;

   private State oldState;

   private State newState;

   private Action action;

   /**
    * @param source
    */
   public StateChangeEvent( Object source,
                            State oldState,
                            Action action,
                            State newState )
   {
      super(source);

      this.oldState = oldState;
      this.newState = newState;
      this.action = action;

   }

   /**
    * @return Returns the action.
    */
   public Action getAction()
   {
      return action;
   }

   /**
    * @return Returns the newState.
    */
   public State getNewState()
   {
      return newState;
   }

   /**
    * @return Returns the oldState.
    */
   public State getOldState()
   {
      return oldState;
   }


}
