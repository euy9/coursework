package Game;
import java.awt.*;
import java.util.Objects;
import javax.swing.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestOaklandOligarchy {


	// Test that the main frame of the layout is able to be displayeed
	@Test
	public void testFrame() {
		JFrame testFrame = OaklandOligarchy.frame;
		testFrame.isDisplayable();
	}

	// Test String can be passed as a player name
	@Test
	public void testStringName() {
		Player testing = new Player("Bob");
		String name = "Bob";
		String playa = testing.setName(name);
		assertEquals("Bob", playa);
	}

	// Test EMPTY String can be passed as a player name
	@Test
	public void testEmptyStringName() {
		Player testing = new Player("Bob");
		String name = "";
		String playa = testing.setName(name);
		assertEquals("", playa);
	}

	// Test instantiated name is passed correctly
	@Test
	public void testSetName() {
			Player testing = new Player("Bob");
			String name = testing.getName();
			assertEquals("Bob", name);
	}

	// Test starting money is passed when player is instantiated
	@Test
	public void getStartingMoney() {
		Player testing = new Player("Bob");
		int cash = testing.getMoney();
		assertEquals(2000, cash);
	}

	// Test that the uninstantiated default player money amount is zero
	@Test
	public void testGetMoneyZero() {
		Player test = new Player("Bob");
		int money = test.getMoney();
		assertEquals(2000, money);
	}

	// Test if the uninstantiated money can be adjusted
	@Test
	public void testReceiveMoney500() {
		Player test = new Player("Bob");
		int money = 500;
		assertEquals(2500, test.receiveMoney(money));
	}

	// Test instantiated money can be deducted
	@Test
	public void testPayMoneyBeginGame() {
		Player testing = new Player("Bob");
		int toPay = 500;
		assertEquals(1500, testing.payMoney(toPay));
	}

	// Test paying could result in negative value
	@Test
	public void testPayMoneyNegative() {
		Player testing = new Player("Bob");
		int toPay = 5000;
		assertEquals(-3000, testing.payMoney(toPay));
	}

	// Test zero money received keeps current value
	@Test
	public void testReceiveZero(){
		Player testing = new Player("Bob");
		int toReceive = 0;
		assertEquals(2000, testing.receiveMoney(toReceive));
	}

	// Test 10000 money received adjusts appropriately
	@Test
	public void testReceiveTenThousand(){
		Player testing = new Player("Bob");
		int toReceive = 10000;
		assertEquals(12000, testing.receiveMoney(toReceive));
	}

	// Test initial properties is zero.
	@Test
	public void testNumPropertiesZero(){
		Player testing = new Player("Bob");
		assertEquals(0, testing.getPropNum());
	}

	// Test property is NOT on player's list
	@Test
	public void testPropertyExists(){
		Player testing = new Player("Bob");
		Property name = new Property("Starbucks");
		assertEquals(false, testing.propExist(name));
	}


}