package br.com.sudoku.services;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class NotifierService {
    private final Map<EventEnum, List<EventListener>> listeners = new HashMap<>(){{
        put(EventEnum.CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscribe(final EventEnum eventType, EventListener listener){
        var selectedListeners = listeners.get(eventType);
        selectedListeners.add(listener);
    }

    public void notify(final EventEnum eventType){
        listeners.get(eventType).forEach(l -> l.update(eventType));
    }
}
