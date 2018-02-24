package prin366_2018.client;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.internal.bind.MapTypeAdapterFactory;

import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.TreeSet;

import ServerExchange.LocationsList;
import ServerExchange.ServerRequests.CreateAdvertRequest;
import ServerExchange.ServerRequests.ServerAnswerHandlers.DefaultServerAnswerHandler;
import ServerExchange.ServerRequests.UpdateAdvertRequest;
import ServerExchange.ServerRequests.UpdateProfileRequest;

public class EditAdvertActivity extends AppCompatActivity {

    private TextView title, description, cost;
    private Spinner kingdom, city;


    private int selectedKingdomIndex;
    private int selectedCityIndex;
    private long[] locsIds;
    private long locId;

    UpdateAdvertRequest updateRequest = new UpdateAdvertRequest();
    CreateAdvertRequest createRequest = new CreateAdvertRequest();

    private class onCreateAdvert extends DefaultServerAnswerHandler<Boolean>{

        public onCreateAdvert(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {
            if (answ != null && answ == true){
                goBack();
            }
        }
    }

    private class onUpdateAdvert extends DefaultServerAnswerHandler<Boolean>{

        public onUpdateAdvert(Context context) {
            super(context);
        }

        @Override
        public void handle(Boolean answ) {
            if (answ != null && answ == true){
                goBack();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_advert);

        final LocationsList locsList =LocationsList.getInstance();
        //{"Не указано"};



        kingdom = ((Spinner)findViewById(R.id.spinner_edit_kingdom));
        String[] kingdoms = new String[0];
        kingdoms = locsList.getKingdoms().toArray(kingdoms);
        setSpinner(kingdom, kingdoms, "Королевство");
        String advertKingdom = getIntent().getStringExtra("kingdom");
        selectedKingdomIndex = 0;
        if (advertKingdom != null && !"".equals(advertKingdom)){
            int kingdomsSize = kingdoms.length;
            for (int i = 0; i < kingdomsSize; i++){
                if (kingdoms[i].equals(advertKingdom)){
                    selectedKingdomIndex = i; break;
                }
            }
        }
        kingdom.setSelection(selectedKingdomIndex);
        kingdom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKingdomIndex = position;
                Pair<long[], String[]> idsAndNamesC = locsList.getCitiesIdsAndNames((String) kingdom.getSelectedItem());

                locsIds = idsAndNamesC.first;
                String cities[] = idsAndNamesC.second;

                setSpinner(city, cities, city.getPrompt().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Pair<long[], String[]> idsAndNamesC = locsList.getCitiesIdsAndNames((String) kingdom.getSelectedItem());

        locsIds = idsAndNamesC.first;
        String cities[] = idsAndNamesC.second;

        selectedCityIndex = 0;
        String advertCity = getIntent().getStringExtra("city");
        if (advertCity != null && !"".equals(advertCity)){
            int citiesSize = cities.length;
            for (int i = 0; i < citiesSize; i++){
                if (cities[i].equals(advertCity)){
                    selectedCityIndex = i; break;
                }
            }
        }
        locId = locsIds[selectedCityIndex];
        city = ((Spinner)findViewById(R.id.spinner_edit_city));
        setSpinner(city, cities, "Город");
        city.setSelection(selectedCityIndex);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locId = locsIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        title = (TextView)findViewById(R.id.text_title_advert);
        description = (TextView)findViewById(R.id.text_description);
        cost = (TextView)findViewById(R.id.edit_advert_cost);



        String oldTitleVal = title.getText().toString();
        String oldDescriptionValue = description.getText().toString();
        int oldCostVal = 0;
        try {
            oldCostVal = Integer.parseInt(cost.getText().toString());

        }catch (Exception e) {}
        long oldLockId = locId;

        Button buttonPublic = (Button)findViewById(R.id.button_public);
        buttonPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Публикация объявления
                String newTitleVal = title.getText().toString();
                String newDescriptionVal = description.getText().toString();
                int newCostVal = Integer.parseInt(cost.getText().toString());
                long newLocId = locId;

                if (getIntent().getBooleanExtra("isCreate", false)){
                      createRequest.createAdvert(newTitleVal, newDescriptionVal, newCostVal, locId, null, new onCreateAdvert(EditAdvertActivity.this));
                } else {
                    //  updateRequest.updateAdvert();
                }


            }
        });
    }

    private void goBack(){
        Intent intent = new Intent();
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        intent.putExtra("cost", cost.getText().toString());
        intent.putExtra("kingdom", (String)kingdom.getSelectedItem());
        intent.putExtra("city", (String) city.getSelectedItem());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setSpinner(Spinner spinner, String[] data, String title) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt(title);
    }
}
