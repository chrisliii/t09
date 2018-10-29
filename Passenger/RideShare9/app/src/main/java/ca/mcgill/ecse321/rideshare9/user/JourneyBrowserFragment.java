package ca.mcgill.ecse321.rideshare9.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ca.mcgill.ecse321.rideshare9.HttpUtils;
import ca.mcgill.ecse321.rideshare9.R;
import cz.msebera.android.httpclient.Header;

import static android.support.constraint.Constraints.TAG;


public class JourneyBrowserFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private final static List<Advertisement> advertisements = new ArrayList<>();
    private RecyclerView rvAdvertisements;
    private AdvertisementsAdapter advertisementsAdapter;
    private LinearLayoutManager layoutManager;

    public JourneyBrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getAvailableTrips() {

        //  Get SharedPreferences which holds the JWT Token
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String authentication = "Bearer " + sharedPreferences.getString("token", null);

        //  Set headers for the request
        HttpUtils.addHeader("Authorization", authentication);

        HttpUtils.get("adv/get-list-adv", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                advertisements.addAll(advertisementsFromJSONArray(response));

                for (int i = 0; i < advertisements.size(); i++) {
                    for (int j = 0; j < advertisements.get(i).getStops().size(); j++) {
                        final int finalI = i;
                        final int finalJ = j;
                        HttpUtils.get("/stop/get-by-id/" + advertisements.get(i).getStops().get(j).getId(),
                                null, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        advertisements.get(finalI).getStops().get(finalJ)
                                                .setName(response.optString("stopName"));
                                        advertisements.get(finalI).getStops().get(finalJ)
                                                .setPrice((float) response.optDouble("price"));
                                        advertisementsAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }
            }
        });


    }

    private List<Advertisement> advertisementsFromJSONArray(JSONArray jsonAdArray) {
        int adCount = jsonAdArray.length();
        List<Advertisement> advertisements = new ArrayList<>();

        for (int i = 0; i < adCount; i++) {
            JSONObject advertisement = jsonAdArray.optJSONObject(i);
            advertisements.add(advertisementFromJSONObject(advertisement));
        }

        return advertisements;
    }

    private Advertisement advertisementFromJSONObject(JSONObject jsonAdObject) {
        int adId = jsonAdObject.optInt("id");
        int adSeatsAvailable = jsonAdObject.optInt("seatAvailable");
        int adVehicleId = jsonAdObject.optInt("vehicle");
        int adDriverId = jsonAdObject.optInt("driver");
        String adTitle = jsonAdObject.optString("title");
        String adStartTime = jsonAdObject.optString("startTime");
        String adStartLocation = jsonAdObject.optString("startLocation");
        String adStatus = jsonAdObject.optString("status");
        List<Stop> adStops = new ArrayList<>();

        JSONArray stops = jsonAdObject.optJSONArray("stops");

        //  Get the number of stops for the advertisement
        int stopCount = stops.length();

        for (int j = 0; j < stopCount; j++) {
            Stop newStop = new Stop();
            // Only the id is set for now
            newStop.setId(stops.optLong(j));
            adStops.add(newStop);
        }

        return new Advertisement(adId, adSeatsAvailable, adVehicleId, adDriverId, adTitle,
                adStartTime, adStartLocation, adStatus, adStops);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journey_browser, container, false);
        rvAdvertisements = view.findViewById(R.id.rvAdvertisements);
        rvAdvertisements.setLayoutManager(new LinearLayoutManager(getContext()));
        advertisementsAdapter = new AdvertisementsAdapter(advertisements);
        rvAdvertisements.setAdapter(advertisementsAdapter);

        getAvailableTrips();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
}