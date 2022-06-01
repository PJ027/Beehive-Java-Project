package Beehive;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Main {	
	public static double  HoneyStock = 0;
	public static int eggsLaid=0;
	public static int  deathCount=0 ;
	public static int birthCount=0;
	public static int initWorkers=0;
	public static int totalNumberOfDays=0;
	public static int currentDay=0;
	public static boolean  firstLine = true;
	public static ArrayList<Bee>beesArray= new ArrayList <Bee>();
	public static ArrayList<Flower>flowersArray= new ArrayList <Flower>();
	public static Stack<Bee>workersLaunchChamber = new Stack<Bee>();
		
	//This method is called repeatedly from the main method to simulate a new day.	
	public static void aDayPasses(){
		incrementAge(); 	
		emptyStomachofAllBees();		
		metamorphose();		
		feedingTime();	
		undertakerCheck();	
		logDailyStatusToFile();	
		resetFlowerArray();	
	}
	
	//This method takes the number of eggs as arg, instantiate new eggs repeatedly and add each one of them to the beesArray.
	//It also ensures that the max size of bee population has not been reached.	
	public static void addEggtoHive( int egg ){	
		eggsLaid=egg;
		do {	
			if (beesArray.size()<1000) {
				beesArray.add(new Bee());
					egg-=1;
			}			
		}while (egg!=0 && beesArray.size()<1000);					
	}
  	
	//This method is executed 5 times daily and for each time, all worker bees are made to visit flowers for pollen collection.
	//The method removes all worker bees from the beesArray and add them to a workersLaunchChamber.	
	public static void allWorkerBeesGardenSorties(){	
		int i = 0;
		while(i<beesArray.size()){
			Bee y = beesArray.get(i);
			if(y.getType()==BeeType.worker) {
				workersLaunchChamber.push(y);
				beesArray.remove(i);
				i = 0;
			}else {
				i++;
			}	
		}
		while(!workersLaunchChamber.isEmpty()) {
			visitFlower();
			beesArray.add(workersLaunchChamber.pop());
		}
	}
		
	//This method is called once daily to reset the NectarCollectionSorties of all bees to 0 and the eaten attribute to false.	
	public static void emptyStomachofAllBees() {		
		for(Bee bee : beesArray) {				
			bee.setNectarCollectionSorties(0);
			bee.setEaten(false);
		}
	}
			
	//This method is called to feed the bees in hierarchical order. 	
	public static void feed(double x, BeeType beeType){		 
		for(Bee bee : beesArray) {			
			if(HoneyStock>=x && bee.getType()== beeType){
				HoneyStock-= x;
	            bee.setEaten(true);
			}
		}
	}  
	
	//This method is called once daily to feed everyone in the beehive except the eggs. 
	public static void feedingTime() {
		if (HoneyStock < 2) {
		   System.out.println("Since the Queen did not receive honey, the simulation is terminated.");
		     }
		else {
			//feeding queen
		    HoneyStock-=2;
		    //feed larva
		    feed(0.5,BeeType.larva);
			//feeding worker		         
		    feed(1,BeeType.worker);
		    //feeding drone
			feed(1,BeeType.drone);
		}
	}

	//This method removes a particular bee from beesArray. 
	//To keep track of the total number of death that has occurred.	
	public static void funeral(Iterator<Bee> bee) {
		bee.remove();
		deathCount++;
	}
	
	//Everyday this method increment the age of all eggs, larva, pupa, worker and drone bees in the hive.
	public static void incrementAge() {	
		Iterator<Bee> beeIterator = beesArray.iterator();
		while (beeIterator.hasNext()) {
			Bee bee = beeIterator.next(); 
			bee.setAge(bee.getAge()+1);
		}	
	}
		
	//This method gets called at the beginning of the simulation one time only. 
	//If the simulation configuration file provided some workers, these are instantiated and added to the beesArray.
	public static void initBeesArray() {	
		for (int i=0;i<initWorkers;i++) {
			beesArray.add(new Bee(20, BeeType.worker));		
		}
	}
	
	//This method gets called daily to determine the random number of eggs to be laid by the Queen on a day. 
	//The range is between 10 and 50 eggs.
	public static int layDailyEggs() {
		int numberOfEggs = randomSelect(10,50); 	
		return numberOfEggs;
	}
	
	//This method is called at the end of each day to append the day’s data to a logfile.
	public static void logDailyStatusToFile() {
		PrintWriter output= null;
		try {
			//creation of file in excel			
			output= new PrintWriter(new FileOutputStream("simLog.csv",true));	
			//Print headings
			if (firstLine){
				firstLine = false;				
			output.print("Days"); 
			output.print(";"); 
			output.print("eggsLaid");
			output.print(";");
			output.print("eggInHive");
			output.print(";");
			output.print("Larva");
			output.print(";");
			output.print("Pupa");
			output.print(";");
			output.print("Worker");
			output.print(";");
			output.print("Drone");
			output.print(";");
			output.print("Death");
			output.print(";");
			output.print("Birth");
			output.print(";");
			output.print("HoneyStock");
			output.print(";");
			output.print("Flower1Nectar");
			output.print(";");
			output.print("Flower2Nectar");
			output.print(";");
			output.print("Flower3Nectar");
			output.print("\n");
			}
			//using println to change row
			//output for each headings
			output.print(currentDay);
			output.print(";");
			output.print(eggsLaid);
			output.print(";");
			output.print(countEggs());
			output.print(";");
			output.print(countLarva());
			output.print(";");
			output.print(countPupa());
			output.print(";");
			output.print(countWorker());
			output.print(";");
			output.print(countDrone());
			output.print(";");
			output.print(deathCount);
			output.print(";");
			output.print(birthCount);
			output.print(";");
			output.print(HoneyStock);
			output.print(";");
			//The first flower added was Rose.Calling Rose first.
			output.print(flowersArray.get(0).getCurrentNectarAvailable());
			output.print(";");
			//The second flower added was Hibiscus.Calling Hibiscus second.
			output.print(flowersArray.get(1).getCurrentNectarAvailable());
			output.print(";");
			//The third flower added was Frangipani.Calling Frangipani third.
			output.print(flowersArray.get(2).getCurrentNectarAvailable());
			output.print("\n");			
		}
		catch(FileNotFoundException  e)
		{
			System.out.println("File not created");
		}
		output.close();
	}
	
	public static void main(String[] args) throws IOException {
	
		//adding the 3 types of flowers to the flower arraylist
		flowersArray.add(new Flower(FlowerName.Rose,20,20000,20000));	
		flowersArray.add(new Flower(FlowerName.Hibiscus,10,10000,10000));	
		flowersArray.add(new Flower(FlowerName.Frangipani,50,50000,50000));	
		readSimulationConfig();
		initBeesArray();
		for(currentDay = 1; currentDay<=totalNumberOfDays;currentDay++){
			addEggtoHive(layDailyEggs());
			for(int x=0;x<5;x++){
				allWorkerBeesGardenSorties();
			}
			printBeehiveStatus();
			aDayPasses();
			Collections.sort(beesArray);
		}  
	}
	
	//This method checks each entry in the beesArray and update their types if required based on their age.
	public static void  metamorphose() {	
		Random random = new Random();		
		Iterator<Bee> beeIterator = beesArray.iterator();
		while (beeIterator.hasNext()) {
			Bee bee = beeIterator.next();
			// checking if age = 4 (it is an no longer an egg)
			if ( (bee.getAge()==4) && (bee.getType() == BeeType.egg ) ) {
				bee.setType(BeeType.larva);
				//assigning larva to array
				birthCount++;
			// checking if age = 10 (it is a larva)
			} if ( (bee.getAge()== 10) && (bee.getType() == BeeType.larva) ) {  
				//assigning pupa to array.
				bee.setType(BeeType.pupa);		
			} 
			// checking if age = 20 ( it is transformed into either a worker bee or a drone)
			if(bee.getAge() == 20){
				double probability = random.nextDouble();
				if (probability <= 0.1) { 
					bee.setType(BeeType.drone);			
				}
				else 
				{
					bee.setType(BeeType.worker);
				}
			}
			//checking if age is more than 35 days and if bee is of type worker.			
			if ( (bee.getAge()==35) && (bee.getType() == BeeType.worker) ) {
				funeral(beeIterator);
			}
			//checking  if age is more than 56 day and if bee is of type drone. 		
			if ( (bee.getAge()==56) && (bee.getType() == BeeType.drone) ) {
				funeral(beeIterator);
			}
		}
		feedingTime();
	}	
	
	//This method counts the total number for the different categories of entities, total death/birth of bees in the beehive.
	//The method counts current honey in storage. 
	//It also collects necessary data to be used for writing to the CSV log file.
	public static void printBeehiveStatus() {		
		System.out.println("*** This is day " +(currentDay) + " ***");
		System.out.println("Queen laid " +eggsLaid+ " eggs!");
		System.out.println("Beehive Status");
		System.out.println("Egg Count : " + countEggs());
		System.out.println("Larva Count : " + countLarva());
		System.out.println("Pupa Count : " + countPupa());
		System.out.println("Worker Count : " + countWorker());
		System.out.println("Drone Count : " + countDrone());
		System.out.println("Death Count : " + deathCount);
		System.out.println("Birth Count : "+ birthCount);
		System.out.println("Honey Stock : "+ HoneyStock);
		printFlowerGarden();
		}
	
	//This method does the screen printing for the remaining nectar stock for each of the 3 types of flowers.
	public static void printFlowerGarden() {
		System.out.println ("Flower Roses nectar stock : " +flowersArray.get(0).getCurrentNectarAvailable());
		System.out.println ("Flower Hibiscus nectar stock : " +flowersArray.get(1).getCurrentNectarAvailable());
		System.out.println ("Flower Frangipani nectar stock : " +flowersArray.get(2).getCurrentNectarAvailable());
		System.out.println("");	
	}
		
	//This methods gets called at the beginning of the simulation to read in some configuration parameters
	public static void readSimulationConfig() {
		Scanner input = new Scanner(System.in);		
		try {
			input=new Scanner (new FileInputStream("simconfig.txt"));
			while(input.hasNextLine())
			{
				String line=input.nextLine();
				String[] arrline=line.split(": ");								
				if (arrline[0].equals("simulationDays")) {
					//to convert string to integer using Integer.parseInt
					totalNumberOfDays= Integer.parseInt(arrline[1]);	
				}		
				if (arrline[0].equals("initWorkers")) {
					initWorkers= Integer.parseInt(arrline[1]);
				}					
				if (arrline[0].equals("initHoneyStock")) {
					HoneyStock= Integer.parseInt(arrline[1]);
				}
			}
		}
		catch(FileNotFoundException  e)
		{
			System.out.println("File not created");
		}
		input.close();	
	}		
	
	//This method is called at the end of each day to check whether larva, worker or drone have eaten on that day. 
	//If not, they die.	
	public static void undertakerCheck() {		
		Iterator<Bee> beeIterator = beesArray.iterator();
		while (beeIterator.hasNext()) {
		   Bee bee = beeIterator.next(); 
			if ( (bee.getType() == BeeType.larva) && (bee.isEaten()==false)) {
				funeral(beeIterator);
			}
			if ((bee.getType() == BeeType.worker) && (bee.isEaten()==false)){
				funeral(beeIterator);	
			}
			if ((bee.getType() == BeeType.drone ) && (bee.isEaten()==false)) {
				funeral(beeIterator);
			}
		}
	}
	
	//This method replenish the maximum pollen capacity for each flower on a daily basis. 
	public static void resetFlowerArray() { 	
		 //Using the for loop to set the DailyNectarProduction() for each flower as mentioned in the main method.
		for(Flower flower : flowersArray) {
			flower.setCurrentNectarAvailable(flower.getDailyNectarProduction());
		}
	}
	
	//This method enables a bee to select a flower type to visit not more than 
	public static void visitFlower() {				       
	       // successfully collected pollen
	       boolean success;
	       //number of visits
	       int visits=0;	      
	       do {
	           visits++;
	           int flower = randomSelect(0,2);
	           success = false;
	           // Assuming that any flowers have been selected, thus using .get(flower)
	           if (flowersArray.get(flower).getCurrentNectarAvailable() >= flowersArray.get(flower).getNectarCollectionEachVisit()) {
	    		   // Increase honey stock by nectar collected/40
	        	   HoneyStock +=flowersArray.get(flower).getNectarCollectionEachVisit()/40.0;	    
	        	   
	        	   // Reduce nectar available in flower by collected amount by using a variable and setting the new CurrentNectarAvailable()       	   
	        	  int x =flowersArray.get(flower).getCurrentNectarAvailable() - flowersArray.get(flower).getNectarCollectionEachVisit();
	        	  flowersArray.get(flower).setCurrentNectarAvailable(x);	   		  	
	        	  success = true;
	           }
	      } while(success==false && visits !=5);  	       
	}
	
	//counting total number of eggs
	public static int countEggs() {
		int eggs= 0 ;
		for(Bee bee : beesArray) {
			if (bee.getType() == BeeType.egg) {
				eggs+=1;
			}
		}
		return eggs;
	}
	
	//counting total number of larva		
	public static int countLarva() {
		int larva= 0 ;	
		for(Bee bee : beesArray) {
			if (bee.getType() == BeeType.larva) {
				larva+=1;
			}	
		}	
		return larva;
	}
	
	//counting total number of pupa
	public static int countPupa() {
		int pupa= 0 ;	
		for(Bee bee : beesArray) {
			if (bee.getType() == BeeType.pupa) {
				pupa+=1;
			}
		}
		return pupa;
	}
	
	//counting total number of worker bee
	public static int countWorker() {
		int worker= 0 ;
		for(Bee bee : beesArray) {
			if (bee.getType() == BeeType.worker) {
				worker+=1;
			}
		}
		return worker;
	}
	
	//counting total number of drone
	public static int countDrone() {
		int drone= 0 ;
		for(Bee bee : beesArray) {
			if (bee.getType() == BeeType.drone) {
				drone+=1;
			}
		}
		return drone;
	}
	
	//This method is used for randomly selecting anything. 
	public static int randomSelect(int i, int j) {
		Random r = new Random();
		return r.nextInt((j - i) + 1) + i;
		}
	}

