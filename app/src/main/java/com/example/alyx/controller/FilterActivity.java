package com.example.alyx.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.alyx.model.Filter;
import com.example.alyx.model.Model;
import com.example.alyx.server.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import server.model.Event;
import server.model.Person;
import server.model.Searchable;

/**
 * Created by Alyx on 4/13/17.
 */

public class FilterActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Adapter adapter;
    private Filter filter = Filter.instanceOf();
    private Set<Filter.FilterOptions> filters;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        mRecyclerView = (RecyclerView) findViewById(R.id.filterOptions);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        generateView();

    }

    private void generateView(){
        filter.buildFilterOptions();
        filters = filter.getFilters();
        List<Filter.FilterOptions> listOfFilters = new ArrayList<>();
        for (Filter.FilterOptions f : filters) {
            listOfFilters.add(f);
        }

        adapter = new Adapter(listOfFilters,getBaseContext());
        mRecyclerView.setAdapter(adapter);

    }
    /**
     * Recycler view for the SearchActivity, displays the results of the Search.
     */
    private class FilterContainer extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private Filter.FilterOptions currentFilter;
        private Context context;
        private TextView mFilterResultTitle;
        private TextView mFilterResultDescription;
        private Switch mFilterSwitch;
        private RelativeLayout mContainer;

        public FilterContainer(View view, Context context){
            super(view);
            this.context = context;

            // Hook up fields in the view!
            mFilterResultTitle = (TextView) view.findViewById(R.id.resultTitle);
            mFilterResultDescription = (TextView) view.findViewById(R.id.resultDescription);
            mFilterSwitch = (Switch) view.findViewById(R.id.filterSwitch);
            mContainer = (RelativeLayout) view.findViewById(R.id.filterLayout);

        }


        /**
         *  Displays the filter in the list.
         * @param filter filter to be displayed
         */
        public void displayFilterOption(Filter.FilterOptions filter){
            currentFilter = filter;
            if(filter != null){
                // Set string values
                String eventTitle = filter.getFilterType() + " Events";
                String eventPerson = "Filter by " + filter.getFilterType() + " events";

                // Set text values
                mFilterResultTitle.setText(eventTitle);
                mFilterResultDescription.setText(eventPerson);
                mFilterSwitch.setChecked(filter.isOn());
                mFilterSwitch.setOnCheckedChangeListener(this);

            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            currentFilter.setOn(isChecked);
        }
    }

    /**
     * The adapter class for my recycler view. Takes in a list of matches form the
     * search criteria and displays them in the Context (app context).
     */
    private class Adapter extends RecyclerView.Adapter<FilterContainer> {
        /**
         * The list of filter options
         */
        private List<Filter.FilterOptions> listOfFilters;

        /**
         * The Context of the App
         */
        private Context context;


        private FilterContainer filterResultPrinter;

        /**
         * Public constructor, sets the list of matches & the app context
         */
        public Adapter(List<Filter.FilterOptions>listOfFilters, Context context) {
            this.listOfFilters = listOfFilters;
            this.context = context;
            filterResultPrinter = new FilterContainer(mRecyclerView,context);

        }

        @Override
        public FilterContainer onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate layout and return the view
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.item_filter, parent, false);
            return new FilterContainer(view, this.context);
        }

        @Override
        public void onBindViewHolder(FilterContainer holder, int position) {
            // AT this position, DISPLAY this data
            Filter.FilterOptions filterOption = listOfFilters.get(position);
            holder.displayFilterOption(filterOption);
        }

        @Override
        public int getItemCount() {
            // filters == items
            return filters.size();
        }
    }
}
