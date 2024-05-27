require("dotenv").config();
const http = require("http");
const app = require("./app");
const server = http.createServer(app);
require("./database/db");

const port = process.env.PORT;

server.listen(port, () => {
  console.log(`server started http://localhost:${port}`);
});
