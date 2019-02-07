package com.eightycats.litterbox.state;

/**
 * @author Matt
 *
 */
public class StateMachine
{

   private State currentState;

   public StateMachine( State initialState )
   {
      currentState = initialState;
   }

   public State getCurrentState()
   {
      return currentState;
   }

   public void perform(Action action)
   {
      currentState = currentState.perform( action );
   }

   /**
    * @see mjensen.state.IState#getActions()
    */
   public Action[] getActions()
   {
      return getCurrentState().getActions();
   }


   public String toString()
   {
      return getCurrentState().getName();
   }

}
