package petfeeder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanBookTest {

    private MealPlanBook mealPlanBook;
    private MealPlan [] mealPlans;
    private MealPlan meal;
    private MealPlan breakfast;


    @BeforeEach
    void setUp() throws Exception {
        mealPlanBook = new MealPlanBook();
        breakfast = new MealPlan();
        breakfast.setName("Breakfast");

        meal = new MealPlan();
        meal.setName("Meal");

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

    // tries to add same meal twice
    @Test
    void testAddMealPlan_Bad() {
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
}