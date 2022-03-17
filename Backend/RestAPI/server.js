import express from "express";
import mongoose from "mongoose";

import Users from "./routers/manager.js";
import Messages from "./routers/messages.js";

const PORT = 3000;
const DBURL = "mongodb://admin:Cstrike1@localhost:27017/voicebeam?authSource=admin";
export const SALTROUNDS = 10;

await mongoose.connect(DBURL).then(() => console.log(`Connetect to DB "${DBURL}"!`));

const app = express();
app.use(express.json());

app.use("/manager",Users);
app.use("/messages",Messages);

app.listen(PORT,() => {
    console.log(`Running on Port ${PORT}!`);
});