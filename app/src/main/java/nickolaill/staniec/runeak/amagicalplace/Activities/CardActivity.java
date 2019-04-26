package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;

public class CardActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextText, editTextSeries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextText = findViewById(R.id.edit_text_text);
        editTextSeries = findViewById(R.id.edit_text_series);

        getSupportActionBar()
                .setHomeAsUpIndicator(R.drawable.ic_close);

        Intent i = getIntent();
        if(i.hasExtra(Constants.EDIT_EXTRA_ID)){
            setTitle("Edit Card");
            editTextTitle.setText(i.getStringExtra(Constants.EDIT_EXTRA_TITLE));
            editTextSeries.setText(i.getStringExtra(Constants.EDIT_EXTRA_SERIES));
            editTextText.setText(i.getStringExtra(Constants.EDIT_EXTRA_TEXT));

        } else {
            setTitle("Add Card");
        }
    }

    private void saveCard() {
        String title = editTextTitle.getText().toString();
        String text = editTextText.getText().toString();
        String series = editTextSeries.getText().toString();

        if (title.trim().isEmpty() || text.trim().isEmpty() || series.trim().isEmpty()){
            Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show();
            return;
        }

        // Should remove the below part, and instead have a ViewModel
        // for this fragment instead. But we'll use intent for now.
        Intent data = new Intent();
        data.putExtra(Constants.EDIT_EXTRA_TITLE, title);
        data.putExtra(Constants.EDIT_EXTRA_SERIES, series);
        data.putExtra(Constants.EDIT_EXTRA_TEXT, text);
        if (getIntent().getIntExtra(Constants.EDIT_EXTRA_ID, -1) != -1) {
            data.putExtra(Constants.EDIT_EXTRA_ID, getIntent().getIntExtra(Constants.EDIT_EXTRA_ID, -1));
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cardactivity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_card:
                saveCard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
