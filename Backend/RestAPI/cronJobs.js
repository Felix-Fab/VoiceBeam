import cron from "node-cron";
import User from "./models/user.js";

export default function startCronJobs() {
    // Delete Invalid Sessions every hour
    cron.schedule("0 0 */1 * * *", async () => {
        await deleteInvalidSessions()
    });
    deleteInvalidSessions();

    console.log("Cron-Jobs Started!");
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