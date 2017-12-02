package ru.spbau.mit.karvozavr.cityquest.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ru.spbau.mit.karvozavr.cityquest.R;
import ru.spbau.mit.karvozavr.cityquest.quest.QuestController;
import ru.spbau.mit.karvozavr.cityquest.quest.QuestInfo;
import ru.spbau.mit.karvozavr.cityquest.ui.adapters.QuestInfoAdapter;
import ru.spbau.mit.karvozavr.cityquest.ui.util.EndlessRecyclerViewOnScrollListener;

public class QuestGalleryActivity extends AppCompatActivity {

    private RecyclerView galleryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_gallery);

        loadGallery();
    }

    private void loadGallery() {
        galleryRecyclerView = findViewById(R.id.gallery_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        galleryRecyclerView.setLayoutManager(layoutManager);

        // TODO
        RecyclerView.Adapter questInfoAdapter = new QuestInfoAdapter(getInitQuestInfos());

        galleryRecyclerView.setAdapter(questInfoAdapter);
        galleryRecyclerView.setOnFlingListener(new EndlessRecyclerViewOnScrollListener(galleryRecyclerView));

        initRefreshLayout();
    }

    private void initRefreshLayout() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.gallery_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().post(() -> {
            if (((QuestInfoAdapter) galleryRecyclerView.getAdapter()).firstLoaded == 0) {
                // TODO
                Toast.makeText(QuestGalleryActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                getInitQuestInfos();
            }
            swipeRefreshLayout.setRefreshing(false);
        }));
    }

    private List<QuestInfo> getInitQuestInfos() {
        return QuestController.getQuestInfoList(0, 30);
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
            searchView.setOnCloseListener(() -> {
                Toast.makeText(QuestGalleryActivity.this, "Close", Toast.LENGTH_SHORT).show();
                QuestController.currentQuery = "";

                // refresh gallery
                loadGallery();
                return false;
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    QuestController.currentQuery = query;
                    Toast.makeText(QuestGalleryActivity.this, "Query", Toast.LENGTH_SHORT).show();

                    // refresh gallery
                    loadGallery();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }
}
