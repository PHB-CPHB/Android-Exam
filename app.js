var express = require('express')
  , bodyParser = require('body-parser')
  , app = express()
  , firebase = require("firebase")

// Initialize Firebase
// TODO: Replace with your project's customized code snippet
var admin = require("firebase-admin");
var serviceAccount = require("./firebase-service-key.json");

admin.initializeApp({
  credential: admin.credential.cert({
    projectId: serviceAccount.project_id,
    clientEmail: serviceAccount.client_email,
    privateKey: serviceAccount.private_key
  }),
  databaseURL: "https://exam-app-e819f.firebaseio.com/"
});

app.use(bodyParser.json())

app.post('/', function (req, res) {
  var content = req.body;
  var finalResponse = "init";

  var payload = {
    data: {
      uEmail: content.email,
      uToken: content.token,
      uMsg: content.msg
    }
  }

  admin.messaging().sendToDevice("04XWkkzws0Uu1b86yz3g29HMEdG2", payload)
    .then(function (response) {
      // See the MessagingDevicesResponse reference documentation for
      // the contents of response.
      console.log("Successfully sent message:", response);
      finalResponse = response
    })
    .catch(function (error) {
      console.log("Error sending message:", error);
      finalResponse = response
    });

  /*database.ref("https://exam-app-e819f.firebaseio.com/users/").child("testUser").set({
    email: uEmail,
    token: uToken,
    msg: uMsg
  }) */

  res.send(finalResponse)
})

module.exports = app;