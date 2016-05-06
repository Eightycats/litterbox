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
