// 0) Connect to Mongo
conn = new Mongo();
db = conn.getDB("local");


// 1) Create a collection named “test”, and insert into this collection the documents
// found in this link (10 documents):
// http://docs.mongodb.org/manual/reference/bios-example-collection/
// Run from terminal
// mongoimport --db local --collection test --drop --file ./data.json --jsonArray


// 2) Write a CRUD operation(s) that changes the _id of “John McCarthy” to value 2.
db.test.update(
	  {
		"name": {
            "first": "John",
            "last": "McCarthy"
        }
	  },
	{
		  $set: {"_id":2}
	}
);


// 3) Write a CRUD operation(s) that inserts the following new records into the
// collection:
new_bios = [
	{
		"_id" : 20, 
		"name" : {
			"first" : "Alex",
			"last" : "Chen"
		},
		"birth" : ISODate("1933-08-27T04:00:00Z"),
		"death" : ISODate("1984-11-07T04:00:00Z"),
		"contribs" : ["C++", "Simula"],
		"awards" : [
			{
				"award" : "WPI Award",
				"year" : 1977,
				"by" : "WPI"
			}
		]
	},
 	{
		"_id" : 30,
		"name" : {
			"first" : "David",
			"last" : "Mark"
		},
		"birth" : ISODate("1911-04-12T04:00:00Z"),
		"death" : ISODate("2000-11-07T04:00:00Z"),
		"contribs" : ["C++", "FP", "Lisp"],
		"awards" : [
			{
				"award" : "WPI Award",
				"year" : 1963,
				"by" : "WPI"
			},
			{
				"award" : "Turing Award",
				"year" : 1966,
				"by" : "ACM"
			}
		]
	}
];
db.test.insert(new_bios);


// 4) Report all documents of people who got a “Turing Award” after 1976
cursor = db.test.find(
	{
		awards: {
        	$elemMatch: {
            	award: "Turing Award",
            	year: { $gt: 1976 }
        	}
      	}
	}
);
print("========================================================================");
print("4");
print("========================================================================");
cursor.forEach(printjson);


// 5) Report all documents of people who got less than 3 awards or have contribution
// in “FP”
cursor = db.test.find(
	{
		$or: [
			{ $where: "this.awards ? this.awards.length < 3 : false" },
			{ contribs: "FP" }
		]
	}
);
print("========================================================================");
print("5");
print("========================================================================");
cursor.forEach(printjson);


// 6) Report the contributions of “Dennis Ritchie” (only report the name and the
// contribution array)
cursor = db.test.find(
	{
        name: {
            "first": "Dennis",
            "last": "Ritchie"
        }
    },
    {
    	_id: 0,
    	name: 1,
    	contribs: 1
    }
);
print("========================================================================");
print("6");
print("========================================================================");
cursor.forEach(printjson);


// 7) Update the document of “Guido van Rossum” to add “OOP” to the contribution list.
db.test.update(
	{
        name: {
            "first": "Guido",
            "last": "van Rossum"
        }
	},
   	{
		$push: { contribs: "OOP" }
   }
);


// 8) Insert a new filed of type array, called “comments”, into the document of “Alex Chen”
// storing the following comments: “He taught in 3 universities”, “died from cancer”,
// “lived in CA”
db.test.update(
	{
        name: {
            "first": "Alex",
            "last": "Chen"
        }
	},
   	{
		$set: {
			comments: [
				"He taught in 3 universities",
				"died from cancer",
				"lived in CA"
			]
		}
   }
);


// 9) For each contribution by “Alex Chen”, say X, list the peoples’ names (fisrt and last)
// who have contribution X. E.g., Alex Chen has two contributions in “C++” and “Simula”.
// Then, the output should be similar to: a.
// { Contribution: “C++”, People: [{first: “Alex”, last: “Chen”},
// {first: “David”, last: “Mark”}]}, { Contribution: “Simula”, ....}
var contributions=[];
result = db.test.find(
	{
        name: {
            "first": "Alex",
            "last": "Chen"
        }
    }
).forEach(
	function(u){
		contributions=u.contribs
	}
	);
cursor = db.test.aggregate(
        [
        	{$unwind:"$contribs"},
		{$match:{'contribs':{$in: contributions}}},
		{$group:{_id: "$contribs",people:{$push:"$name"}}}
            ]
);
print("========================================================================");
print("9");
print("========================================================================");
cursor.forEach(printjson);


// 10) Report all documents where the first name matches the regular expression “Jo*”,
// where “*” means any number of characters. Report the documents sorted by the last name.
cursor = db.test.find(
	{
        "name.first": { $regex: /^Jo/ }
    },
    {
    	_id: 0,
    	name: 1
    }
).sort({ 'name.last': 1 });
print("========================================================================");
print("10");
print("========================================================================");
cursor.forEach(printjson);


// 11) Report the distinct organization that gave awards. This information can be found
// in the “by” field inside the “awards” array. The output should be an array of the
// distinct values, e.g., [“wpi’, “acm’, ...]
cursor = db.test.distinct(
	'awards.by'
)
print("========================================================================");
print("11");
print("========================================================================");
cursor.forEach(printjson);

// 12) Delete from all documents the “death” field.
db.test.update(
	{},
	{
		$unset: {
			death: 1
		}
	},
	{
		multi: true
	}
);


// 13) Delete from all documents any award given on 2011.
match_year = 2011
db.test.update(
	{
		"awards.year": match_year
    },
    {
		$pull: {
			awards: {
				year: match_year
			}
		}
    },
    {
		multi: true
	}
);


// 14) Update the award of document _id =30, which is given by WPI, and set the year
// to 1965.
db.test.update(
   	{
   		_id: 30,
   		"awards.by": "WPI"
   	},
   	{
   		$set: {
   			"awards.$.year": 1965
   		}
   	}
);

// 15) Add (copy) all the contributions of document _id = 3 to that of document _id = 30
doc = db.test.findOne(
	{
        _id: 3
    },
    {
    	_id: 0,
    	contribs: 1
    }
);
contribs = doc.contribs;

db.test.update(
	{
        _id: 30
	},
   	{
		$push: { contribs: { $each: contribs } }
   }
);


// 16) Report only the names (first and last) of those individuals who won at least two
// awards in 2001.
cursor=db.test.aggregate(
	[
		{$unwind:"$awards"},
		{$match:{'awards.year':2001}},
		{$group:{_id: "$name",count:{$sum:1}}},
                {$match:{count:{$gte: 2}}},
                {$project:{name:1}}
		]
	);

print("========================================================================");
print("16");
print("========================================================================");
cursor.forEach(printjson);

// 17) Report the document with the largest id. First, you need to find the largest _id
// (using a CRUD statement), and then use that to report the corresponding document.
cursor = db.test.find().sort({ _id: -1 }).limit(1);
max_id = cursor.next()._id
doc = db.test.findOne(
	{
		_id: max_id
    }
);
print("========================================================================");
print("17");
print("========================================================================");
print(doc);


// 18) Report only one document where one of the awards is given by “ACM”.
cursor = db.test.find(
	{
		awards: {
        	$elemMatch: {
            	by: "ACM"
        	}
      	}
	}
);
print("========================================================================");
print("18");
print("========================================================================");
cursor.forEach(printjson);

// 19) Delete the documents inserted in Q3, i.e., _id = 20 and 30.
db.test.remove({_id: { $in: [20, 30] }});

// 20) Report the number of documents in the collection.
print("========================================================================");
print("20");
print("========================================================================");
print(db.test.count());
