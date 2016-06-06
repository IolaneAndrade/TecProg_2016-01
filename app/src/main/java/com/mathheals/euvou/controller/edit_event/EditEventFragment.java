/**
 * File: EditEventFragment.java
 * Purpose: Fragment responsible for edit or delete an event
 */

package com.mathheals.euvou.controller.edit_event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mathheals.euvou.R;
import com.mathheals.euvou.controller.event_registration.LocalEventActivity;
import com.mathheals.euvou.controller.showPlaceRanking.ShowTop5Ranking;
import com.mathheals.euvou.controller.utility.EditAndRegisterUtility;
import com.mathheals.euvou.controller.utility.Mask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Vector;

import dao.CategoryDAO;
import dao.EventCategoryDAO;
import dao.EventDAO;
import exception.EventException;
import model.Event;

public class EditEventFragment extends Fragment implements View.OnClickListener {
    private int idEvent;
    private static final String SUCCESSFULL_UPDATE_MESSAGE = "Evento alterado com sucesso :)";
    private String latitude;
    private String longitude;
    private EditText nameField, dateField, hourField, descriptionField, addressField, priceDecimalField, priceRealField;
    private CheckBox showCheckBox, expositionCheckBox, cinemaCheckBox, museumCheckBox, theaterCheckBox, educationCheckBox,
                     othersCheckBox,sportsCheckBox, partyCheckBox;
    private final Vector<String> CATEGORIES = new Vector<>();
    private EditAndRegisterUtility  editAndRegisterUtility = new EditAndRegisterUtility();

    /**
     * Required constructor to instantiate a fragment object
     */
    public EditEventFragment() {

    }

    /**
     * Formats a date
     * @param jsonEvent - JSONObject with the event data
     * @throws JSONException
     */
    private void formatDate(JSONObject jsonEvent) throws JSONException {
        assert jsonEvent != null;

        String dateHourEvent = jsonEvent.getJSONObject("0").getString("dateTimeEvent");
        String[] dateHourEventSplit = dateHourEvent.split(" ");

        String dateEvent = dateHourEventSplit[0];
        String[] dateEventSplit = dateEvent.split("-");
        dateEvent = dateEventSplit[2]+"/"+dateEventSplit[1]+"/"+dateEventSplit[0];

        String hourEvent = dateHourEventSplit[1];

        this.dateField.setText(dateEvent);
        this.hourField.setText(hourEvent);
    }

    /**
     * Format a price
     * @param jsonEvent - JSONObject with the event data
     * @throws JSONException
     */
    private void formatPrice(JSONObject jsonEvent) throws JSONException {
        assert jsonEvent != null;

        Integer priceEvent = jsonEvent.getJSONObject("0").getInt("price");
        this.priceRealField.setText(Integer.toString(priceEvent/100));
        this.priceDecimalField.setText(Integer.toString(priceEvent - priceEvent / 100 * 100));
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment
     * @param inflater - Object used to inflate any views in the fragment
     * @param container - If non-null, is the parent view that the fragment should be attached to
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a
     *                           previous saved state as given here
     * @return View - View of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert inflater != null;
        assert container != null;
        assert savedInstanceState != null;

        idEvent = this.getArguments().getInt("idEvent");

        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        assert dateField != null;

        settingEditText(view);
        dateField.addTextChangedListener(Mask.insert("##/##/####", dateField));
        settingCheckBoxes(view);

        showEventDataToEdit();

        //Adding listener to eventLocal EditText
        Button eventLocal = (Button) view.findViewById(R.id.eventLocal);
        eventLocal.setOnClickListener(this);

        //Adding listener to CheckBoxs to verify if each CheckBox is checked or not
        addCheckBoxListeners(view);

        Button removeEvent = (Button)view.findViewById(R.id.removeEvent);
        removeEvent.setOnClickListener(this);

        Button updateEvent = (Button)view.findViewById(R.id.updateEvent);
        updateEvent.setOnClickListener(this);

        return view;
    }

    /**
     * Gets the data of an event at database and show on the view to be edited
     */
    public void showEventDataToEdit(){

        EventDAO eventDAO = new EventDAO(getActivity());
        EventCategoryDAO eventCategoryDAO = new EventCategoryDAO(getActivity());

        //Change the value of idEvent when the consultEvent was finished
        JSONObject jsonEvent = eventDAO.searchEventById(idEvent);
        JSONObject jsonEventCategory = eventCategoryDAO.searchCategoriesByEventId(idEvent);

        assert jsonEvent != null;
        assert jsonEventCategory != null;

        try {
            String nameEvent = jsonEvent.getJSONObject("0").getString("nameEvent");
            nameField.setText(nameEvent);

            String descriptionEvent = jsonEvent.getJSONObject("0").getString("description");
            descriptionField.setText(descriptionEvent);

            String addressEvent = jsonEvent.getJSONObject("0").getString("address");
            addressField.setText(addressEvent);

            formatDate(jsonEvent);
            formatPrice(jsonEvent);

            latitude = jsonEvent.getJSONObject("0").getString("latitude");
            longitude = jsonEvent.getJSONObject("0").getString("longitude");

            Vector <Integer> idCategories = new Vector<>();
            String idCategory;
            for(int i=0; i<jsonEventCategory.length(); i++){
                idCategory=jsonEventCategory.getJSONObject(Integer.toString(i)).getString("idCategory");
                idCategories.add(Integer.parseInt(idCategory));
            }

            defineEventCategory(idCategories);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Define de category of an event, based on the choice of the user
     * @param idCategories - The id of the category of the event in the vector of categories
     * @throws JSONException -
     */
    public void defineEventCategory(Vector <Integer> idCategories) throws JSONException {
        for (int i = 0; i < idCategories.size(); i++) {

            CategoryDAO categoryDAO = new CategoryDAO(getActivity());

            assert categoryDAO != null;

            JSONObject jsonCategory = categoryDAO.searchCategoryById(idCategories.get(i));
            String nameCategory = jsonCategory.getJSONObject("0").getString("nameCategory");

            switch (nameCategory) {
                case "Show":
                    showCheckBox.setChecked(true);
                    CATEGORIES.add("Show");
                    break;
                case "Teatro":
                    theaterCheckBox.setChecked(true);
                    CATEGORIES.add("Teatro");
                    break;
                case "Cinema":
                    cinemaCheckBox.setChecked(true);
                    CATEGORIES.add("Cinema");
                    break;
                case "Balada":
                    partyCheckBox.setChecked(true);
                    CATEGORIES.add("Balada");
                    break;
                case "Museu":
                    museumCheckBox.setChecked(true);
                    CATEGORIES.add("Museu");
                    break;
                case "Educacao":
                    educationCheckBox.setChecked(true);
                    CATEGORIES.add("Educacao");
                    break;
                case "Exposicao":
                    expositionCheckBox.setChecked(true);
                    CATEGORIES.add("Exposicao");
                    break;
                case "Esporte":
                    sportsCheckBox.setChecked(true);
                    CATEGORIES.add("Esporte");
                    break;
                case "Outros":
                    othersCheckBox.setChecked(true);
                    break;
            }
        }
    }

        /**
         * Sets the EditTexts of the EditEventFragment view
         * @param view - Current view being used in the fragment
         */
    private void settingEditText(View view){
        assert view != null;

        this.nameField = (EditText) view.findViewById(R.id.eventName);
        this.dateField = (EditText) view.findViewById(R.id.eventDate);
        this.hourField = (EditText) view.findViewById(R.id.eventHour);
        this.descriptionField = (EditText) view.findViewById(R.id.eventDescription);
        this.priceRealField = (EditText) view.findViewById(R.id.eventPriceReal);
        this.priceDecimalField = (EditText) view.findViewById(R.id.eventPriceDecimal);
        this.addressField = (EditText) view.findViewById(R.id.eventAddress);
    }

    /**
     * Sets the CheckBoxes of
     * @param view - Current view being used in the fragment
     */
    private void settingCheckBoxes(View view){
        assert view != null;

        this.showCheckBox = (CheckBox) view.findViewById(R.id.optionShow);
        this.expositionCheckBox = (CheckBox) view.findViewById(R.id.optionExposition);
        this.cinemaCheckBox = (CheckBox) view.findViewById(R.id.optionCinema);
        this.theaterCheckBox = (CheckBox) view.findViewById(R.id.optionTheater);
        this.partyCheckBox = (CheckBox) view.findViewById(R.id.optionParty);
        this.educationCheckBox = (CheckBox) view.findViewById(R.id.optionEducation);
        this.museumCheckBox = (CheckBox) view.findViewById(R.id.optionMuseum);
        this.sportsCheckBox = (CheckBox) view.findViewById(R.id.optionSports);
        this.othersCheckBox = (CheckBox) view.findViewById(R.id.optionOthers);
    }

    /**
     * Updates an event on the DataBase
     * @param event - Object with event data
     */
    private void updateEventOnDataBase(Event event){
        assert event != null;

        EventDAO eventDAO = new EventDAO(getActivity());
        eventDAO.updateEvent(event);
    }

    /**
     * Creates a object with event data and, in case of success, calls the method which updates the
     * event on the DataBase. In case of failure,throws an EventException
     */
    private void updateEvent(){
        String nameEvent = nameField.getText().toString();
        String dateEvent = dateField.getText().toString();

        String[] dateEventSplit = dateEvent.split("/");
        dateEvent = dateEventSplit[2]+"-"+dateEventSplit[1]+"-"+dateEventSplit[0];

        String hourEvent = hourField.getText().toString();

        String dateHourEvent = dateEvent + " " + hourEvent;

        String descriptionEvent = descriptionField.getText().toString();

        String addressEvent = addressField.getText().toString();

        Integer eventPriceReal = Integer.parseInt(priceRealField.getText().toString());
        Integer eventPriceDecimal = Integer.parseInt(priceDecimalField.getText().toString());
        Integer priceEvent = eventPriceReal * 100 + eventPriceDecimal;

        try {
            Event event = new Event(idEvent, nameEvent, priceEvent, addressEvent, dateHourEvent,
                    descriptionEvent, latitude, longitude, CATEGORIES);

            updateEventOnDataBase(event);

            Toast.makeText(getActivity().getBaseContext(), SUCCESSFULL_UPDATE_MESSAGE, Toast.LENGTH_LONG).show();
        } catch (EventException e){
            String message = e.getMessage();

            //Verify address field
            if(message.equals(Event.ADDRESS_IS_EMPTY)){
                editAndRegisterUtility.setMessageError(addressField, message);
            }else{
                // Nothing to do
            }

            if(message.equals(Event.DESCRIPTION_CANT_BE_EMPTY)){
                editAndRegisterUtility.setMessageError(descriptionField, message);
            }else{
                // Nothing to do
            }

            if(message.equals(Event.DESCRIPTION_CANT_BE_GREATER_THAN)){
                editAndRegisterUtility.setMessageError(descriptionField, message);
            }else{
                // Nothing to do
            }

            if(message.equals(Event.EVENT_DATE_IS_EMPTY)){
                editAndRegisterUtility.setMessageError(dateField, message);
            }else{
                // Nothing to do
            }

            if(message.equals(Event.EVENT_NAME_CANT_BE_EMPTY_NAME)){
                editAndRegisterUtility.setMessageError(nameField, message);
            }else{
                // Nothing to do
            }

            if(message.equals(Event.INVALID_EVENT_DATE)){
                editAndRegisterUtility.setMessageError(dateField, message);
            }else{
                // Nothing to do
            }

            if(message.equals(Event.NAME_CANT_BE_GREATER_THAN_50)){
                editAndRegisterUtility.setMessageError(nameField, message);
            }else{
                // Nothing to do
            }
        } catch (ParseException e){
            e.printStackTrace();

        }
    }


    /**
     * Adds the names of the categories which were clicked in a vector of strings
     * @param v - Current view being used in the fragment
     */
    private void addEventCategories(View v){
        assert v != null;

        if(v.getId() == R.id.optionCinema){
            CheckBox cinemaCheckBox = (CheckBox) v;

            if(cinemaCheckBox.isChecked()) {
                CATEGORIES.add(cinemaCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(cinemaCheckBox.getText().toString());
            }
        }else if(v.getId() == R.id.optionEducation) {
            CheckBox educationCheckBox = (CheckBox) v;

            if(educationCheckBox.isChecked()) {
                CATEGORIES.add("Educacao");
            }else{
                CATEGORIES.remove("Educacao");
            }
        }else if(v.getId() == R.id.optionExposition){
            CheckBox expositionCheckBox = (CheckBox) v;

            if(expositionCheckBox.isChecked()) {
                CATEGORIES.add("Exposicao");
            }else{
                CATEGORIES.remove("Exposicao");
            }
        }else if(v.getId() == R.id.optionMuseum){
            CheckBox museumCheckBox = (CheckBox) v;

            if(museumCheckBox.isChecked()) {
                CATEGORIES.add(museumCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(museumCheckBox.getText().toString());
            }
        }else if(v.getId() == R.id.optionOthers){
            CheckBox othersCheckBox = (CheckBox) v;

            if(othersCheckBox.isChecked()) {
                CATEGORIES.add(othersCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(othersCheckBox.getText().toString());
            }
        }else if(v.getId() == R.id.optionParty){
            CheckBox partyCheckBox = (CheckBox) v;

            if(partyCheckBox.isChecked()) {
                CATEGORIES.add(partyCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(partyCheckBox.getText().toString());
            }
        }else if(v.getId() == R.id.optionShow){
            CheckBox showCheckBox = (CheckBox) v;

            if(showCheckBox.isChecked()) {
                CATEGORIES.add(showCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(showCheckBox.getText().toString());
            }
        }else if(v.getId() == R.id.optionSports){
            CheckBox sportsCheckBox = (CheckBox) v;

            if(sportsCheckBox.isChecked()) {
                CATEGORIES.add(sportsCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(sportsCheckBox.getText().toString());
            }
        }else if(v.getId() == R.id.optionTheater){
            CheckBox theaterCheckBox = (CheckBox) v;

            if(theaterCheckBox.isChecked()) {
                CATEGORIES.add(theaterCheckBox.getText().toString());
            }else{
                CATEGORIES.remove(theaterCheckBox.getText().toString());
            }
        }else{
            // Nothing to do
        }
    }

    /**
     * Updates the event data when the user clicks on the update button, deletes the event when
     * the user clicks on the remove button or calls addEventCategories when the user clicks on
     * a new category
     * @param v Current view being used in the fragment
     */
    @Override
    public void onClick(View v){
        assert v != null;

        if(v.getId() == R.id.updateEvent){
            updateEvent();
        }
        else if(v.getId() == R.id.removeEvent){
            removeEvent(idEvent);
        }
        else if(v.getId() == R.id.eventLocal){
            Intent map = new Intent(getActivity(), LocalEventActivity.class);
            startActivityForResult(map, 2);
        }else{
            addEventCategories(v);
        }
    }

    /**
     * Called when an activity which was launched exits
     * @param requestCode - Request code which the activity started with
     * @param resultCode - Result code which was returned
     * @param data - Additional data which the activity may contain
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        assert data != null;

        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case (2) : {
                if (resultCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    latitude = bundle.getString("latitude");
                    longitude = bundle.getString("longitude");

                    Toast.makeText(getContext(), "Local selecionado com sucesso", Toast.LENGTH_LONG).show();
                }else{
                    // Nothing to do
                }
                break;
            }
        }
    }

    /**
     * Adds listeners of the checkboxes
     * @param v - Current view being used in the fragment
     */
    private void addCheckBoxListeners(View v){
        assert v != null;

        CheckBox showCategory = (CheckBox) v.findViewById(R.id.optionShow);
        assert showCategory != null;
        showCategory.setOnClickListener(this);

        CheckBox expositionCategory = (CheckBox) v.findViewById(R.id.optionExposition);
        assert expositionCategory != null;
        expositionCategory.setOnClickListener(this);

        CheckBox museumCategory = (CheckBox) v.findViewById(R.id.optionMuseum);
        assert museumCategory != null;
        museumCategory.setOnClickListener(this);

        CheckBox cinemaCategory = (CheckBox) v.findViewById(R.id.optionCinema);
        assert  cinemaCategory != null;
        cinemaCategory.setOnClickListener(this);

        CheckBox theaterCategory = (CheckBox) v.findViewById(R.id.optionTheater);
        assert theaterCategory != null;
        theaterCategory.setOnClickListener(this);

        CheckBox partyCategory = (CheckBox) v.findViewById(R.id.optionParty);
        assert partyCategory != null;
        partyCategory.setOnClickListener(this);

        CheckBox educationCategory = (CheckBox) v.findViewById(R.id.optionEducation);
        assert educationCategory != null;
        educationCategory.setOnClickListener(this);

        CheckBox sportsCategory = (CheckBox) v.findViewById(R.id.optionSports);
        assert sportsCategory != null;
        sportsCategory.setOnClickListener(this);

        CheckBox othersCategory = (CheckBox) v.findViewById(R.id.optionOthers);
        assert othersCategory != null;
        othersCategory.setOnClickListener(this);
    }

    /**
     * Removes the event selected by the user
     * @param eventId - ID of the event selected by the user
     */
    private void removeEvent(int eventId){
        assert eventId >= 0;
        assert eventId <= Integer.MAX_VALUE;

        EventDAO eventDAO = new EventDAO(getActivity());
        assert eventDAO != null;

        if(eventDAO.deleteEvent(eventId).contains("Salvo")){
            Toast.makeText(getActivity(), "Deletado com sucesso", Toast.LENGTH_LONG).show();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new ShowTop5Ranking());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            Toast.makeText(getActivity(), "Houve um erro", Toast.LENGTH_LONG).show();
        }
    }
}