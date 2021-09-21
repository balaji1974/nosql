// Import node package
var MongoClient=require('mongodb').MongoClient, assert=require('assert');

// connection url 
var url ='mongodb://localhost:27017';

// connect to the server
MongoClient.connect(url,function(err, client) {
	assert.equal(null,err);
	console.log("Connected to MongoDB server successfully");

	// Get the database object where you need to perform the CRUD operations 
	var db=client.db('testdb');

	 // Get the collection object into which the documents need to update
	 var collection= db.collection('students');

	// find the document from the collection
	collection.find({name: 'Havisha'}).toArray(function(err, result) {
		if(err) {
			console.log(err);
		}
		else if (result.length) {
			console.log("Documents found", result);
		}
		else {
			console.log("No documents were found");
		}
		client.close();	
	});
});