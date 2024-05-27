const router = require("express").Router();
const studentController = require("../controllers/studentController.js");

router.all("/", studentController.home);

router.get("/getAllStudents", studentController.getAllStudents);

router.post("/signup", studentController.signUp);

router.post("/login", studentController.login);

router.delete("/deleteByStudentId", studentController.deleteByStudentId);

router.post("/getStudentNamesFromMac", studentController.getStudentNamesFromMac);

module.exports = router;