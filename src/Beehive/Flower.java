package Beehive;
///////class should be flower
public class Flower {
	private FlowerName flowerName;
	private int nectarCollectionEachVisit;
	private int currentNectarAvailable;
	private int dailyNectarProduction;
	
	public Flower ( FlowerName flowerName,int nectarCollectionEachVisit, int currentNectarAvailable, int dailyNectarProduction) {
		this.flowerName = flowerName;
		this.nectarCollectionEachVisit = nectarCollectionEachVisit;
		this.currentNectarAvailable = currentNectarAvailable;
		this.dailyNectarProduction = dailyNectarProduction;
	}

	public FlowerName getFlowerName() {
		return flowerName;
	}
	
	public void setFlowerName(FlowerName flowerName) {
		this.flowerName = flowerName;
	}

	public int getNectarCollectionEachVisit() {
		return nectarCollectionEachVisit;
	}

	public void setNectarCollectionEachVisit(int nectarCollectionEachVisit) {
		this.nectarCollectionEachVisit = nectarCollectionEachVisit;
	}

	public int getCurrentNectarAvailable() {
		return currentNectarAvailable;
	}

	public void setCurrentNectarAvailable(int currentNectarAvailable) {
		this.currentNectarAvailable = currentNectarAvailable;
	}

	public int getDailyNectarProduction() {
		return dailyNectarProduction;
	}

	public void setDailyNectarProduction(int dailyNectarProduction) {
		this.dailyNectarProduction = dailyNectarProduction;
	}
		
}
