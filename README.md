Installation

- Download the source and unzip it if necessary
- cd to the directory (i.e. elasticsearch-analysis-naturalsort-master/)
- Package the plugin with Maven:
    - Just run "mvn package"
- Then install the plugin:
    - /path/to/elasticsearch/bin/plugin -url file:./target/releases/elasticsearch-analysis-naturalsort-1.0.0.zip -install naturalsort
- Restart elasticsearch:
    - service elasticserach restart

- You can make sure it's installed by doing:
    - curl -XGET 'http://localhost:9200/_nodes/plugin?pretty=true'

Settings

    index:
      analysis:
          analyzer:
              naturalsort:
                  tokenizer: keyword
                  filter: naturalsort

Mappings

    { 
      "type1" : { 
        "properties" : { 
             "points" : { 
                "type" : "multi_field", 
                "fields" : { 
                    "points" : { "type" : "string" }, 
                    "sort" : { "type" : "string", "analyzer" : "naturalsort" } 
                } 
             } 
         } 
      } 
    }


Search

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
