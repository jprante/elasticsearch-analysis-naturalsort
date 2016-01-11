
# Elasticsearch analysis plugin for natural sort

This plugin allows the use of sort keys for natural sort order.

## Versions

| Elasticsearch version    | Plugin      | Release date |
| ------------------------ | ----------- | -------------|
| 2.1.1                    | 2.1.1.0     | Jan 11, 2016 |
| 1.7.0                    | 1.7.0.0     | Jul 27, 2015 |
| 1.6.1                    | 1.6.1.0     | Jul 27, 2015 |
| 1.4.0                    | 1.4.0.0     | Nov 13, 2014 |
| 1.3.4                    | 1.3.4.0     | Jan 23, 2014 |
| 1.2.1                    | 1.2.1.0     | Jun 30, 2014 |

## Installation

### 2.x

    ./bin/plugin install http://xbib.org/repository/org/xbib/elasticsearch/plugin/elasticsearch-analysis-naturalsort/2.1.1.0/elasticsearch-analysis-naturalsort-2.1.1.0-plugin.zip

Do not forget to restart the node after installing.

### 1.x

    ./bin/plugin -install analysis-naturalsort -url http://xbib.org/repository/org/xbib/elasticsearch/plugin/elasticsearch-analysis-naturalsort/1.7.0.0/elasticsearch-analysis-naturalsort-1.7.0.0-plugin.zip

Do not forget to restart the node after installing.

## Project docs

The Maven project site is available at [Github](http://jprante.github.io/elasticsearch-analysis-naturalsort)

## Issues

All feedback is welcome! If you find issues, please post them at [Github](https://github.com/jprante/elasticsearch-analysis-naturalsort/issues)

# How to use

Define a field analyzer of `naturalsort`

    PUT /test

    POST /_test
    { 
        "type1" : { 
            "properties" : { 
                "points" : { 
                    "type" : "string", 
                    "fields" : { 
                        "sort" : { "type" : "string", "analyzer" : "naturalsort" } 
                    } 
                } 
            } 
        } 
    }

Index some data

    PUT /test/doc/1
    {
      "points" :  "Bob: 2 points"
    }

    PUT /test/doc/2
    {
      "points" : "Bob: 3 points"
    }

    PUT /test/doc/3
    {
      "points" : "Bob: 10 points"
    }

Search and sort

    POST /test/_search
    {
       "fields" : "points",
       "query": {
           "match_all" : {}
        },
        "sort" : { 
                "points.sort" : {
                     "order" : "asc"
                }
        }       
    }

Response

    {
      "took" : 54,
      "timed_out" : false,
      "_shards" : {
        "total" : 2,
        "successful" : 2,
        "failed" : 0
      },
      "hits" : {
        "total" : 3,
        "max_score" : null,
        "hits" : [ {
          "_index" : "test",
          "_type" : "type1",
          "_id" : "Ono1C2F7SdWBPL6QRAGA6Q",
          "_score" : null,
          "fields" : {
            "points" : "Bob: 2 points"
          },
          "sort" : [ "*\u0018䀊䀀。ᰁက挀㄀ᜀఠٰ̰\u0000\u0002\u0001\u0000䀀‎瀀ࠀЁ渀Ā@ \u0010\b\u0000\u0004\u0001\u0000䀀 ကࠀЀȀĀ@ \u0001" ]
        }, {
          "_index" : "test",
          "_type" : "type1",
          "_id" : "J5EornIuRKCy7_G084eyoQ",
          "_score" : null,
          "fields" : {
            "points" : "Bob: 3 points"
          },
          "sort" : [ "*\u0018䀊䀀。ᰁሀ挀㄀ᜀఠٰ̰\u0000\u0002\u0001\u0000䀀‎瀀ࠀЁ渀Ā@ \u0010\b\u0000\u0004\u0001\u0000䀀 ကࠀЀȀĀ@ \u0001" ]
        }, {
          "_index" : "test",
          "_type" : "type1",
          "_id" : "yHWy94jPTHqULtgSZvvysw",
          "_score" : null,
          "fields" : {
            "points" : "Bob: 10 points"
          },
          "sort" : [ "*\u0018䀊䀀。 ฀䘀ㆀᢀ஀̸ؐƘ\u0000\u0001\u0000䀀 ဇ㠀ЀȀĀ㮀@ \u0010\b\u0004\u0002\u0000\u0001\u0000 ကࠀЀȀĀ@ \u0010\b\u0004\u0001" ]
        } ]
      }
    }

# License

Elasticsearch Analysis Naturalsort Plugin

Copyright (C) 2012 Jörg Prante

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
lim