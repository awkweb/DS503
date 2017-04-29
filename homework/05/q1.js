// Question 1
// ===========================================================================

// 0) Connect to Mongo and add data
conn = new Mongo();
db = conn.getDB("local");

// PARENT REFERENCES
new_categories = [
 	{ _id: "MongoDB", parent: "Databases" },
 	{ _id: "dbm", parent: "Databases" },
 	{ _id: "Databases", parent: "Programming" },
	{ _id: "Languages", parent: "Programming" },
 	{ _id: "Programming", parent: "Books" },
	{ _id: "Books", parent: null },
];
db.categories.insert(new_categories);

// CHILD REFERENCES
new_categories2 = [
	{ _id: "MongoDB", children: [] },
 	{ _id: "dbm", children: [] },
	{ _id: "Databases", children: ["dbm", "MongoDB"] },
	{ _id: "Languages", children: [] },
 	{ _id: "Programming", children: ["Databases", "Languages"] },
 	{ _id: "Books", children: ["Programming"] },
 ];
db.categories2.insert(new_categories2);


// 1) Assume we model the records and relationships in Figure 1 using the
// Parent-Referencing model (Slide 49 in MongoDB-2). Write a query to report
// the ancestors of “MongoDB”. The output should be an array containing values
// [{Name: “Databases”, Level: 1}, {Name: “Programming”, Level: 2},
// {Name: “Books”, Level: 3}]
var stack = [],
	ancestors = [],
	level = 0;
var category = db.categories.findOne({_id: "MongoDB"});
stack.push(category);
while (stack.length > 0) {
	level++;
	var current = stack.pop();
	var parent = db.categories.findOne({_id: current.parent});
	if (parent) {
		ancestors.push({ Name: parent._id, Level: level });
		stack.push(parent);
	}
}
print("====================================================================");
print("1");
print("====================================================================");
print(tojson(ancestors));


// 2) Assume we model the records and relationships in Figure 1 using the
// Parent-Referencing model (Slide 49 in MongoDB-2). You are given only the root
// node, i.e., _id = “Books”, write a query that reports the height of the tree.
// (It should be 4 in our case).
var stack = [],
	visitedIds = {},
	level = 0;
var category = db.categories.findOne({_id: "Books"});
stack.push(category);
while (stack.length > 0) {
	var current = stack.pop();
	var children = db.categories.find({parent: current._id});

	if (!(current.parent in visitedIds)) {
		level++;
		visitedIds[current.parent] = 1;
	}

	while (children.hasNext()) {
		var child = children.next();
		stack.push(child);
	}
}
print("====================================================================");
print("2");
print("====================================================================");
print(level);


// 3) Assume we model the records and relationships in Figure 1 using the
// Child-Referencing model (Slide 54 in MongoDB-2). Write a query to report
// the parent of “dbm”.
parent = db.categories2.findOne(
	{ children: { $in: ["dbm"] }}
);
print("====================================================================");
print("3");
print("====================================================================");
print(tojson(parent));


// 4) Assume we model the records and relationships in Figure 1 using the
// Child-Referencing model (Slide 54 in MongoDB-2). Write a query to report
// the descendants of “Books”. The output should be an array containing values
// [“Programming”, “Languages”, “Databases”, “MongoDB”, “dbm”]
var stack = [],
	descendants = [];
var category = db.categories2.findOne({_id: "Books"});
stack.push(category);
while (stack.length > 0) {
	var current = stack.pop();
	var children = db.categories2.find({_id: {$in: current.children}});

	while (children.hasNext()) {
		var child = children.next();
		descendants.push(child._id);
		stack.push(child);
	}
}
print("====================================================================");
print("4");
print("====================================================================");
print(tojson(descendants));


// 5) Assume we model the records and relationships in Figure 1 using the
// Child-Referencing model (Slide 54 in MongoDB-2). Write a query to report
// the siblings of “Databases”.
var category = db.categories2.findOne({_id: "Databases"});
var children = [category._id];
var parent = db.categories2.findOne({children: {$in: children}});
cursor = db.categories2.find(
	{
		_id: {
			$in: parent.children,
			$ne: category._id
		}
	}
);
print("====================================================================");
print("5");
print("====================================================================");
cursor.forEach(printjson);
