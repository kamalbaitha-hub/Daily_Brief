/**
 * Daily Brief Scheduler
 * Runs daily at 7:00 AM IST (Indian Standard Time, UTC+5:30)
 * Triggers news fetching, OpenAI summarization, and SQLite persistence.
 */

const { runDailyUpdate } = require('./fetcher');

let cronJob = null;

function initScheduler() {
  const cronTime = '0 7 * * *'; // 7:00 AM every day
  const timezone = process.env.TZ || 'Asia/Kolkata';

  console.log(`[Scheduler] Initializing Daily Brief scheduler for 7:00 AM daily (${timezone})...`);

  try {
    let cron;
    try {
      cron = require('node-cron');
    } catch (e) {
      cron = null;
    }

    if (cron && typeof cron.schedule === 'function') {
      cronJob = cron.schedule(
        cronTime,
        async () => {
          console.log(`[Scheduler] 7:00 AM IST Triggered! Starting daily news pipeline...`);
          try {
            await runDailyUpdate();
          } catch (err) {
            console.error(`[Scheduler] Daily update job encountered error:`, err);
          }
        },
        {
          timezone: timezone,
          scheduled: true
        }
      );
      console.log(`[Scheduler] node-cron registered for 7:00 AM (${timezone}).`);
    } else {
      // Fallback timer calculation for 7:00 AM IST without external package
      setupDailyIntervalFallback(timezone);
    }
  } catch (err) {
    console.warn(`[Scheduler] Could not initialize node-cron (${err.message}). Setting up fallback timer.`);
    setupDailyIntervalFallback(timezone);
  }
}

function setupDailyIntervalFallback(timezone) {
  function getMsUntilNext7AmIST() {
    const now = new Date();
    // Calculate current time in IST (UTC + 5.5 hours)
    const istOffsetMs = 5.5 * 60 * 60 * 1000;
    const nowIst = new Date(now.getTime() + (now.getTimezoneOffset() * 60 * 1000) + istOffsetMs);

    const targetIst = new Date(nowIst);
    targetIst.setHours(7, 0, 0, 0);

    if (nowIst >= targetIst) {
      targetIst.setDate(targetIst.getDate() + 1);
    }
    return targetIst.getTime() - nowIst.getTime();
  }

  const msUntil7Am = getMsUntilNext7AmIST();
  const hoursUntil = (msUntil7Am / (1000 * 60 * 60)).toFixed(2);
  console.log(`[Scheduler] Next 7:00 AM IST run scheduled in ${hoursUntil} hours (via timer fallback).`);

  setTimeout(async () => {
    console.log(`[Scheduler] 7:00 AM IST Timer triggered! Starting news pipeline...`);
    try {
      await runDailyUpdate();
    } catch (e) {
      console.error('[Scheduler] Error during scheduled update:', e);
    }
    // Repeat every 24 hours
    setInterval(async () => {
      console.log(`[Scheduler] 24h Daily trigger started.`);
      await runDailyUpdate();
    }, 24 * 60 * 60 * 1000);
  }, msUntil7Am);
}

module.exports = {
  initScheduler
};
