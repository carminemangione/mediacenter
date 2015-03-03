package com.mangione.mediacenter.view.rottentomatoes.rottentomatoessearch;

import com.mangione.mediacenter.view.rottentomatoes.RTMainController;

public class ResearchController {


    private final ResearchPanel researchPanel;
    private final RTMainController movieDetailsController;

    public ResearchController(RTMainController movieDetailsController) {
        this.movieDetailsController = movieDetailsController;
        researchPanel = new ResearchPanel(this);

    }

    public ResearchPanel getResearchPanel() {
        return researchPanel;
    }

    public void research(String returnText) {
        movieDetailsController.reSearch(returnText);
    }
}
