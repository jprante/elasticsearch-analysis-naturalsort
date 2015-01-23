package org.xbib.elasticsearch.index.analysis.naturalsort;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

import java.text.Collator;
import java.util.Locale;

public class NaturalSortKeyFilterFactory extends AbstractTokenFilterFactory {

    private final String locale;
    private final int digits;
    private final int maxTokens;

    @Inject
    public NaturalSortKeyFilterFactory(Index index, @IndexSettings Settings indexSettings,
                                       @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.locale = settings.get("locale", Locale.getDefault().toString());
        this.digits = settings.getAsInt("digits", 1);
        this.maxTokens = settings.getAsInt("maxTokens", 2);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new NaturalSortKeyFilter(tokenStream, Collator.getInstance(new Locale(locale)), digits, maxTokens);
    }
}
