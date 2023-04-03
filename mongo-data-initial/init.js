// Create user
dbAdmin = db.getSiblingDB("review-service");
dbAdmin.createUser({
  user: "reviewserviceadmin",
  pwd: "reviewservicepwd",
  roles: [{ role: "readWrite", db: "review-service" }],
});

// Authenticate user
dbAdmin.auth({
    user: "reviewserviceadmin",
    pwd: "reviewservicepwd",
});

// Create DB and collection
db.createCollection("review", { capped: false });