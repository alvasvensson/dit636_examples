package petfeeder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import petfeeder.exceptions.FoodStockException;
import petfeeder.exceptions.MealPlanException;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
class PetFeederTest {
    private PetFeeder feeder;
    private MealPlan meal;
    private MealPlan breakfast;
    private MealPlan lunch;

    @BeforeEach
    public void setUp() throws Exception {

        feeder = new PetFeeder();

        //Set up for p1 (Basic Meal)
        meal = new MealPlan();
        meal.setName("Meal");
        meal.setAmtTreats("0");
        meal.setAmtKibble("4");
        meal.setAmtWater("1");
        meal.setAmtWetFood("1");

        //Set up
        breakfast = new MealPlan();
        breakfast.setName("Breakfast");
        breakfast.setAmtTreats("0");
        breakfast.setAmtKibble("4");
        breakfast.setAmtWater("1");
        breakfast.setAmtWetFood("1");

        lunch = new MealPlan();
        lunch.setName("Lunch");

        feeder.addMealPlan(meal);
    }

    @Test
    public void testAddMealPlan_Normal(){
        assertTrue(feeder.addMealPlan(breakfast), "failure: should be true.");
    }

    @Test
    public void testAddMealPlan_Bad(){
        assertTrue(feeder.addMealPlan(lunch), "failure: should be false.");
    }

    @Test
    public void testDeleteMealPlan_Normal(){
        String deleted = feeder.deleteMealPlan(0);
        String expected = "Meal";

        assertEquals(expected, deleted);
    }

    @Test
    public void testDeleteMealPlan_Bad(){
        String deleted = feeder.deleteMealPlan(3);
        String expected = null;

        assertEquals(expected, deleted);
    }


    @Test
    public void testEditMealPlan_Normal(){
        String edited = feeder.editMealPlan(0, lunch);
        String expected = "Meal";

        assertEquals(expected, edited);
    }

    @Test
    public void testEditMealPlan_Bad(){
        String edited = feeder.editMealPlan(3, lunch);
        String expected = null;

        assertEquals(expected, edited);
    }

    @Test
    public void testReplenishFood_Normal(){
        try{
            feeder.replenishFood("2","1","1","1");
        }catch (FoodStockException e) {
            fail("FoodStockException should not be thrown");
        }
    }

    @Test
    public void testReplenishFood_Bad(){ // exception should be thrown
        assertThrows(FoodStockException.class, () -> {
            feeder.replenishFood("cat", "dog", "1", "1");
        });
    }

    @Test
    public void testCheckFoodStock_Normal(){
        // stock at beginning should be 15 for all
        String expected = "Kibble: 15\nWater: 15\nWet Food: 15\nTreats: 15\n";
        String stock = feeder.checkFoodStock();
        assertEquals(expected, stock);

        // if we dispense a meal
        feeder.dispenseMeal(0);
        expected = "Kibble: 11\nWater: 14\nWet Food: 14\nTreats: 15\n";
        stock = feeder.checkFoodStock();
        assertEquals(expected, stock);

    }

    @Test
    public void testDispenseMeal_Normal(){
        assertTrue(feeder.dispenseMeal(0), "failure: should be true");
    }

    @Test
    public void testDispenseMeal_Bad_OutofBoundsIndex(){
        assertFalse(feeder.dispenseMeal(5), "failure: should be false");
    }

    @Test
    public void testDispenseMeal_Bad_MealNull(){
        assertFalse(feeder.dispenseMeal(2), "failure: should be false");
    }


    @Test
    public void testDispenseMeal_Bad_NoEnergy() throws Exception{
        //Setup
        MealPlan highEnergyMeal = new MealPlan();
        highEnergyMeal.setAmtTreats("26");
        feeder.addMealPlan(highEnergyMeal);

        assertFalse(feeder.dispenseMeal(1), "failure: should be false");
    }


    @Test
    public void testDispenseMeal_Bad_LowStock() throws Exception{
        //Setup
        MealPlan highVolumeMeal = new MealPlan();
        highVolumeMeal.setAmtTreats("16");
        feeder.addMealPlan(highVolumeMeal);

        assertFalse(feeder.dispenseMeal(1), "failure: should be false");
    }



    @Test
    public void testGetMealPlans_Normal(){
        feeder.addMealPlan(breakfast);
        MealPlan[] mealPlans = feeder.getMealPlans();
        MealPlan[] expected = {meal, breakfast, null, null};

        assertArrayEquals(expected, mealPlans);
    }

    @Test
    public void testGetEnergyLimits_Normal(){
        int expected = 500;
        int energyLimit = feeder.getEnergyLimit();
        assertEquals(expected, energyLimit);
    }

    @Test
    public void testGetRemainingEnergyBudget_Normal(){
        int expected = 500;
        int energyBudget = feeder.getRemainingEnergyBudget();
        assertEquals(expected, energyBudget);
    }
}