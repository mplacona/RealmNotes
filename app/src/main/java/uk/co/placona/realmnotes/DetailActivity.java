package uk.co.placona.realmnotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Instantiate the API
        ApiService api = NoteClient.getApiService(getString(R.string.apiUserName), getString(R.string.apiPassword));

        //Get the item id
        Bundle b = getIntent().getExtras();
        int id = 1;

        if(b != null){
            id = b.getInt("id");
        }

        // Request JSON
        Call<NoteDetail> call = api.getNoteJson(String.valueOf(id));

        call.enqueue(new Callback<NoteDetail>() {
            @Override
            public void onResponse(Call<NoteDetail> call, Response<NoteDetail> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");
                    final TextView textView = (TextView) findViewById(R.id.tv_body);
                    textView.setText(response.body().getBody());
                }
            }

            @Override
            public void onFailure(Call<NoteDetail> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                final TextView textView = (TextView) findViewById(R.id.tv_body);
                textView.setText("Something went wrong: " + t.getMessage());
            }
        });
    }
}
