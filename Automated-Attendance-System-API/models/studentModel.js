const mongoose = require("mongoose");

const studentSchema = new mongoose.Schema({
  studentId: String,
  name: String,
  email: String,
  mobileNumber: String,
  branch: String,
  class: String,
  rollNo: Number,
  macAddress: String,
  password: String,
});

module.exports = mongoose.model("Student", studentSchema);
