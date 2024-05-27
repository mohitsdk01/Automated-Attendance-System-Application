const mongoose = require("mongoose");

mongoose.set("strictQuery", false);
const database = mongoose.connect(process.env.DB_URL, (err) => {
  if (err) {
    console.log("error while connecting to db");
  } else {
    console.log("connected to the DB");
  }
});

module.exports = database;
