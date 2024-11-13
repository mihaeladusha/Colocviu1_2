package ro.pub.cs.systems.eim.Colocviu1_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Colocviu1_2MainActivity extends AppCompatActivity {

    private static final String TAG = "Colocviu1_2MainActivity";
    private EditText nextTermEditText;
    private TextView allTermsTextView;
    private Button addButton;
    private Button computeButton;

    private int lastComputedSum = 0;
    private String lastAllTerms = "";
    private boolean isServiceStarted = false;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String currentTime = intent.getStringExtra("currentTime");
            int sum = intent.getIntExtra(Colocviu1_2Service.EXTRA_SUM, 0);
            Log.d(TAG, "Broadcast received with currentTime: " + currentTime + " and sum: " + sum);
            Toast.makeText(context, "Broadcast received: " + currentTime + " Sum: " + sum, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test01_2_main);

        nextTermEditText = findViewById(R.id.next_term);
        allTermsTextView = findViewById(R.id.all_terms);
        addButton = findViewById(R.id.add_button);
        computeButton = findViewById(R.id.compute_button);

        if (savedInstanceState != null) {
            lastComputedSum = savedInstanceState.getInt("lastComputedSum", 0);
            lastAllTerms = savedInstanceState.getString("lastAllTerms", "");
            allTermsTextView.setText(savedInstanceState.getString("allTermsText", ""));
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nextTerm = nextTermEditText.getText().toString();
                if (!nextTerm.isEmpty() && nextTerm.matches("\\d+")) {
                    String allTerms = allTermsTextView.getText().toString();
                    if (allTerms.isEmpty()) {
                        allTermsTextView.setText(nextTerm);
                    } else {
                        allTermsTextView.setText(allTerms + " + " + nextTerm);
                    }
                    nextTermEditText.setText("");
                    Log.d(TAG, "Added term: " + nextTerm);
                } else {
                    Toast.makeText(Colocviu1_2MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Invalid term entered: " + nextTerm);
                }
            }
        });

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentAllTerms = allTermsTextView.getText().toString();
                if (currentAllTerms.equals(lastAllTerms)) {
                    Toast.makeText(Colocviu1_2MainActivity.this, "Sum: " + lastComputedSum, Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Sum already computed: " + lastComputedSum);
                } else {
                    Intent intent = new Intent(Colocviu1_2MainActivity.this, Colocviu1_2SecondaryActivity.class);
                    intent.putExtra("allTerms", currentAllTerms);
                    startActivityForResult(intent, 1);
                    Log.d(TAG, "Starting secondary activity with terms: " + currentAllTerms);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lastComputedSum", lastComputedSum);
        outState.putString("lastAllTerms", lastAllTerms);
        outState.putString("allTermsText", allTermsTextView.getText().toString());
        Log.d(TAG, "Saved instance state");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            lastComputedSum = data.getIntExtra("result", 0);
            lastAllTerms = allTermsTextView.getText().toString();
            Toast.makeText(this, "Sum: " + lastComputedSum, Toast.LENGTH_LONG).show();
            Log.d(TAG, "Received result from secondary activity: " + lastComputedSum);

            if (lastComputedSum > 10 && !isServiceStarted) {
                Intent serviceIntent = new Intent(this, Colocviu1_2Service.class);
                serviceIntent.putExtra(Colocviu1_2Service.EXTRA_SUM, lastComputedSum);
                startService(serviceIntent);
                isServiceStarted = true;
                Log.d(TAG, "Started service with sum: " + lastComputedSum);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Colocviu1_2Service.BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        Log.d(TAG, "Registered broadcast receiver");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        Log.d(TAG, "Unregistered broadcast receiver");
    }

    @Override
    protected void onDestroy() {
        if (isServiceStarted) {
            stopService(new Intent(this, Colocviu1_2Service.class));
            Log.d(TAG, "Stopped service");
        }
        super.onDestroy();
    }
}