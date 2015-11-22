package com.mathheals.euvou.controller.showPlaceRanking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mathheals.euvou.R;
import com.mathheals.euvou.controller.show_place.ShowPlaceInfo;
import com.mathheals.euvou.controller.utility.LoginUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import dao.PlaceDAO;
import exception.PlaceException;
import model.Place;

public class ShowPlaceRank extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private JSONObject result;
    private ArrayList<Place> places;


    public ShowPlaceRank() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.fragment_show_place_rank, container, false);
        // Inflate the layout for this fragment
        listView = (ListView) vw.findViewById(R.id.listViewPlaces);
        listView.setOnItemClickListener(this);
        fillList();
        return  vw;
    }
    private void fillList() {
        try {
            int id = (new LoginUtility(getActivity())).getUserId();
            result = new PlaceDAO(getActivity()).searchAllPlaces();
            //List<Map<String, String>> placeList= new ArrayList<Map<String, String>>();
           places = new ArrayList<>();
            for (int i = 0; i < result.length(); i++) {
                int idPlace = result.getJSONObject("" + i).getInt("idPlace");
                String namePlace = result.getJSONObject("" + i).getString("namePlace");
                Place aux = new Place(idPlace,
                        namePlace,
                        result.getJSONObject("" + i).getString("evaluate"),
                        result.getJSONObject("" + i).getString("longitude"),
                        result.getJSONObject("" + i).getString("latitude"),
                        result.getJSONObject("" + i).getString("operation"),
                        result.getJSONObject("" + i).getString("description"),
                        result.getJSONObject("" + i).getString("address"),
                        result.getJSONObject("" + i).getString("phonePlace")
                );
                places.add(aux);
                //placeList.add(placeRank(place.getJSONObject("" + i).getString("namePlace"), place.getJSONObject("" + i).getString("evaluate")));

            }
            PlaceAdapter placeAdapter = new PlaceAdapter(getActivity(),places);
            /*SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),placeList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"Nome","Nota"}, new int[]{android.R.id.text1});

            Toast.makeText(getActivity(), simpleAdapter.toString(), Toast.LENGTH_SHORT).show();
*/
            listView.setAdapter(placeAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PlaceException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private HashMap<String, String> placeRank(String namePlace, String evaluate) {
        HashMap<String, String> place = new HashMap<String, String>();
        place.put("Nome", namePlace);
        place.put("Nota", evaluate);
        return place;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startShowInfoActivity(position);
        /*final android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        clicado = events.get(position);
        EditOrRemoveFragment editOrRemoveFragment = new EditOrRemoveFragment();
        editOrRemoveFragment.evento = clicado;
        fragmentTransaction.replace(R.id.content_frame, editOrRemoveFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }

    private void startShowInfoActivity(int id) {
        Intent intent = new Intent(getActivity(), ShowPlaceInfo.class);
        intent.putExtras(getPlaceInfoAsBundle(id));
        startActivity(intent);
    }

    private Bundle getPlaceInfoAsBundle(int id) {
        Bundle placeInfo = new Bundle();
        Toast.makeText(getActivity(),"" + id,Toast.LENGTH_LONG);
        placeInfo.putString("name", places.get(id).getName());
        placeInfo.putString("phone", places.get(id).getPhone());
        placeInfo.putString("address", places.get(id).getAddress());
        placeInfo.putString("description", places.get(id).getDescription());
        placeInfo.putDouble("latitude", places.get(id).getLatitude());
        placeInfo.putDouble("longitude", places.get(id).getLongitude());
        placeInfo.putString("operation", places.get(id).getOperation());
        placeInfo.putInt("idPlace", places.get(id).getId());
        return placeInfo;
    }

}