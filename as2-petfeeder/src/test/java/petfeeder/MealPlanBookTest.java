package petfeeder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanBookTest {

    private MealPlanBook mealPlanBook;
    private MealPlan meal;
    private MealPlan breakfast;
    private MealPlan mp3;
    private MealPlan mp4;
    private MealPlan mp5;


    @BeforeEach
    void setUp() {
        mealPlanBook = new MealPlanBook();
        breakfast = new MealPlan();
        breakfast.setName("Breakfast");

        meal = new MealPlan();
        meal.setName("Meal");

        mp3 = new MealPlan();
        mp4 = new MealPlan();
        mp5 = new MealPlan();

    }

    @Test
    void testGetMealPlans_Normal() {
        mealPlanBook.addMealPlan(meal);
        MealPlan[] actual = mealPlanBook.getMealPlans();
        MealPlan[] expected = {meal, null, null, null};

        assertArrayEquals(expected, actual);
    }

    @Test
    void testAddMealPlan_Normal() {
        assertTrue(mealPlanBook.addMealPlan(meal), "failure: should be true");
    }

    @Test
    void testAddMealPlan_Bad_ArrayFull() {
        mealPlanBook.addMealPlan(meal);
        mealPlanBook.addMealPlan(breakfast);
        mealPlanBook.addMealPlan(mp3);
        mealPlanBook.addMealPlan(mp4);
        assertFalse(mealPlanBook.addMealPlan(mp5), "failure: should be false");
    }

    // tries to add same meal twice
    @Test
    void testAddMealPlan_Bad_AlreadyAdded() {
        mealPlanBook.addMealPlan(meal);
        assertFalse(mealPlanBook.addMealPlan(meal), "failure: should be false");
    }

    @Test
    void testDeleteMealPlan_Normal() {
        mealPlanBook.addMealPlan(meal);
        String deleted = mealPlanBook.deleteMealPlan(0);
        String expected = "Meal";

        assertEquals(expected, deleted);

    }

    @Test
    void testDeleteMealPlan_Bad() {
        String deleted = mealPlanBook.deleteMealPlan(0);
        String expected = null;

        assertEquals(expected, deleted);

    }

    @Test
    void testEditMealPlan_Normal() {
        mealPlanBook.addMealPlan(breakfast);
        String actual = mealPlanBook.editMealPlan(0, meal);
        String expected = "Breakfast";

        assertEquals(expected, actual);

        // check if meal at that position is changed
        MealPlan [] actualMeals = mealPlanBook.getMealPlans();
        MealPlan expectedMeal = meal;

        assertEquals(expectedMeal, actualMeals[0]);
    }

    @Test
    void testEditMealPlan_Bad() {
        mealPlanBook.addMealPlan(breakfast);
        String actual = mealPlanBook.editMealPlan(3, meal);
        String expected = null;

        assertEquals(expected, actual);
    }

    @Test
    void testMealPlanShouldBeDeleted() {
        boolean exists = mealPlanBook.addMealPlan(meal);
        assertTrue(exists);

        MealPlan[] plan = mealPlanBook.getMealPlans();
        assertSame(plan[0].getName(), meal.getName(), "Name of meal at the 0-th Mealplan index and the created" +
                "mealplan should be the same.");

        mealPlanBook.deleteMealPlan(0);

        assertNull(plan[0], "Mealplan that was created should be null as a result of the deletion.");

    }
}