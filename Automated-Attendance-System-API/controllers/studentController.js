const Student = require("../models/studentModel");
const createModel = require("../models/attendanceModel");

exports.signUp = async (req, res) => {
  // check whether student with current email id does not exist
  Student.findOne({ mobileNumber: req.body.mobileNumber }, (err, doc) => {
    if (!err && doc) {
      return res.status(200).send({ message: "Mobile Number Already Registered." });
    } else {
      Student.countDocuments({}, (err, count) => {
        if (err) {
          console.log("error while counting the documents" + error);
        } else {
          const zeroPad = (num, places) => String(num).padStart(places, "0");
          //console.log(zeroPad(5, 2)); // "05"
          //console.log(zeroPad(5, 4)); // "0005"

          const _class = req.body.class;
          const branch = req.body.branch;
          const number = zeroPad(count + 1, 4);
          const studentId = `${_class}${branch}${number}`;

          Student.create({
            studentId: studentId,
            name: req.body.name,
            email: req.body.email,
            mobileNumber: req.body.mobileNumber,
            branch: req.body.branch,
            class: req.body.class,
            rollNo: req.body.rollNo,
            macAddress: req.body.macAddress,
            password: req.body.password,
          });

          /*
          if(branch=="IT" && class=="BE"){
            define their subjects
          }
          */
          
        //   Attendance.create({
        //     studentId: studentId,
        //     subjects :{
        //     Cyber_Security: 0,
        //     Soft_Computing: 0,
        //     LRPS: 0,
        //     Data_Analytics: 0
        //     }
        // });

        createAttendance(_class+branch,studentId,branch,_class);

          return res.status(201).send({ studentId: studentId });
        }
      });
    }
  });
};

function createAttendance(collectionName,studentId,branch,_class){

  if(branch=="CS" && _class=="SE"){
    createModel(collectionName).create({
      studentId: studentId,
      subjects :{
      BIO: 0,
      COA: 0,
      DE: 0,
      FA: 0,
      DSA:0
      }
  });
  }else if((branch=="CS" || branch=="IT") && _class=="BE"){
    createModel(collectionName).create({
      studentId: studentId,
      subjects :{
      Cyber_Security: 0,
      Soft_Computing: 0,
      LRPS: 0,
      Data_Analytics: 0
      }
});
  }


}

exports.login = async (req, res) => {
  const studentId = req.body.studentId;
  const password = req.body.password;

  Student.findOne({ studentId: studentId, password: password }, (err, doc) => {
    if (err) {
      console.log("error while finding the student in login functionaliy");
      return res.status(500);
    } else if (doc) {
      return res.status(200).send({ message: "student exist" });
    } else {
      return res.status(200).send({ message: "student does not exists" });
    }
  });
};

exports.getAllStudents = async (req, res) => {
  Student.find({}, (err, students) => {
    if (!err) {
      return res.status(200).send({students});
    } else {
      return res.status(500).send(err);
    }
  }).select('-_id -password -__v');
};

exports.home = async (req, res) => {
  return res.send({ message: "Student API / Endpoints Is Working Fine." });
};

exports.getStudentNamesFromMac = async (req, res) => {

  // console.log(req);

  let studentIds = req.body.studentIds.split(",");
  Student.find({studentId: studentIds, branch: req.body.branch, class: req.body.class}, (err, students) => {
    if (!err) {
      let studentNamesFromMac = [];
      for(let i=0;i<students.length;i++){
        studentNamesFromMac.push(students[i].rollNo+" : "+students[i].name);
      }
      studentNamesFromMac.sort();
      return res.status(200).send({studentNamesFromMac});
    } else {
      return res.status(500).send({err});
    }
  });
};

exports.deleteByStudentId = async (req, res) => {
  Student.deleteOne({studentId : req.body.studentId}, (err, response) => {
    if(err){
      return res.status(500).send({message:"Error While Deleting.."});
    }else{
      return res.status(204).send({message:"Deleted Successfully!"});
    }

  });
};