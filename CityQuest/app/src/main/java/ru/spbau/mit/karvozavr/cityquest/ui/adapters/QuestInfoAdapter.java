package ru.spbau.mit.karvozavr.cityquest.ui.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.mit.karvozavr.cityquest.QuestInfoActivity;
import ru.spbau.mit.karvozavr.cityquest.R;
import ru.spbau.mit.karvozavr.cityquest.quest.QuestController;
import ru.spbau.mit.karvozavr.cityquest.quest.QuestInfo;
import ru.spbau.mit.karvozavr.cityquest.quest.ServerMock;
import ru.spbau.mit.karvozavr.cityquest.ui.QuestStepActivity;

public class QuestInfoAdapter extends RecyclerView.Adapter<QuestInfoAdapter.QuestInfoViewHolder> {

    private List<QuestInfo> quests;
    public int firstLoaded = 0;
    public final int batchSize = 15;

    public QuestInfoAdapter(List<QuestInfo> quests) {
        this.quests = quests;
    }

    @Override
    public QuestInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View questInfoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_quest, null);
        return new QuestInfoViewHolder(questInfoView);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(QuestInfoViewHolder holder, int position) {

        QuestInfo questInfo = quests.get(position);

        if (questInfo != null) {
            TextView name = holder.questInfoView.findViewById(R.id.quest_title);
            TextView avgDistance = holder.questInfoView.findViewById(R.id.quest_avg_distance);
            TextView description = holder.questInfoView.findViewById(R.id.quest_short_description);
            AppCompatRatingBar ratingBar = holder.questInfoView.findViewById(R.id.quest_rating_bar);

            ratingBar.setRating(questInfo.rating);
            name.setText(questInfo.name);
            avgDistance.setText(Float.toString(questInfo.averageDistance) + " km");
            description.setText(questInfo.shortDescription);


            Button questStartButton = holder.questInfoView.findViewById(R.id.quest_start_button);
            questStartButton.setOnClickListener((view) -> {
                Intent intent = new Intent(holder.questInfoView.getContext(), QuestStepActivity.class);
                intent.putExtra("quest_id", questInfo.id);
                holder.questInfoView.getContext().startActivity(intent);
            });

            Button questInfoButton = holder.questInfoView.findViewById(R.id.quest_info_button);
            questInfoButton.setOnClickListener((view) -> {
                Intent intent = new Intent(holder.questInfoView.getContext(), QuestInfoActivity.class);
                intent.putExtra("quest_info", questInfo);
                holder.questInfoView.getContext().startActivity(intent);
            });

            // TODO picture
        }
    }

    public void loadPrevBatch() {
        firstLoaded -= batchSize;

        List<QuestInfo> newQuests = QuestController.getQuestInfoList(firstLoaded, firstLoaded + batchSize);
        newQuests.addAll(quests.subList(0, batchSize));
        quests = newQuests;

        notifyDataSetChanged();
    }

    public void loadNextBatch() {
        firstLoaded += batchSize;
        quests = new ArrayList<>(quests.subList(batchSize, batchSize * 2));

        quests.addAll(QuestController.getQuestInfoList(firstLoaded + batchSize, firstLoaded + batchSize * 2));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public static class QuestInfoViewHolder extends RecyclerView.ViewHolder {

        View questInfoView;

        public QuestInfoViewHolder(View view) {
            super(view);
            questInfoView = view;
        }
    }
}