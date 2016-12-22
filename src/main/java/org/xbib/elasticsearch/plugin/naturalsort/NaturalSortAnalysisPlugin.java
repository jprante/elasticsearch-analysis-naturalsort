package org.xbib.elasticsearch.plugin.naturalsort;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.xbib.elasticsearch.index.analysis.naturalsort.NaturalSortKeyAnalyzerProvider;
import org.xbib.elasticsearch.index.analysis.naturalsort.NaturalSortKeyTokenizerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The plugin class for natural sort analysis.
 */
public class NaturalSortAnalysisPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new LinkedHashMap<>();
        extra.put("naturalsort", NaturalSortKeyTokenizerFactory::new);
        return extra;
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new LinkedHashMap<>();
        extra.put("naturalsort", NaturalSortKeyAnalyzerProvider::new);
        return extra;
    }
}
