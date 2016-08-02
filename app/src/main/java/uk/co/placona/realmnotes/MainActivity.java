package uk.co.placona.realmnotes;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;
    private RealmConfiguration mRealmConfig;
    private EditText mText;
    private RealmRecyclerView mNotes;
    private FloatingActionButton mNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();
        mText = (EditText) findViewById(R.id.et_text);
        mNotes = (RealmRecyclerView) findViewById(R.id.rv_notes);
        mNew = (FloatingActionButton) findViewById(R.id.fab_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mText.getText().length() > 0){
                    insertNote(mText.getText().toString());
                    mText.setText("");

                    Snackbar.make(view, "Note added!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        loadNotes();
    }

    private void loadNotes() {
        RealmResults<Note> notes = mRealm.where(Note.class).findAllSorted("date", Sort.ASCENDING);
        NoteRecyclerViewAdapter noteAdapter = new NoteRecyclerViewAdapter(getBaseContext(), notes);
        mNotes.setAdapter(noteAdapter);
    }

    private void insertNote(final String noteText) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note note = new Note();
                note.setText(noteText);
                mRealm.copyToRealm(note);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class NoteRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<
                Note, NoteRecyclerViewAdapter.ViewHolder> {

        public NoteRecyclerViewAdapter(
                Context context,
                RealmResults<Note> realmResults) {
            super(context, realmResults, true, true, true, "text");
        }

        public class ViewHolder extends RealmViewHolder {
            private TextView mText;
            private TextView mDate;

            public ViewHolder(RelativeLayout container) {
                super(container);
                this.mText = (TextView) container.findViewById(R.id.tv_text);
                this.mDate = (TextView) container.findViewById(R.id.tv_date);
            }
        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.note_item, viewGroup, false);
            return new ViewHolder((RelativeLayout) v);
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
            final Note note = realmResults.get(position);
            viewHolder.mText.setText(note.getText());
            viewHolder.mDate.setText(note.getDate().toString());
        }
    }
}
