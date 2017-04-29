// Question 2
// ===========================================================================

// 0) Connect to Mongo and add data
conn = new Mongo();
db = conn.getDB("local");

// Create a collection named “test”, and insert into this collection the documents
// found in this link (10 documents):
// http://docs.mongodb.org/manual/reference/bios-example-collection/
// Run from terminal
// mongoimport --db local --collection test --drop --file ./data.json --jsonArray


// 1) Write an aggregation query that groups by the award name, i.e., the “award”
// field inside the “awards” array, and reports the count of each award.
// (Use Map-Reduce mechanism)
cursor = db.test.findOne(
	{'awards.by': "ACM"}	
);
print("========================================================================");
print("18");
print("========================================================================");
cursor.forEach(printjson);


// 2) Write an aggregation query that groups by the birth year, i.e., the year
// within the “birth” field, are report an array of _ids for each birth year.
// (Use Aggregate mechanism)

// 3) Report the document with the smallest and largest _ids. You first need to
// find the values of the smallest and largest, and then report their documents.


// 4) Use the $text operator to search for and report all documents containing
// “Turing Award” as one sentence (not separate keywords).


// 5) Use the $text operator to search for and report all documents containing
// either “Turing” or “National Medal”.
