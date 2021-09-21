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

	// Create the documents to insert 
	var std1={'name' : 'Haasya', 'no' :'B00778'}
	var std2={'name': 'Havisha', 'no': 'B00779'}

	 // Get the collection object into which the documents need to be inserted
	 var collection= db.collection('students');

	// Insert the documents into the collection - insertMany for mulitple documents 
	// and insertOne for single document
	collection.insertMany([std1, std2], function(err, result) {
		if(err) {
			console.log(err);
		}
		else {
			console.log("Document inserted successfully", result.length, result);
		}
		client.close();	
	});
});