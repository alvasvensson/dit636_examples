package petfeeder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;


public class FeedingSchedulerTest {
    private PetFeeder petFeeder;
    private ScheduledFuture<?> currentTask;
    private FeedingScheduler feedingScheduler;
    private MealPlan mealPlan;
    private MealPlan mealPlan2;
    private int mealIndex0;
    private int mealIndex1;
    private int mealIndex2;
    private long periodicity;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream stdout = System.out;

    @BeforeEach
    public void setUp() throws Exception {
        petFeeder = new PetFeeder();
        feedingScheduler = new FeedingScheduler(petFeeder);

        mealPlan = new MealPlan();
        mealPlan.setName("Breakfast");
        mealPlan.setAmtKibble("5");
        mealPlan.setAmtTreats("5");
        mealPlan.setAmtWater("5");
        mealPlan.setAmtWetFood("5");

        mealPlan2 = new MealPlan();
        mealPlan2.setName("");
        mealPlan2.setAmtKibble("5");
        mealPlan2.setAmtTreats("5");
        mealPlan2.setAmtWater("5");
        mealPlan2.setAmtWetFood("5");

        petFeeder.addMealPlan(mealPlan);
        petFeeder.addMealPlan(mealPlan2);

        mealIndex0 = 0;
        mealIndex1 = 1;
        mealIndex2 = 2;

        periodicity = 40;
    }

    @AfterEach
    public void cleanUp() throws Exception {
        System.setOut(stdout);
    }

    /**
     * .scheduleRecurringFeeding method
     **/
    @Test
    public void CurrentFeedingScheduleShouldBeUpdated() {
        ScheduledFuture<?> oldTask;
        ScheduledFuture<?> newTask;

        feedingScheduler.scheduleRecurringFeeding(mealIndex0, periodicity);
        oldTask = feedingScheduler.getCurrentTask();

        feedingScheduler.scheduleRecurringFeeding(mealIndex0, periodicity);
        newTask = feedingScheduler.getCurrentTask();

        assertNotSame(oldTask, newTask, "oldTask and newTask should not be same to show " +
                "that a new currentTask (ie. feeding schedule) is created if there already exists a currentTask");
    }

    @Test
    public void CurrentTaskShouldNotBeNull() {
        feedingScheduler.scheduleRecurringFeeding(mealIndex0, periodicity);

        assertNotNull(feedingScheduler.getCurrentTask(), "currentTask should not be null when a " +
                "feeding schedule is created");
    }

    @Test
    public void ThrowExceptionWhenPeriodicityIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            feedingScheduler.scheduleRecurringFeeding(mealIndex0, 0);
        }, "IllegalArgumentException is thrown when argument periodicity " +
                "equals 0 seconds (edge case)");
    }

    @Test
    public void ThrowExceptionWhenPeriodIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            feedingScheduler.scheduleRecurringFeeding(mealIndex0, -60);
        }, "IllegalArgumentException is thrown when argument periodicity is a " +
                "negative integer");
    }


    /**
     * .stop() method
     */

    @Test
    public void CurrentTaskShouldRemainNull() {
        currentTask = feedingScheduler.getCurrentTask();
        assertNull(currentTask, "CurrentTask should be null at the start of the test because a feeding" +
                "schedule has not been created");

        feedingScheduler.stop();

        currentTask = feedingScheduler.getCurrentTask();
        assertNull(currentTask, "CurrentTask should maintain null value after calling method to stop " +
                "feeding scheduler");
    }

    @Test
    public void CurrentTaskShouldBecomeNull() {
        feedingScheduler.scheduleRecurringFeeding(mealIndex0, periodicity);
        currentTask = feedingScheduler.getCurrentTask();
        assertNotNull(currentTask, "CurrentTask should not be null because a feeding schedule was " +
                "created (CurrentTask)");

        feedingScheduler.stop();
        currentTask = feedingScheduler.getCurrentTask();
        assertNull(currentTask, "CurrentTask should be null after calling method to stop " +
                "feeding schedule");
    }


    /**
     * .hasActiveSchedule() method
     */
    @Test
    public void ActiveFeedingScheduleShouldReturnFalse() {
        boolean active = feedingScheduler.hasActiveSchedule();
        assertFalse(active, "active should return FALSE because a feeding schedule" +
                "was not created");
    }

    @Test
    public void ActiveFeedingScheduleShouldReturnTrue() {
        feedingScheduler.scheduleRecurringFeeding(mealIndex0, periodicity);
        boolean active = feedingScheduler.hasActiveSchedule();
        assertTrue(active, "active should return TRUE because a feeding schedule" +
                "was created");
    }


    /**
     * .shutdown() method
     */
    @Test
    public void CurrentTaskShouldBeNull() {
        boolean active;
        feedingScheduler.scheduleRecurringFeeding(mealIndex0, periodicity);
        active = feedingScheduler.hasActiveSchedule();
        assertTrue(active, "active should be true because a feeding schedule was " +
                "created");

        feedingScheduler.shutdown();

        currentTask = feedingScheduler.getCurrentTask();
        assertNull(currentTask, "CurrentTask should be null because the executor terminated " +
                "all active and scheduled feedings");

        active = feedingScheduler.hasActiveSchedule();
        assertFalse(active, "active should return false because currentTask was canceled" +
                " (by the executor)");
    }


    /**
     * FeedingJob.run() method
     */
    @Test
    public void ExistingScheduledMealShouldBeDispensed() {
        FeedingScheduler.FeedingJob job = feedingScheduler.new FeedingJob(mealIndex0);
        System.setOut(new PrintStream(outputStream));

        job.run();

        String capturedOutput = outputStream.toString();
        String expectedMessage = "[Scheduler] Dispensed scheduled meal: " + mealPlan.getName();
        assertTrue(capturedOutput.contains(expectedMessage), "Console output should contain the" +
                "expectedMessage when the mealplan exists");
    }

    @Test
    public void NonExistingMealShouldNotBeDispensed() {
        FeedingScheduler.FeedingJob job = feedingScheduler.new FeedingJob(mealIndex2);
        System.setOut(new PrintStream(outputStream));

        job.run();

        String capturedOutput = outputStream.toString();
        String expectedMessage = "[Scheduler] Scheduled meal could not be dispensed...";
        assertTrue(capturedOutput.contains(expectedMessage), "Console output should" +
                "contain expectedMessage when the mealplan does not exist");
    }

    @Test
    public void NegativeIndexShouldProduceArrayIndexOutOfBoundsError() {
        FeedingScheduler.FeedingJob job = feedingScheduler.new FeedingJob(-1);
        System.setOut(new PrintStream(outputStream));

        job.run();

        String capturedOutput = outputStream.toString();
        String expectedMessage = "[Scheduler] Error during scheduled feeding: Index -1 out of bounds for length 4";
        assertTrue(capturedOutput.contains(expectedMessage), "Console output should contain " +
                "expectedMessage when index of mealplan is out of bounds");
    }

    @Test
    public void UnnamedExistingMealShouldBeDispensed() {
        FeedingScheduler.FeedingJob job = feedingScheduler.new FeedingJob(mealIndex1);
        System.setOut(new PrintStream(outputStream));

        job.run();

        String capturedOutput = outputStream.toString();
        String expectedMessage = "[Scheduler] Dispensed scheduled meal: (unknown meal)";
        assertTrue(capturedOutput.contains(expectedMessage), "Console output should contain " +
                "expectedMessage when name of mealplan is an empty string");
    }


}
