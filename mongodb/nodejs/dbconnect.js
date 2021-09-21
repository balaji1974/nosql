// Import node package
var MongoClient=require('mongodb').MongoClient, assert=require('assert');
// connection url 
var url ='mongodb://localhost:27017/testdb';
// connect to the server
MongoClient.connect(url,function(err, db) {
	if(err) {
		console.log(err);
	}
	else {
		console.log("Connected to MongoDB server successfully");
		db.close();
	}
});