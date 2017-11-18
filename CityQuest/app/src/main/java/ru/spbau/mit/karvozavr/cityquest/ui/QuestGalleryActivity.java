package ru.spbau.mit.karvozavr.cityquest.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.spbau.mit.karvozavr.cityquest.R;
import ru.spbau.mit.karvozavr.cityquest.quest.ServerMock;
import ru.spbau.mit.karvozavr.cityquest.ui.adapters.QuestInfoAdapter;
import ru.spbau.mit.karvozavr.cityquest.ui.util.EndlessRecyclerViewOnScrollListener;

public class QuestGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_gallery);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());*/

        loadGallery();
    }

    private void loadGallery() {
        RecyclerView galleryRecyclerView = findViewById(R.id.gallery_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        galleryRecyclerView.setLayoutManager(layoutManager);

        // TODO
        RecyclerView.Adapter questInfoAdapter = new QuestInfoAdapter(ServerMock.getQuestInfosBatch(0, 30));

        galleryRecyclerView.setAdapter(questInfoAdapter);
        galleryRecyclerView.setOnFlingListener(new EndlessRecyclerViewOnScrollListener(galleryRecyclerView));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.gallery_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().post(() -> {
            if (((QuestInfoAdapter) questInfoAdapter).firstLoaded == 0) {
                // TODO
                Toast.makeText(QuestGalleryActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                ServerMock.getQuestInfosBatch(0, 30);
            }
            swipeRefreshLayout.setRefreshing(false);
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) QuestGalleryActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(QuestGalleryActivity.this.getComponentName()));
        }

        return super.onCreateOptionsMenu(menu);
    }
}
