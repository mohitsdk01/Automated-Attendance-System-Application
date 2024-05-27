const express = require("express");
const app = express();
const studentRoute = require("./routes/studentRoute");
const attendanceRoute = require("./routes/attendanceRoute");

// middleware
app.use(express.json());

app.use("/api/student", studentRoute);
app.use("/api/attendance",attendanceRoute);

module.exports = app;
