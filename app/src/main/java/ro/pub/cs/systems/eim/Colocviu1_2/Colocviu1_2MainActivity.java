package ro.pub.cs.systems.eim.Colocviu1_2;

import android.content.Intent;
import android.os.Bundle;
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

    private EditText nextTermEditText;
    private TextView allTermsTextView;
    private Button addButton;
    private Button computeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test01_2_main);

        nextTermEditText = findViewById(R.id.next_term);
        allTermsTextView = findViewById(R.id.all_terms);
        addButton = findViewById(R.id.add_button);
        computeButton = findViewById(R.id.compute_button);

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
                } else {
                    Toast.makeText(Colocviu1_2MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Colocviu1_2MainActivity.this, Colocviu1_2SecondaryActivity.class);
                intent.putExtra("allTerms", allTermsTextView.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int result = data.getIntExtra("result", 0);
            Toast.makeText(this, "Sum: " + result, Toast.LENGTH_LONG).show();
        }
    }
}