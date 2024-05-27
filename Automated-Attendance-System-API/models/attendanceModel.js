const mongoose = require("mongoose");

const attendanceSchema = new mongoose.Schema({
    studentId: String,
    subjects : {}
},{
  versionKey : false,
});

function createModel(collectionName){
  let newCollection = mongoose.model(collectionName, attendanceSchema,collectionName);

  return newCollection;
}

module.exports = createModel;