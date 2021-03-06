package ru.spbau.mit.karvozavr.cityquest.quest;

import android.app.Activity;

import java.io.Serializable;

/**
 * Represents a single quest step
 */
public abstract class AbstractQuestStep implements Serializable {
    public final String title;
    public final String description;
    public final String goal;
    public final String image;
    public int stepNum;

    /**
     * This is text that would be shown on action button in QuestActivity
     */
    public final String actionLabel;

    public AbstractQuestStep(String title,
                             String description,
                             String goal,
                             String actionLabel,
                             String image) {
        this.title = title;
        this.description = description;
        this.goal = goal;
        this.image = image;
        this.actionLabel = actionLabel;
    }

    /**
     * Checks if the step pass condition satisfied.
     */
    public abstract void check(Activity context);
}
