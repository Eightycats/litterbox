/**
 * Copyright 2016 Matthew A Jensen <eightycats@gmail.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eightycats.litterbox.state;

import java.util.Hashtable;
import java.util.Map;

public class TransitionTable {
    public Map<String, Map<String, String>> transitionTable = new Hashtable();

    private String currentState;

    public TransitionTable(String initialState) {
        currentState = initialState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void performAction(String action) {
        Map actionMap = getActionMap(currentState);
        String nextState = (String) actionMap.get(action);
        if (nextState != null) {
            currentState = nextState;
        }
    }

    public void addTransition(String oldState, String action, String newState) {
        Map actionMap = getActionMap(oldState);
        actionMap.put(action, newState);
    }

    public String[] getActions(String state) {
        Map actionMap = getActionMap(state);
        return (String[]) actionMap.keySet().toArray(new String[actionMap.size()]);
    }

    protected Map getActionMap(String state) {
        Map actionMap;
        if (!transitionTable.containsKey(state)) {
            actionMap = new Hashtable();
            transitionTable.put(state, actionMap);
        } else {
            actionMap = transitionTable.get(state);
        }
        return actionMap;
    }
}
