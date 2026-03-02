package petfeeder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import petfeeder.exceptions.MealPlanException;

public class MealPlanTest {
private MealPlan mp;

    @BeforeEach
    public void setUp(){
        mp = new MealPlan();
    }

    @AfterEach
    public void tearDown(){
        mp = null;
    }

    @Test
    public void testSetMealPlanName_Normal(){
        mp.setName("Lunch");
        assertEquals("Lunch", mp.getName());
    }

    @Test
    public void testSetMealPlanKibble_Normal(){

        try{
            mp.setAmtKibble("0");
        } catch(MealPlanException e){
            fail("MealPlanException should not be thrown");
        }
        assertEquals(0, mp.getAmtKibble());
    }
    @Test
    public void testSetMealPlanTreats_Normal(){
        try{
            mp.setAmtTreats("0");
        } catch(MealPlanException e){
            fail("MealPlanException should not be thrown");
        }
        assertEquals(0, mp.getAmtTreats());
    }
    @Test
    public void testSetMealPlanWetfood_Normal(){
        try{
            mp.setAmtWetFood("1");
        } catch(MealPlanException e){
            fail("MealPlanException should not be thrown");
        }
        assertEquals(1, mp.getAmtWetFood());
    }
    @Test
    public void testSetMealPlanWater_Normal(){
        try{
            mp.setAmtWater("1");
        } catch(MealPlanException e){
            fail("MealPlanException should not be thrown");
        }
        assertEquals(1, mp.getAmtWater());
    }
    @Test
    public void testCalculateEnergyCost() throws Exception{

        mp.setAmtWater("1"); //1*5 = 5
        mp.setAmtTreats("1"); //1*20 = 20
        mp.setAmtKibble("1"); //1*10 = 10
        mp.setAmtWetFood("1"); //1*15 = 15

        //Energy cost should be 5 + 20 + 10 + 15 = 50
        assertEquals(50, mp.getEnergyCost());
    }

    @Test
    public void testSetMealPlanWater_Exception(){
        Throwable exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtWater("-1"));
        assertEquals("Units of water must be a positive integer", exception.getMessage());
        exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtWater("One"));
        assertEquals("Units of water must be a positive integer", exception.getMessage());
        assertEquals(0, mp.getAmtWater());
    }
    @Test
    public void testSetMealPlanWetfood_Exception(){
        Throwable exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtWetFood("-1"));
        assertEquals("Units of wet food must be a positive integer", exception.getMessage());
        exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtWetFood("One"));
        assertEquals("Units of wet food must be a positive integer", exception.getMessage());
        assertEquals(0, mp.getAmtWetFood());
    }
    @Test
    public void testSetMealPlanKibble_Exception(){
        Throwable exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtKibble("-1"));
        assertEquals("Units of kibble must be a positive integer", exception.getMessage());
        exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtKibble("One"));
        assertEquals("Units of kibble must be a positive integer", exception.getMessage());
        assertEquals(0, mp.getAmtKibble());
    }
    @Test
    public void testSetMealPlanTreats_Exception(){
        Throwable exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtTreats("-1"));
        assertEquals("Units of treats must be a positive integer", exception.getMessage());
        exception = assertThrows(
                MealPlanException.class, () -> mp.setAmtTreats("One"));
        assertEquals("Units of treats must be a positive integer", exception.getMessage());

        assertEquals(0, mp.getAmtTreats());
    }
}
