// Question 1
// ===========================================================================

// 0) Connect to Mongo and add data
conn = new Mongo();
db = conn.getDB("local");

new_items = [
	{ _id: "MongoDB", parent: "Databases" },
	{ _id: "dbm", parent: "Databases" },
	{ _id: "Databases", parent: "Programming" },
	{ _id: "Languages", parent: "Programming" },
	{ _id: "Books", parent: null},
];
db.tree.insert(new_items);

// 1) Assume we model the records and relationships in Figure 1 using the
// Parent-Referencing model (Slide 49 in MongoDB-2). Write a query to report
// the ancestors of “MongoDB”. The output should be an array containing values
// [{Name: “Databases”, Level: 1}, {Name: “Programming”, Level: 2},
// {Name: “Books”, Level: 3}]

