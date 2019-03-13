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

import java.util.EventObject;

public class StateChangeEvent extends EventObject {
    private static final long serialVersionUID = 2352910892232949211L;

    private State oldState;

    private State newState;

    private Action action;

    public StateChangeEvent(Object source, State oldState, Action action, State newState) {
        super(source);

        this.oldState = oldState;
        this.newState = newState;
        this.action = action;

    }

    public Action getAction() {
        return action;
    }

    public State getNewState() {
        return newState;
    }

    public State getOldState() {
        return oldState;
    }
}
