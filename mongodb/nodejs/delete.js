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

	// delete the documents from the collection - use deleteMany for mulitple
	// and deleteOne for single delete (if multiple then first will be deleted)
	collection.deleteMany({name: 'Haasya'}, function(err, result) {
		if(err) {
			console.log(err);
		}
		else {
			console.log("Documents deleted" + result);
		}
		
		client.close();	
	});
});