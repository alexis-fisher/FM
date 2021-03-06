package com.example.alyx.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.List;

import server.model.Person;
import server.model.Searchable;
import server.model.Event;

/**
 * Created by Alyx on 4/17/17.
 */

public class SearchActivity extends AppCompatActivity{
    /** Search field - enter text here */
    private EditText mSearchField;

    /** Recycler view - holds search results */
    private RecyclerView mRecyclerView;

    /** Adapts the data to fit the recycler view */
    private SearchAdapter adapter;

    /** Access the settings & data from the database/model */
    private Model model = Model.instanceOf();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Run super on create, and load UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Wire up fields
        mSearchField = (EditText) findViewById(R.id.searchField);
        mRecyclerView = (RecyclerView) findViewById(R.id.searchList);

        // Wait for input
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){
                    search(s.toString());
                }
            }
        });

        // Set up search-er
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        search("");

    }

    private void search(String term){
        // Search for the terms!
        List<Searchable>searchedData = model.search(term);

        // Load them into the adapter & display them!
        adapter = new SearchAdapter(searchedData,getBaseContext());
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Recycler view for the SearchActivity, displays the results of the Search.
     */
    private class Results extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        private Searchable currentMatch;
        private TextView mSearchResultTitle;
        private TextView mSearchResultDescription;
        private RelativeLayout mContainer;
        private ImageView mIcon;
        private Context context;


        public Results(View view, Context context){
            super(view);
            this.context = context;

            // Hook up fields in the view!
            mSearchResultTitle = (TextView) view.findViewById(R.id.resultTitle);
            mSearchResultDescription = (TextView) view.findViewById(R.id.resultDescription);
            mContainer = (RelativeLayout) view.findViewById(R.id.searchResults_layout);
            mIcon = (ImageView) view.findViewById(R.id.resultIcon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentMatch != null){
                        if(currentMatch.getClass() == Event.class){
                            toEvent((Event) currentMatch);
                        } else if(currentMatch.getClass() == Person.class){
                            toPerson((Person) currentMatch);
                        }
                    } else {
                        printToast("Clicked on a weird thing?");
                    }
                }
            });

        }


        /**
         *  Displays the item.
         * @param match Item to be displayed
         */
        public void displayMatch(Searchable match){
            currentMatch = match;
            if(currentMatch != null){
                if(currentMatch.getClass() == Event.class){
                    // Display event
                    Event event = (Event) currentMatch;

                    // Set string values
                    String eventTitle = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
                    String eventPerson = event.getPerson();

                    // Set text values
                    mSearchResultTitle.setText(eventTitle);
                    mSearchResultDescription.setText('\n' + eventPerson);

                    // Set icon
                    mIcon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_map_marker).colorRes(R.color.colorPrimary));
                } else if(currentMatch.getClass() == Person.class){
                    // Display person
                    Person person = (Person) currentMatch;

                    // Set string values
                    String personName = person.getFirstName() + " " + person.getLastName();

                    // Set text values
                    mSearchResultTitle.setText(personName);
                    mSearchResultDescription.setText("");

                    // Set icon
                    if(person.getGender().equals("m")){
                        mIcon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_male).colorRes(R.color.colorPrimary));
                    } else {
                        mIcon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_female).colorRes(R.color.colorAccent));
                    }
                }
            }
        }
        @Override
        public void onClick(View v){
            if(currentMatch != null){
                if(currentMatch.getClass() == Event.class){
                    toEvent((Event) currentMatch);
                } else if(currentMatch.getClass() == Person.class){
                    toPerson((Person) currentMatch);
                }
            } else {
                printToast("Clicked on a weird thing?");
            }
        }

        /**
         * Prints toast.
         * @param toast the message to print
         */
        public void printToast(String toast){
            Toast.makeText(SearchActivity.this, toast, Toast.LENGTH_SHORT).show();
        }


        /**
         * Navigates to the PersonActivity when a person is clicked.
         * @param person the person that the PersonActivity is built around
         */
        private void toPerson(Person person){
            Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
            intent.putExtra("personID", person.getPersonID());
            startActivity(intent);
        }

        /**
         * Navigates to the MapActivity when an event is clicked.
         * @param event the event that should be marked & centered in the MapActivity
         */
        private void toEvent(Event event){
            model.setEventSelected(event);
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(intent);
        }

    }

    /**
     * The adapter class for my recycler view. Takes in a list of matches form the
     * search criteria and displays them in the Context (app context).
     */
     private class SearchAdapter extends RecyclerView.Adapter<Results> {
        /** The List of matches for the search term */
        private List<Searchable> data;
        private Results searchResultPrinter;

        /** The Context of the App */
        private Context context;

        /** Public constructor, sets the list of matches & the app context */
        public SearchAdapter (List<Searchable> data, Context context){
            this.data = data;
            this.context = context;
            searchResultPrinter = new Results(mRecyclerView,context);
        }

        @Override
        public Results onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate layout and return the view
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.item_search_and_person, parent, false);
            return new Results(view, this.context);
        }

        @Override
        public void onBindViewHolder(Results holder, int position) {
            // AT this position, DISPLAY this data
            Searchable currentMatch = data.get(position);
            holder.displayMatch(currentMatch);
        }

        @Override
        public int getItemCount() {
            // Data == items
            return data.size();
        }
    }

}
