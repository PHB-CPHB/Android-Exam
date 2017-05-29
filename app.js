var express = require('express')
  , bodyParser = require('body-parser')
  , app = express()
  , firebase = require("firebase")
  , admin = require("firebase-admin")
  , serviceAccount = require("./firebase-service-key.json")
  , mongodb = require('mongodb')
  , assert = require('assert')
  , url = 'mongodb://localhost:27017/usersdata'
  , MongoClient = mongodb.MongoClient
  , connection = MongoClient.connect(url);

var config = {
  apiKey: "AIzaSyA6M4DZqpVed0Z9W7qDSof1S-wNCUGO1as",
  authDomain: "exam-app-e819f.firebaseapp.com",
  databaseURL: "https:exam-app-e819f.firebaseio.com"
};

firebase.initializeApp(config);

admin.initializeApp({ serviceAccount });

app.use(bodyParser.json())

app.post('/register', (req, res) => {
  var content = req.body;
  var newUser = {
    email: content.email,
    token: content.token,
    phone: content.phone,
    displayName: content.displayName
  }

  var errorRes = {
    email: content.email
  }

  firebase.auth().createUserWithEmailAndPassword(content.email, content.password).then(() => {
    connection.then(function (db) {
      db.collection('userdata').insertOne(newUser, function (err, result) {
        if (err) res.end(JSON.stringify(err))
        res.end(JSON.stringify({ message: "User successfully registered", FBRes: result, user: newUser }))
      })
    });
  }).catch(function (error) {
    // Handle Errors here.
    errorRes.code = error.code
    errorRes.error = error.message
    res.end(JSON.stringify(errorRes))
  });
})

app.get('/users', (req, res) => {

  connection.then(function (db) {
    db.collection('userdata').find({}).toArray().then(function (docs) {
      console.log(docs)
      res.end(JSON.stringify(docs));
    }, function (err) {
      res.end(JSON.stringify(err))
    });
  });
})

app.post('/updateToken', (req, res) => {
  var content = req.body;

  connection.then(function (db) {
    db.collection('userdata').updateOne(
      { email: content.email },
      { $set: { phone: content.phone, token: content.token } }, (err, count, status) => {
        if (err) res.end(JSON.stringify(err))
        res.end(JSON.stringify(count));
      });
  })
})

app.post('/match', (req, res) => {
  var selector = "";
  if (req.body["email"] == null && req.body["phone"] == null) res.end(JSON.stringify({ error: "Bad request body" }))
  req.body.email == null ? selector = { phone: req.body.phone } : selector = { email: req.body.email };
  var key = Object.getOwnPropertyNames(selector)[0];
  var obj = {};
  obj[key] = selector[key];

  connection.then(function (db) {
    db.collection('userdata').findOne(obj, (err, docs) => {
      if (err) res.end(JSON.stringify(err))
      if (!docs) res.end(JSON.stringify({ error: "No user found" }))
      res.end(JSON.stringify(docs));
    });
  })
})

app.post('/send', (req, res) => {
  var content = req.body;

  var payload = {
    data: {
      fromEmail: content.from,
      toEmail: content.to,
      uMsg: content.msg
    }
  }

  getTokenByEmail(payload.data.toEmail, (result) => {
    if (result.length < 1) res.end(JSON.stringify({ error: "Reciever not found" }))
    admin.messaging().sendToDevice(result[0].token, payload)
      .then(function (response) {
        res.end(JSON.stringify({OK: "OK"}));
      })
      .catch(function (error) {
        res.end(JSON.stringify(error));
      });
  })

})

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