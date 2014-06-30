package org.xbib.elasticsearch.index.analysis.naturalsort;

import org.elasticsearch.index.analysis.AnalysisModule;

public class NaturalSortAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("naturalsort", NaturalSortKeyFilterFactory.class);
    }

}


