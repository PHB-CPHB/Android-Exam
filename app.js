var express = require('express')
  , bodyParser = require('body-parser')
  , app = express()
  , firebase = require("firebase")
  , admin = require("firebase-admin")
  , serviceAccount = require("./firebase-service-key.json")
  , mongodb = require('mongodb')
  , assert = require('assert')
  , url = 'mongodb://localhost:12345/usersdata'
  , MongoClient = mongodb.MongoClient
  , connection = MongoClient.connect(url);

var config = {
  apiKey: "AIzaSyA6M4DZqpVed0Z9W7qDSof1S-wNCUGO1as",
  authDomain: "exam-app-e819f.firebaseapp.com",
  databaseURL: "https:exam-app-e819f.firebaseio.com",
};

firebase.initializeApp(config);

admin.initializeApp({ serviceAccount });

app.use(bodyParser.json())

/* TODO

  /register
    - tilføj firebase register kald
    - gem i mongodb hvis alt ok
    - returner resultat til app

  /updatetoken

  /send

  /match (return user with either phone number or email)

*/



app.post('/register', (req, res) => {
  var content = req.body;
  var newUser = {
    email: content.email,
    token: content.token,
    phone: content.phone,
    displayName: content.displayName
  }

  var error = {
    errorMessage: "Email already exists",
    email: content.email
  }

  firebase.auth().createUserWithEmailAndPassword(content.email, content.password).then(() => {
    connection.then(function (db) {
      db.collection('userdata').insertOne(newUser, function (err, result) {
        if (err) res.end(JSON.stringify(err))
        res.end(JSON.stringify(result))
      })
    });
  }).catch(function (error) {
    // Handle Errors here.
    var error = {
      errorCode: error.code,
      errorMessage: error.message
    }

    res.end(JSON.stringify(error))
  });

  //emailExists(content.email, (valid) => {
  //console.log("VALID:", valid)
  //if (valid) {
  //} else {
  //  res.end(JSON.stringify(error))
  //}
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
          //console.log("Err", err, "Count:", count, "Status", status)
          res.end(JSON.stringify(count));
      });
  })
})

app.post('/match', (req, res) => {

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