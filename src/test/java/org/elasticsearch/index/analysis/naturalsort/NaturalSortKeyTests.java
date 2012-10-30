/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis.naturalsort;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class NaturalSortKeyTests extends AbstractNodesTests {

    private Client client;

    @BeforeClass
    public void createNodes() throws Exception {
        Settings settings = ImmutableSettings.settingsBuilder()
        		.put("index.number_of_shards", numberOfShards())
        		.put("index.number_of_replicas", 0)
        		.build();
        for (int i = 0; i < numberOfNodes(); i++) {
            startNode("node" + i, settings);
        }
        client = getClient();
    }

    protected int numberOfShards() {
        return 2;
    }

    protected int numberOfNodes() {
        return 2;
    }

    protected int numberOfRuns() {
        return 1;
    }

    @AfterClass
    public void closeNodes() {
        client.close();
        closeAllNodes();
    }

    protected Client getClient() {
        return client("node0");
    }

    @Test
    public void testSort() throws Exception {
        try {
            client.admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
            // ignore
        }
	Settings settings = settingsBuilder()
              .put("index.analysis.analyzer.naturalsort.tokenizer", "keyword")			
              .put("index.analysis.analyzer.naturalsort.filter", "naturalsort")
              .build();
	
	client.admin().indices().prepareCreate("test")
	      .setSettings(settings)
              .addMapping("type1", 
                 "{ type1 : { properties : { points : { type : \"multi_field\", fields : { points : { type : \"string\" }, sort : { type : \"string\", analyzer : \"naturalsort\" } } } } } }")
              .execute().actionGet();
        
        client.admin().cluster().prepareHealth().setWaitForGreenStatus().execute().actionGet();

        String [] words = new String[] {
            "Bob: 3 points", "Bob: 10 points", "Bob: 2 points"
        };
        
        for (String word : words) {
            client.prepareIndex("test", "type1")
                .setSource(jsonBuilder().startObject()
                .field("points", word)    
                .endObject()).execute().actionGet();
        }

        client.admin().indices().prepareFlush().setRefresh(true).execute().actionGet();

        for (int i = 0; i < numberOfRuns(); i++) {
            SearchResponse searchResponse = client.prepareSearch()
                    .addField("points")
                    .addSort("points.sort", SortOrder.ASC)
                    .execute().actionGet();

            //logger.info(searchResponse.toString());
            
            assertThat(searchResponse.hits().totalHits(), equalTo(3l));
            assertThat(searchResponse.hits().getAt(0).field("points").getValue().toString(), equalTo("Bob: 2 points"));
            assertThat(searchResponse.hits().getAt(1).field("points").getValue().toString(), equalTo("Bob: 3 points"));
            assertThat(searchResponse.hits().getAt(2).field("points").getValue().toString(), equalTo("Bob: 10 points"));

        }
    }

}
