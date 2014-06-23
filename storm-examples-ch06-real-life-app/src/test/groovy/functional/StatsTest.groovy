package functional
import org.junit.Test;

class StatsTest extends AbstractAnalyticsTest {
	
	static void main(args) {
		println "helloword"
		StatsTest t = new StatsTest()
		t.testNoDuplication()
  }
	
	public void testNoDuplication(){
		navigate("1", "0") // Players
		navigate("1", "1") // Players
		navigate("1", "2") // Players
		navigate("1", "3") // Cameras

		Thread.sleep(2000) // Give two seconds for the system to process the data.

		println getProductCategoryStats("0", "Cameras")
		println getProductCategoryStats("1", "Cameras")
		println getProductCategoryStats("2", "Cameras")
		println getProductCategoryStats("0", "Players")
		println getProductCategoryStats("3", "Players")
	}
}
