package Beehive;

public class Bee implements Comparable<Bee> {
	private static int beeCount=0;
	private int beeId;
	private int age;
	private BeeType type;
	private int nectarCollectionSorties;
	private boolean eaten;
	private boolean alive;
	
	//overload constructor to initialise values of attributes
	public Bee (int age, BeeType type) {
		beeCount++;
		this.beeId=beeCount;
		this.age = age;
		this.type = type;
		this.nectarCollectionSorties = 0;
		this.eaten = false;
		this.alive = true;
	}
	
	public Bee () {
		beeCount++;
		this.beeId=beeCount;
		this.age = 0;
		this.type = BeeType.egg;
		this.nectarCollectionSorties = 0;
		this.eaten = false;
		this.alive = true;
	}

	public int getBeeId() {
		return beeId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public BeeType getType() {
		return type;
	}
	
	public void setType(BeeType type) {
		this.type = type;
	}

	public int getNectarCollectionSorties() {
		return nectarCollectionSorties;
	}

	public void setNectarCollectionSorties(int nectarCollectionSorties) {
		this.nectarCollectionSorties = nectarCollectionSorties;
	}

	public boolean isEaten() {
		return eaten;
	}

	public void setEaten(boolean eaten) {
		this.eaten = eaten;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int compareTo(Bee x) {
        int y=((Bee)x).getAge();
        // For ascending order
        return this.age-y;
	}
	
	public String toString(){
		String str;
		str= ("Count :"+ beeCount +"\t" +"BeeID: "+ beeId +
			  "\t" +"age: "+age+ "\t" +"type : "+ type + "\t" +"NectarCollection: "
			   + nectarCollectionSorties + "\t" + "eaten: " + eaten+ "\t" + "alive "+ alive);
		return str;
		}	
	}
