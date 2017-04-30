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
db.test.mapReduce( 
function(){if(this.awards!=null)
    for(var i=0;i<this.awards.length;i++) 
        emit(this.awards[i].award,1);}, 
function(key,values){return Array.sum(values)},
{out:"award"})
db.award.find();

// 2) Write an aggregation query that groups by the birth year, i.e., the year
// within the “birth” field, are report an array of _ids for each birth year.
// (Use Aggregate mechanism)
db.test.aggregate(
{$match:{birth:{$exists:true}}},
{$group:{_id:{$year:"$birth"},
idarray:{$addToSet:"$_id"}}
})


// 3) Report the document with the smallest and largest _ids. You first need to
// find the values of the smallest and largest, and then report their documents.
db.test.find().sort({_id:1}).limit(1);
db.test.find().sort({_id:-1}).limit(1);


// 4) Use the $text operator to search for and report all documents containing
// “Turing Award” as one sentence (not separate keywords).
db.test.createIndex({"$**":"text"})
db.test.find({$text:{$search:"\"Turing Award\""}})


// 5) Use the $text operator to search for and report all documents containing
// either “Turing” or “National Medal”.
var ids= []
db.test.find({$text:{$search:"Turing" }}).forEach(function(doc){ids.push(doc._id);})
db.test.find({$text:{$search:"National Medal" }}).forEach(function(doc){ids.push(doc._id);})
db.test.find({_id:{$in:ids}})
