package com.example.alyx.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alyx.model.Model;
import com.example.alyx.server.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import server.model.Person;
import server.model.Event;
import server.model.Searchable;
import server.proxy.ServerProxy;
import server.result.PersonResult;

/**
 * Created by Alyx on 4/13/17.
 */

public class PersonActivity extends FragmentActivity {

    // Person data (id, family members)
    private String personID;

    public Person getPerson() {
        return person;
    }

    private Person person = null;
    private Person mother;
    private Person father;
    private Person spouse;

    // UI Name info
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;

    private Toolbar toolbar;


    /** Access the model */
    private Model model = Model.instanceOf();

    /** Recycler view fields */
    private List<String> mHeaderList = new ArrayList<>();
    private List<Searchable> mEventList = new ArrayList<>();
    private List<Searchable> mRelationsList = new ArrayList<>();
    private Map<String,List<Searchable>> mExpandableItemsMap = new HashMap<>();
    private MenuItem mAllTheWayUpButton;

    private ExpandableListView mExpandableListView;


    /** Holds the events that belong to THIS PERSON */
    private Set<Event> events = new TreeSet<>();

    private int familyCount = 0;

    private ExpandableListAdapter expandableListAdapter = null;
    private void familyCheckedFor(){
        mExpandableItemsMap = new TreeMap<>();
        mExpandableItemsMap.put("LIFE EVENTS", mEventList);
        mExpandableItemsMap.put("FAMILY", mRelationsList);

        mExpandableListView = (ExpandableListView) findViewById(R.id.resultDetails);
        expandableListAdapter = new ExpandableListAdapter(this,mHeaderList,mExpandableItemsMap);
        mExpandableListView.setAdapter(expandableListAdapter);
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (expandableListAdapter != null) {
                    if (groupPosition == 0) {
                        Event event = (Event) expandableListAdapter.getChild(groupPosition, childPosition);
                        toMapActivity(event);
                    } else {
                        Person person = (Person) expandableListAdapter.getChild(groupPosition, childPosition);
                        toPersonActivity(person.getPersonID());
                    }
                    return true;
                }
                return false;
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Family Map: Person Details");
        toolbar.setTitleTextColor(Color.WHITE);
        if(model.isInflate()) {
            toolbar.inflateMenu(R.menu.menu_maps);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mapUpButton:
                        model.setInflate(true);
                        up();
                        return true;


                }
                //If above criteria does not meet then default is false;
                return false;
            }
        });

        // Get personID
        personID = getIntent().getStringExtra("personID");

        // Get person from the personID
        new PersonInfo().execute(personID);

        // Get this person's events
        if(model.getEventsOf(personID) != null){
            events = model.getEventsOf(personID);
            model.setInflate(true);
        }

        // Hook up UI text views and set the name values to this person's!
        mFirstName = (TextView) findViewById (R.id.firstNameVariable);
        mLastName = (TextView) findViewById (R.id.lastNameVariable);
        mGender = (TextView) findViewById (R.id.genderVariable);
        mFirstName.setText("");
        mLastName.setText("");
        mGender.setText("");

        mHeaderList = new ArrayList<>();
        mHeaderList.add("LIFE EVENTS");
        mHeaderList.add("FAMILY");

        mEventList = new ArrayList<>();
        mRelationsList = new ArrayList<>();
        for (Event e : events){
            mEventList.add(e);
        }

    }

    private void toMapActivity(Event event){
        model.setInflate(false);
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        model.setEventSelected(event);
        startActivity(intent);
    }

    private void up(){
        model.setEventSelected(null);
        Intent intent = new Intent(PersonActivity.this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void toPersonActivity(String personID){
        Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
        intent.putExtra("personID",personID);
        startActivity(intent);
    }
    /**
     * Customizes this activity to the given person, and adds parent and spouse info.
     * @param p person for Activity, or spouse/mother/father
     */
    private void setPerson(Person p){
        // If the person for this activity hasn't been set... set it to this person!
        if(person == null) {
            person = p;

            // Set name
            mFirstName.setText(person.getFirstName());
            mLastName.setText(person.getLastName());

            // Set gender
            if (person.getGender().equals("m")) {
                mGender.setText("Male");
            } else if (person.getGender().equals("f")) {
                mGender.setText("Female");
            } else {
                mGender.setText(person.getGender());
            }

            // Get their family members
            if(person.getMother() != null){
                new PersonInfo().execute(person.getMother());
            }
            if(person.getSpouse() != null){
                new PersonInfo().execute(person.getSpouse());
            }
            if(person.getFather() != null){
                new PersonInfo().execute(person.getFather());
            }


        } else {
            // If we've got the initial person, this must be one of the family members....

            // If the ID belongs to the mother, this must be the mother! Set it up.
            if(person.getMother() != null && person.getMother().equals(p.getPersonID())){
                mother = p;
                mRelationsList.add(mother);

            // If the ID belongs to the father, this must be the father! Set it up.
            } else if (person.getFather() != null && person.getFather().equals(p.getPersonID())){
                father = p;
                mRelationsList.add(father);

                // If the ID belongs to the spouse, this must be the spouse! Set it up.
            } else if(person.getSpouse() != null && person.getSpouse().equals(p.getPersonID())){
                spouse = p;
                mRelationsList.add(spouse);
            }

        }

        if((person.getMother() == null && mother == null) || (person.getMother() != null && mother != null)){
            if((person.getFather() == null && father == null) || (person.getFather() != null && father != null)){
                if((person.getSpouse() == null && spouse == null) || (person.getSpouse() != null && spouse != null)){
                    familyCheckedFor();
                }
            }
        }

    }

    /**
     * Prints toast.
     * @param toast the message to print
     */
    public void printToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieves the person from the personID given
     */
    private class PersonInfo extends AsyncTask<String, Integer, Person> {
        /** Person result to store info from the database */
        private PersonResult personResult = new PersonResult();

        /** Database accessor */
        private ServerProxy mProxy = ServerProxy.server();

        /** PersonID (person to find) */
        private String personID;

        protected Person doInBackground(String... urls) {
            // Get event ID
            this.personID = urls[0];


            // Get person by id
            personResult = mProxy.person(personID);

            // If no error message, return the person!
            if(personResult.getMessage() == null || personResult.getMessage().equals("")) {
                return personResult.getPerson();
            }

            // if error message, return null
            else {
                return null;
            }

        }

        protected void onProgressUpdate(Integer... progress) {
            // progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Person success) {
            if(success != null){
                setPerson(success);
            } else {
                printToast(personResult.getMessage());
            }
        }
    }
    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> headers = new ArrayList<>();
        private Map<String, List<Searchable>> listItems = new TreeMap<>();

        public ExpandableListAdapter (Context context, List<String> headers, Map<String, List<Searchable>> listItems) {
            this.context = context;
            this.headers = headers;
            this.listItems = listItems;
        }

        @Override
        public int getGroupCount() {
            if(headers != null) {
                return headers.size();
            }
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if(listItems.get(headers.get(groupPosition)) != null) {
                int temp = listItems.get(headers.get(groupPosition)).size();
                return temp;
            } else {
                printToast("Headers are null - position " + groupPosition);
                return 0;
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return headers.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listItems.get(headers.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String listTitle = (String) getGroup(groupPosition);
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list, null);
            }
            TextView listHeader = (TextView) convertView.findViewById(R.id.listHeader);
            listHeader.setText(listTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_and_person, null);

            TextView resultTitle = (TextView) convertView.findViewById(R.id.resultTitle);
            TextView resultDescription = (TextView) convertView.findViewById(R.id.resultDescription);
            ImageView icon = (ImageView) convertView.findViewById(R.id.resultIcon);

            if(groupPosition == 0){
                Event event = (Event) getChild(groupPosition,childPosition);
                String title = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
                resultTitle.setText(title);
                String person = "";
                if(getPerson() != null) {
                    person = getPerson().getFirstName() + " " + getPerson().getLastName();
                }
                resultDescription.setText('\n' + person);
                icon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_map_marker).colorRes(R.color.colorPrimaryDark));
            } else {
                Person person = (Person) getChild(groupPosition,childPosition);
                String personName = person.getFirstName() + " " + person.getLastName();
                resultTitle.setText(personName);
                if(person.getGender().equals("m")){
                    icon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_male).colorRes(R.color.colorPrimary));
                } else {
                    icon.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_female).colorRes(R.color.colorAccent));
                }
                if(PersonActivity.this.person.getMother() != null && person.getPersonID().equals(PersonActivity.this.person.getMother())){
                    resultDescription.setText('\n' + "Mother");
                } else if (PersonActivity.this.person.getFather() != null && person.getPersonID().equals(PersonActivity.this.person.getFather())){
                    resultDescription.setText('\n' + "Father");
                } else {
                    resultDescription.setText('\n' + "Spouse");
                }
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            notifyDataSetInvalidated();
        }
    }
}