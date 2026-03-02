package petfeeder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import petfeeder.exceptions.FoodStockException;
import petfeeder.exceptions.MealPlanException;

public class FoodContainerTest {
    private FoodContainer fc;
    private MealPlan mp;

    @BeforeEach
    public void setUp(){
        fc = new FoodContainer();
        mp = new MealPlan();
    }

    @AfterEach
    public void tearDown(){
        fc = null;
    }

    @Test
    public void testSetKibble_Normal(){
        fc.setKibble(5);
        assertEquals(5, fc.getKibble());
    }

    @Test
    public void testAddKibble_Normal(){
        try {
            fc.addKibble("5");
        } catch (FoodStockException e) {
            fail("Foodstockexception should not be thrown.");
        }
        assertEquals(20, fc.getKibble());
    }

    @Test
    public void testSetWater_Normal(){
        fc.setWater(5);
        assertEquals(5, fc.getWater());
    }

    @Test
    public void testAddWater_Normal(){
        try {
            fc.addWater("5");
        } catch (FoodStockException e) {
            fail("Foodstockexception should not be thrown.");
        }
        assertEquals(20, fc.getWater());
    }

    @Test
    public void testSetWetfood_Normal(){
        fc.setWetFood(5);
        assertEquals(5, fc.getWetFood());
    }

    @Test
    public void testAddWetfood_Normal(){
        try {
            fc.addWetFood("5");
        } catch (FoodStockException e) {
            fail("Foodstockexception should not be thrown.");
        }
        assertEquals(20, fc.getWetFood());
    }

    @Test
    public void testSetTreats_Normal(){
        fc.setTreats(5);
        assertEquals(5, fc.getTreats());
    }

    @Test
    public void testAddTreats_Normal(){
        try {
            fc.addTreats("5");
        } catch (FoodStockException e) {
            fail("Foodstockexception should not be thrown.");
        }
        assertEquals(20, fc.getTreats());
    }

    @Test
    public void testSetTreats_Exception(){
        fc.setTreats(-1);
        assertEquals(15, fc.getTreats());
    }

    @Test
    public void testAddTreats_Exception(){
        Throwable exception = assertThrows(
                FoodStockException.class, () -> fc.addTreats("-1"));
        assertEquals("Units of treats must be a positive integer", exception.getMessage());
        exception = assertThrows(
                FoodStockException.class, () -> fc.addTreats("One"));
        assertEquals("Units of treats must be a positive integer", exception.getMessage());
        assertEquals(15, fc.getTreats());
    }

    @Test
    public void testSetKibble_Exception(){
        fc.setKibble(-1);
        assertEquals(15, fc.getKibble());
    }

    @Test
    public void testAddKibble_Exception(){
        Throwable exception = assertThrows(
                FoodStockException.class, () -> fc.addKibble("-1"));
        assertEquals("Units of kibble must be a positive integer", exception.getMessage());
        exception = assertThrows(
                FoodStockException.class, () -> fc.addKibble("One"));
        assertEquals("Units of kibble must be a positive integer", exception.getMessage());
        assertEquals(15, fc.getKibble());
    }

    @Test
    public void testSetWater_Exception(){
        fc.setWater(-1);
        assertEquals(15, fc.getWater());
    }

    @Test
    public void testAddWater_Exception(){
        Throwable exception = assertThrows(
                FoodStockException.class, () -> fc.addWater("-1"));
        assertEquals("Units of water must be a positive integer", exception.getMessage());
        exception = assertThrows(
                FoodStockException.class, () -> fc.addWater("One"));
        assertEquals("Units of water must be a positive integer", exception.getMessage());
        assertEquals(15, fc.getWater());
    }

    @Test
    public void testSetWetfood_Exception(){
        fc.setWetFood(-1);
        assertEquals(15, fc.getWetFood());
    }

    @Test
    public void testAddWetfood_Exception(){
        Throwable exception = assertThrows(
                FoodStockException.class, () -> fc.addWetFood("-1"));
        assertEquals("Units of wet food must be a positive integer", exception.getMessage());
        exception = assertThrows(
                FoodStockException.class, () -> fc.addWetFood("One"));
        assertEquals("Units of wet food must be a positive integer", exception.getMessage());
        assertEquals(15, fc.getWetFood());
    }

    @Test
    public void testEnoughIngredients_Normal() throws Exception{

        mp.setAmtWater("10");
        mp.setAmtKibble("10");
        mp.setAmtWetFood("10");
        mp.setAmtTreats("10");

        assertTrue(fc.enoughIngredients(mp));
    }
    @Test
    public void testEnoughIngredients_Exception() throws Exception{
        mp.setAmtWater("20");
        mp.setAmtKibble("20");
        mp.setAmtWetFood("20");
        mp.setAmtTreats("20");

        assertFalse(fc.enoughIngredients(mp));
    }
    @Test
    public void testUseIngredients_Normal() throws Exception{

        mp.setAmtWater("10");
        mp.setAmtKibble("10");
        mp.setAmtWetFood("10");
        mp.setAmtTreats("10");

        assertTrue(fc.useIngredients(mp));

        assertEquals(5, fc.getWater());
        assertEquals(5, fc.getKibble());
        assertEquals(5, fc.getWetFood());
        assertEquals(5, fc.getTreats());
    }
    @Test
    public void testUseIngredients_Exception() throws Exception{

        mp.setAmtWater("20");
        mp.setAmtKibble("20");
        mp.setAmtWetFood("20");
        mp.setAmtTreats("20");

        assertFalse(fc.useIngredients(mp));

        assertEquals(15, fc.getWater());
        assertEquals(15, fc.getKibble());
        assertEquals(15, fc.getWetFood());
        assertEquals(15, fc.getTreats());
    }
}
