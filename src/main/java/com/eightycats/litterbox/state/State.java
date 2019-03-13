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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State {

    private String name;

    private Map<Action, State> actions = new HashMap();

    public State(String name) {
        this.name = name;
    }

    public void addAction(Action action, State next) {
        actions.put(action, next);
    }

    public Set<Action> getActions() {
        return Collections.unmodifiableSet(actions.keySet());
    }

    public State perform(Action action) {
        action.perform(this);

        State state = actions.get(action);
        if (state == null) {
            state = this;
        }
        return state;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
