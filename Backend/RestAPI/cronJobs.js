import cron from "node-cron";
import User from "./models/user.js";
import Logger from "./classes/Logger.js";

export default function startCronJobs() {
    // Delete Invalid Sessions every hour
    cron.schedule("0 0 */1 * * *", async () => {
        await deleteInvalidSessions()
    });
    deleteInvalidSessions();

    Logger.writeServerLog("", `Cron Jobs started!`);
}

async function deleteInvalidSessions() {
    await User.updateMany({},
        {
            $pull: {
                sessions: {
                    expiresAt: {
                        $lt: Date.now()
                    }
                }
            }
        });
}