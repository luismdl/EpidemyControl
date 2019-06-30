var express = require('express');
var app = express();
const bodyParser = require('body-parser');
const assert = require('assert');
var router = express.Router();

const MongoClient = require('mongodb').MongoClient;
const url = 'mongodb://192.168.1.93:27017';

const dbName = 'TFM';


router.post("/state",function(req, res, next) {
  var data = req.body;
  data.timestamp = new Date(data.timestamp);
  console.log(data);
  const client = new MongoClient(url,{ useNewUrlParser: true });
  client.connect(function(err) {
    assert.equal(null, err);
    console.log("Connected successfully to server");

    const db = client.db(dbName);

    if(data.state==1){
      const collection = db.collection('epidemyControl');
      collection.find({"state": 1,
        "timestamp": 
        {
          $gte: new Date((new Date().getTime() -  (24 * 60 * 60 * 1000)))
        },
        "location":{
          $geoWithin: { $center: [ data.location.coordinates , 1000 ] }
        }
      }).toArray(function(err, docs) {
        console.log("Docs encontrados: "+ docs.length)
        if(docs.length>1){
          var collAlerts =  db.collection('alerts');
          collAlerts.insertOne({"location": data.location,
            "timestamp": data.timestamp})
        }
      });
    }

    insertDocuments(db,data, function(data) {
      res.sendStatus(201);
      client.close();
    });
  });

})

const insertDocuments = function(db,data ,callback) {
  // Get the documents collection
  const collection = db.collection('epidemyControl');
  // Insert some documents
  collection.insertOne(data, function(err, result) {
    console.log("Inserted document into the collection");
    callback(result);
  })
}



app.use(bodyParser.json());
app.use(router);

app.listen(process.env.PORT || 3000);

