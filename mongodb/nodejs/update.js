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

	// Update the documents into the collection - use updateMany for mulitple
	// and updateOne for single update (if multiple then first will be updated)
	collection.updateOne({name: 'Havisha'}, {$set: {no:46}}, function(err, result) {
		if(err) {
			console.log(err);
		}
		else {
			console.log("Document updated successfully", result.length, result);
		}
		client.close();	
	});
});