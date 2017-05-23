var express = require('express')
  , bodyParser = require('body-parser')
  , app = express()
  , firebase = require("firebase")
  , admin = require("firebase-admin")
  , serviceAccount = require("./firebase-service-key.json")
  , mongodb = require('mongodb')
  , assert = require('assert')
  , database = require('./data/dataAccess')
  , url = 'mongodb://localhost:27017/usersdata'
  , async = require('async')
  , MongoClient = mongodb.MongoClient
  , connection = MongoClient.connect(url);

admin.initializeApp({
  credential: admin.credential.cert({
    projectId: serviceAccount.project_id,
    clientEmail: serviceAccount.client_email,
    privateKey: serviceAccount.private_key
  }),
  databaseURL: "https://exam-app-e819f.firebaseio.com/"
});

app.use(bodyParser.json())

app.post('/register', (req, res) => {
  var content = req.body;
  var newUser = {
    email: content.email,
    token: content.token
  }

  MongoClient.connect(url, function (err, db) {
    assert.equal(null, err);
    console.log("Connected successfully to server");

    database.addUser(newUser, db, function () {
      db.close();
    });
  });

  res.end(JSON.stringify(content))
})

app.get('/', (req, res) => {
  var results = [];

  connection.then(function (db) {
    db.collection('userdata').find({}).toArray().then(function (docs) {
      console.log(docs)
      results = docs
      res.end(JSON.stringify(results));
    });
  });


})

app.post('/', (req, res) => {
  var content = req.body;

  var payload = {
    data: {
      fromEmail: content.from,
      toEmail: content.to,
      uMsg: content.msg
    }
  }

  res.end(sendMessage(payload));
})

var sendMessage = (payload) => {

  return admin.messaging().sendToDevice("cQxiQDuZhNQ:APA91bEB4CduwEZaxawYY6lEGf_rTZlvMtqUul4jqrVcDpNYLlEHe6se_UkGyYcWO37MGnSDZQ8tBxbL7LQlAzZxDPH3ZrAurgBhw35A69EKFTtaGFOZwYY784uzj4Awjt14RYFhstuh", payload)
    .then(function (response) {
      console.log("Successfully sent message:", response)
    })
    .catch(function (error) {
      console.log("Error sending message:", error);
    });
}

module.exports = app;