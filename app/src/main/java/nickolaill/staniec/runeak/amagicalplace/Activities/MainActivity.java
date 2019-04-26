package nickolaill.staniec.runeak.amagicalplace.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Adapters.CardAdapter;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.R;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.ViewModels.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CardAdapter adapter = new CardAdapter();

        FloatingActionButton buttonAddCard = findViewById(R.id.button_add_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardActivity.class);
                startActivityForResult(intent, Constants.ADD_CARD_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                adapter.submitList(cards);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mainViewModel.delete(adapter.getCardAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Card card) {
                Intent intent = new Intent(MainActivity.this, CardActivity.class);
                intent.putExtra(Constants.EDIT_EXTRA_ID, card.getId());
                intent.putExtra(Constants.EDIT_EXTRA_TITLE, card.getTitle());
                intent.putExtra(Constants.EDIT_EXTRA_SERIES, card.getSeries());
                intent.putExtra(Constants.EDIT_EXTRA_TEXT, card.getText());

                startActivityForResult(intent, Constants.EDIT_CARD_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case Constants.ADD_CARD_REQUEST:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(Constants.EDIT_EXTRA_TITLE);
                    String series = data.getStringExtra(Constants.EDIT_EXTRA_SERIES);
                    String text = data.getStringExtra(Constants.EDIT_EXTRA_TEXT);

                    Card card = new Card(title, series, text);
                    mainViewModel.insert(card);
                    Toast.makeText(this, "Card saved", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Card not saved", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.EDIT_CARD_REQUEST:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(Constants.EDIT_EXTRA_TITLE);
                    String series = data.getStringExtra(Constants.EDIT_EXTRA_SERIES);
                    String text = data.getStringExtra(Constants.EDIT_EXTRA_TEXT);

                    int id = data.getIntExtra(Constants.EDIT_EXTRA_ID, -1);
                    if (id == -1) {
                        Toast.makeText(this, "Data corrupted", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Card card = new Card(title, series, text);
                    card.setId(id);

                    mainViewModel.update(card);
                    Toast.makeText(this, "Card updated", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
