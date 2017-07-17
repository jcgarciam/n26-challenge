package com.jcgarciam.n26.repositories;

import com.jcgarciam.n26.domain.StatisticCounter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

/**
 * InMemoryStorage is just a wrapper of a ConcurrentSkipListMap in order to provide a component which can be
 * inject and act as a Singleton storage, it provides forward declaration for only the required API.
 */
@Component
@ApplicationScope
public class InMemoryStorage {
    private final ConcurrentSkipListMap<Long, StatisticCounter> storage;

    public InMemoryStorage() {
        storage = new ConcurrentSkipListMap<>();
    }

    public StatisticCounter computeIfAbsent(final Long key,
                                final Function<? super Long, ? extends StatisticCounter> mappingFunction){
        return storage.computeIfAbsent(key, mappingFunction);
    }

    public ConcurrentNavigableMap<Long, StatisticCounter> tailMap(final Long key){
        return storage.tailMap(key);
    }

    public NavigableSet<Long> keySet(){
        return storage.keySet();
    }

    public int size(){
        return storage.size();
    }

}
