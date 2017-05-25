var express = require('express')
  , bodyParser = require('body-parser')
  , app = express()
  , firebase = require("firebase")
  , admin = require("firebase-admin")
  , serviceAccount = require("./firebase-service-key.json")
  , mongodb = require('mongodb')
  , assert = require('assert')
  , url = 'mongodb://localhost:1234/usersdata'
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
    token: content.token,
    displayName: content.displayName
  }

  var error = {
    msg: "Email already exists",
    email: content.email
  }

  emailExists(content.email, (valid) => {
    console.log("VALID:", valid)
    if (valid) {
      connection.then(function (db) {
        db.collection('userdata').insertOne(newUser, function (err, result) {
          console.log("Inserted 1 documents into the collection:", result);
          error.errObj = err
          res.end(JSON.stringify(result))
        })
      });
    } else {
      res.end(JSON.stringify(error))
    }
  })
})

app.get('/', (req, res) => {

  connection.then(function (db) {
    db.collection('userdata').find({}).toArray().then(function (docs) {
      console.log(docs)
      res.end(JSON.stringify(docs));
    }, function (err) {
      res.end(JSON.stringify(err))
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
  getTokenByEmail(payload.data.toEmail, (result) => {
    admin.messaging().sendToDevice(result[0].token, payload)
      .then(function (response) {
        return response;
      })
      .catch(function (error) {
        return error;
      });
  })
}

var getTokenByEmail = (email, cb) => {
  var token = ""
  connection.then(function (db) {
    db.collection('userdata').find({ "email": email }).limit(1).toArray().then(function (docs) {
      cb(docs);
    });
  })
}

var emailExists = (email, cb) => {
  return connection.then(function (db) {
    return db.collection('userdata').findOne({ "email": email }).then(function (result) {
      cb(result == null);
    });
  })
}


module.exports = app;