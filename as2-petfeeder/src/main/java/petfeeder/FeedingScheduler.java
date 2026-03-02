package petfeeder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Schedules recurring automatic feedings while the program is running.
 */
public class FeedingScheduler {

    private final PetFeeder petFeeder;
    private final ScheduledExecutorService executor;
    private ScheduledFuture<?> currentTask;

    /**
     * Creates a feeding scheduler bound to a specific PetFeeder.
     * @param petFeeder The PetFeeder instance to use for dispensing meals.
     */
    public FeedingScheduler(PetFeeder petFeeder) {
        this.petFeeder = petFeeder;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts a recurring feeding schedule. If a schedule is already active,
     * it will be replaced by the new one.
     * @param mealPlanIndex Index of the meal plan to dispense.
     * @param periodSeconds Interval in seconds between feedings.
     */
    public synchronized void scheduleRecurringFeeding(final int mealPlanIndex,
                                                      long periodSeconds) {
        if (currentTask != null && !currentTask.isCancelled()) {
            currentTask.cancel(false);
        }

        currentTask = executor.scheduleAtFixedRate(new FeedingJob((mealPlanIndex)),
                periodSeconds, periodSeconds, TimeUnit.SECONDS);
    }

    /**
     * Stops the current feeding schedule, if any.
     */
    public synchronized void stop() {
        if (currentTask != null) {
            currentTask.cancel(false);
            currentTask = null;
        }
    }

    /**
     * Returns true if there is an active schedule.
     * @return boolean
     */
    public synchronized boolean hasActiveSchedule() {
        return currentTask != null && !currentTask.isCancelled();
    }

    /**
     * Shuts down the underlying executor service.
     * Should be called before the program exits if possible.
     */
    public void shutdown() {
        executor.shutdownNow();
    }

    public ScheduledFuture<?> getCurrentTask() { return currentTask; }

    public class FeedingJob implements Runnable {
        private final int mealIndex;

        public FeedingJob(int mealIndex) {
            this.mealIndex = mealIndex;
        }

        @Override
        public void run() {
            currentTask = null;
            try {
                boolean dispensed = petFeeder.dispenseMeal(mealIndex);
                if (!dispensed) {
                    System.out.println("[Scheduler] Scheduled meal could not be dispensed...");
                } else {
                    MealPlan[] plans = petFeeder.getMealPlans();
                    String name = (plans[mealIndex] != null) ? plans[mealIndex].getName() : "(unknown meal)";
                    System.out.println("[Scheduler] Dispensed scheduled meal: " + name);
                }
            } catch (Exception e) {
                System.out.println("[Scheduler] Error during scheduled feeding: " + e.getMessage());
            }
        }
    }


}
