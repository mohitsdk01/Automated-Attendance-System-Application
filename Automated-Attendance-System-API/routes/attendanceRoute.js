const router = require("express").Router();
const attendanceController = require("../controllers/attendanceController.js");

router.all("/", attendanceController.home);

router.post("/markAttendance", attendanceController.markAttendance);

router.post("/getAttendance",attendanceController.getAttendance);

module.exports = router;