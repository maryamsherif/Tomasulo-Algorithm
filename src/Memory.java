package src;

public class Memory {

    double [] memoryValues =new double [1024];
    boolean found ;
 
    public Memory(){
		memoryValues[10]=10;
		memoryValues[20]=20;
		

    }
    public double readData(int address) {
		if (0<=address && address<=1024) {
			double temp=memoryValues[address];
			found=true;
			return temp;
		}
		else {
			found=false;
			return -1;
		}
			
	}
	
	public void writeData(int address,double data) {
		if(0<=address && address<=1024) {
			memoryValues[address]=data;
		}
		else {
			System.out.println(" Memory is Full !");
		}
		

	}
}
