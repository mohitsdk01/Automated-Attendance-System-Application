const createModel = require("../models/attendanceModel");

exports.home = async (req, res) => {
    return res.send({ message: "Attendance API / Endpoints Is Working Fine." });
  };

exports.getAttendance = async (req,res) => {

  let collectionName = req.body.class + req.body.branch;

  createModel(collectionName).find({}, (err, students) => {
          if (!err) {
            return res.status(200).send({students});
          } else {
            return res.status(500).send(err);
          }
        });
};

exports.markAttendance = async (req,res) => {

  console.log(req.body);

  let collectionName = req.body.class + req.body.branch;
  let subject = req.body.subject;
  let studentIds = req.body.studentIds.split(",");
  createModel(collectionName).updateMany({studentId:studentIds},{$inc:{[`subjects.${subject}`]:+1}}, (err, students) => {
        if (!err) {
          return res.status(200).send({message:"Attendance Marked!"});
        } else {
          return res.status(500).send(err);
        }
      });

};
