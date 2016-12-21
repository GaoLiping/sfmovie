# sfmovie
A demo web application
# data
data is updated from http://data.sfgov.org/resource/wwmu-gmzc.json every 6 hours by the application
# backend api
backend is powered by lucene in-memory index

There are 2 end point in the backend,

<b>query - json post</b>

query takes following input
<pre>
start - hit start position
rows - max rows to return
fq - filter query pairs
fl - return fields (optional, by default system will return all fields)
</pre>
query example,
<pre>
{
"start":0,
"rows":5,
"fq": [
{
"name":"title",
"value":"freebie and the bean"
},
{
"name":"production_company",
"value":"columbia pictures"
}
]
}
</pre>
The output will be a list of sfmovie entires filtered by the input query. It is an <b>ORed</b> filtering, which means the more filter pairs the more results until the list reaches max rows to return.

example
<pre>
{
  "q": {
    "start": 0,
    "rows": 100,
    "fq": [
      {
        "name": "title",
        "value": "freebie and the bean"
      }
    ],
    "fl": null
  },
  "totalHits": 1,
  "hits": [
    {
      "values": [
        {
          "name": "title",
          "value": "freebie and the bean"
        },
        {
          "name": "release_year",
          "value": "1974"
        },
        {
          "name": "locations",
          "value": "stockton tunnel (stockton street at sutter street)"
        },
        {
          "name": "fun_facts",
          "value": ""
        },
        {
          "name": "production_company",
          "value": "warner bros. pictures"
        },
        {
          "name": "distributor",
          "value": "american broadcasting company (abc)"
        },
        {
          "name": "director",
          "value": "richard rush"
        },
        {
          "name": "writer",
          "value": "robert kaufman"
        },
        {
          "name": "actor_1",
          "value": "alan arkin"
        },
        {
          "name": "actor_2",
          "value": "james caan"
        },
        {
          "name": "actor_3",
          "value": "loretta swit"
        }
      ]
    }
  ],
  "error": ""
}
</pre>
<b>autocomplete - json post</b>

autocomplete takes following input
<pre>
rows - max autocomplete per category
q - autocomplete query text
fls - a list of query fields(optional, by default system will return autocomplete results on all fields)
</pre>
autocomplete query example,
<pre>
{
"start":0,
"rows":3,
"q":"Le",
"fls":["locations", "director"]
}
</pre>

The output will be a list of autocomplete entires, each autocomplete entry contains a category name and a list of autocomplete suggestion values,

example
<pre>
{
  "q": {
    "rows": 5,
    "q": "fre",
    "fls": null
  },
  "results": [
    {
      "name": "title",
      "value": {
        "values": [
          "freebie and the bean"
        ]
      }
    },
    {
      "name": "release_year",
      "value": {
        "values": []
      }
    },
    {
      "name": "locations",
      "value": {
        "values": []
      }
    },
    {
      "name": "fun_facts",
      "value": {
        "values": []
      }
    },
    {
      "name": "production_company",
      "value": {
        "values": [
          "frederick & ashbury, llc."
        ]
      }
    },
    {
      "name": "distributor",
      "value": {
        "values": []
      }
    },
    {
      "name": "director",
      "value": {
        "values": []
      }
    },
    {
      "name": "writer",
      "value": {
        "values": []
      }
    },
    {
      "name": "actor_1",
      "value": {
        "values": [
          "freddie prinze, jr."
        ]
      }
    },
    {
      "name": "actor_2",
      "value": {
        "values": [
          "fred macmurray"
        ]
      }
    },
    {
      "name": "actor_3",
      "value": {
        "values": []
      }
    }
  ],
  "error": null
}
</pre>
#frontend
frontend contains only one html file - index.html. It can do autocomplete on all fields by calling autocomplete endpoint, and then it gets all the locations filtered by calling the query endpoint with selected filtering pairs. Locations will be shown on google maps. If the locations information is not in good quality, google maps cannot show them.
