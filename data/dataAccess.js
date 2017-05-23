var User = require('../model/User')
    , MongoClient = require('mongodb').MongoClient
    , assert = require('assert')
    , url = 'mongodb://localhost:27017/usersdata'
    , async = require('async')

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
    // Find some documents
    var collection = db.collection('userdata');

    collection.find().forEach(function (item) {
        callback(item);
    })
}

module.exports = db;