/**
 * Copyright 2016 Matthew A Jensen <eightycats@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.eightycats.litterbox.event;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Support for adding listeners and firing off events.
 */
public abstract class EventSupportBase<S, L, E>
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected List<L> _listeners = new CopyOnWriteArrayList<L>();

    protected S _source;

    public EventSupportBase ()
    {
        this(null);
    }

    public EventSupportBase (S source)
    {
        _source = source;
    }

    protected S getSource ()
    {
        return _source;
    }

    public void addListener (L listener)
    {
        _listeners.add(listener);
    }

    public void removeListener (L listener)
    {
        _listeners.remove(listener);
    }

    public void fireEvent (E event)
    {
        for (L listener : _listeners) {
            publishEvent(listener, event);
        }
    }

    public int getListenerCount ()
    {
        return _listeners.size();
    }

    public boolean hasListeners ()
    {
        return getListenerCount() > 0;
    }

    protected abstract void publishEvent (L listener, E event);
}
