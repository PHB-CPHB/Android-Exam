var User = require('../model/User')
    , MongoClient = require('mongodb').MongoClient
    , assert = require('assert')
    , url = 'mongodb://localhost:27017/usersdata'

var db = {};

db.addUser = function (newUser, db, callback) {

    // Get the documents collection
    var collection = db.collection('userdata');
    // Insert some documents
    collection.insertOne(newUser, function (err, result) {
        assert.equal(err, null);
        console.log("Inserted 1 documents into the collection:", result);
        callback(result);
    });

}

db.findUsers = function (db, callback) {
    // Get the documents collection
    var result = [];
    var users = [];
    // Find some documents
    MongoClient.connect(url, function (err, db) {
        assert.equal(null, err);
        var collection = db.collection('userdata');
        users = collection.find().forEach(function (item) {
            result.push(item);
        });

        return result;
        console.log(result);
    })


}



db.getAll = function () {

}

module.exports = db;