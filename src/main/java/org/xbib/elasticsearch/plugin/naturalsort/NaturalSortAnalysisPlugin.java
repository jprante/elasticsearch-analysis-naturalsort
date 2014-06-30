package org.xbib.elasticsearch.plugin.naturalsort;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;
import org.xbib.elasticsearch.index.analysis.naturalsort.NaturalSortAnalysisBinderProcessor;

public class NaturalSortAnalysisPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-naturalsort-"
                + Build.getInstance().getVersion() + "-"
                + Build.getInstance().getShortHash();

    }

    @Override
    public String description() {
        return "Natural Sort Key Analysis";
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new NaturalSortAnalysisBinderProcessor());
    }
}

