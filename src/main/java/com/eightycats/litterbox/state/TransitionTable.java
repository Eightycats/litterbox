/**
 *
 */
package com.eightycats.litterbox.state;

import java.util.Hashtable;
import java.util.Map;

/**
 * @author Matt
 *
 */
public class TransitionTable
{
   public Map transitionTable = new Hashtable();

   private String currentState;

   public TransitionTable( String initialState )
   {
      currentState = initialState;
   }

   public String getCurrentState()
   {
      return currentState;
   }

   public void performAction( String action )
   {
      Map actionMap = getActionMap( currentState );
      String nextState = (String) actionMap.get( action );

      if( nextState != null )
      {
         currentState = nextState;
      }

   }

   public void addTransition( String oldState, String action, String newState )
   {
      Map actionMap = getActionMap( oldState );

      actionMap.put( action, newState );

   }

   public String[] getActions( String state )
   {
      Map actionMap = getActionMap(state);
      return (String[]) actionMap.keySet().toArray( new String[actionMap.size()] );
   }

   protected Map getActionMap( String state )
   {
      Map actionMap;

      synchronized( transitionTable )
      {
         actionMap = (Map) transitionTable.get( state );

         if( actionMap == null )
         {
            actionMap = new Hashtable();
            transitionTable.put( state, actionMap );
         }

      }

      return actionMap;

   }

}
