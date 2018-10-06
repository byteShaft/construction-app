package com.byteshaft.hafizconstructionworks;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.byteshaft.hafizconstructionworks.gettersetters.Quarters;
import com.byteshaft.hafizconstructionworks.gettersetters.QuartersDetails;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class QuarterDetailActivity extends AppCompatActivity {

    private ListView mListview;
    private int mQuarterId;
    private String mQuarterName;
    private QuarterDetailsAdapter adapter;
    private ArrayList<QuartersDetails> quartersDetailsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        mQuarterId = getIntent().getIntExtra("q_id", -1);
        mQuarterName = getIntent().getStringExtra("q_name");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        quartersDetailsArrayList = new ArrayList<>();
        setTitle(mQuarterName);
        mListview = findViewById(R.id.quarter_detail_list);
        getQuarterDetails(mQuarterId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getQuarterDetails(int quarterId) {
        HttpRequest request = new HttpRequest(getApplicationContext());
        request.setOnReadyStateChangeListener(new HttpRequest.OnReadyStateChangeListener() {
            @Override
            public void onReadyStateChange(HttpRequest httpRequest, int i) {
                switch (i) {
                    case HttpRequest.STATE_DONE:
                        switch (httpRequest.getStatus()) {
                            case HttpURLConnection.HTTP_OK:
                                Log.wtf("Response Data", httpRequest.getResponseText());
                                try {
                                    JSONObject jsonObject = new JSONObject(request.getResponseText());
                                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject quarterDetails = jsonArray.getJSONObject(j);
                                        QuartersDetails details = new QuartersDetails();
                                        details.setItemName(quarterDetails.getString("item_name"));
                                        details.setItemPrice(quarterDetails.getInt("price"));
                                        details.setItemQuantity(quarterDetails.getString("quantity"));
                                        quartersDetailsArrayList.add(details);
                                    }
                                    adapter = new QuarterDetailsAdapter(getApplicationContext(), quartersDetailsArrayList);
                                    mListview.setAdapter(adapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                        }
                }

            }
        });
        request.open("GET", String.format("%squarter/%s", AppGlobals.SERVER_IP, quarterId));
        request.send();
    }

    private class QuarterDetailsAdapter extends BaseAdapter {
        private ViewHolder viewHolder;
        private Context context;
        private ArrayList<QuartersDetails> quarters;

        private QuarterDetailsAdapter(Context context, ArrayList<QuartersDetails> quarters) {
            this.context = context;
            this.quarters = quarters;
        }

        @Override
        public int getCount() {
            return quarters.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.delegate_quarter_detail, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.item = convertView.findViewById(R.id.item_name);
                viewHolder.price = convertView.findViewById(R.id.item_price);
                viewHolder.quantity = convertView.findViewById(R.id.item_quantity);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            QuartersDetails singleQuarter = quarters.get(position);
            viewHolder.item.setText("Item Name:  " + singleQuarter.getItemName());
            Log.wtf("nonononon", String.valueOf(singleQuarter.getItemPrice()));
            viewHolder.quantity.setText("Quantity:  " + singleQuarter.getItemQuantity());
            viewHolder.price.setText("Price:  " + singleQuarter.getItemPrice());

            return convertView;
        }

        private class ViewHolder {
            private TextView item;
            private TextView price;
            private TextView quantity;
        }
    }
}
