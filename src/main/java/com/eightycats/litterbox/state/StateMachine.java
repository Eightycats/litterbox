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

import java.util.Set;

public class StateMachine {
    private State currentState;

    public StateMachine(State initialState) {
        currentState = initialState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void perform(Action action) {
        currentState = currentState.perform(action);
    }

    public Set<Action> getActions() {
        return getCurrentState().getActions();
    }

    public String toString() {
        return getCurrentState().getName();
    }
}
