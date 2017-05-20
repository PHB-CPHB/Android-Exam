var express = require('express')
  , bodyParser = require('body-parser')
  , app = express()
  , firebase = require("firebase")
  , admin = require("firebase-admin")
  , serviceAccount = require("./firebase-service-key.json")

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