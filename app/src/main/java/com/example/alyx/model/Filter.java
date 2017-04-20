package com.example.alyx.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import server.model.Event;

/**
 * Created by Alyx on 4/14/17.
 */

public class Filter {

    private Model model = Model.instanceOf();
    private Set<FilterOptions> filters = new TreeSet<>();
    private Map<String,Set<Event>> eventsByType = new HashMap<>();

    public static Filter instanceOf(){
        if(_instance == null){
            _instance = new Filter();
        }
        return _instance;
    }

    private static Filter _instance;

    private Filter(){
        buildFilterOptions();
    }

    public Set<FilterOptions> getFilters() {
        return filters;
    }

    public void buildFilterOptions() {
        Event[] events = model.getEvents();
        // Wipe old filter options
        filters = new TreeSet<>();

        // Add all new event type filter options
        if(events != null){
            for (Event e : events){
                // Filter for Event Types
                FilterOptions newOption = new FilterOptions(e.getEventType().toLowerCase());
                filters.add(newOption);

                // Store by event type
                String eventType = e.getEventType();
                if (eventsByType.containsKey(eventType)) {
                    eventsByType.get(eventType).add(e);
                } else {
                    eventsByType.put(eventType, new TreeSet<Event>());
                    eventsByType.get(eventType).add(e);
                }
            }
        }

        // Add gender and family tree side filter options
        filters.add(new FilterOptions("Male"));
        filters.add(new FilterOptions("Female"));
        filters.add(new FilterOptions("Father's Side"));
        filters.add(new FilterOptions("Mother's Side"));
    }

    public class FilterOptions implements Comparable {
        private String filterType;

        public String getFilterType() {
            return filterType;
        }

        public void setFilterType(String filterType) {
            this.filterType = filterType;
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        private boolean on;

        public FilterOptions(String filterType){
            this.filterType = filterType;
            this.on = true;
        }

        @Override
        public int compareTo(Object o) {
            // Check to see if its the same type
            if(o == null){
                return 0;
            }
            if(o == this){
                return 0;
            }
            if(o.getClass() != this.getClass()){
                return 0;
            }

            // Cast
            FilterOptions filterType = (FilterOptions) o;

            // Compare the Strings in FilterType (alphabetical)
            return this.getFilterType().compareTo(filterType.getFilterType());
        }
    }

}
