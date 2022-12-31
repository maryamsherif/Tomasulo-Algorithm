package src;

public class origin {

   static ReservationStation [] addSubStation ;
   static ReservationStation [] MulDivStation ;
   static LoadBuffer [] LoadBuff;
   static StoreBuffer [] StoreBuff;
   static InstructionQueue [] InstrQueue;

    



    public static void main (String [] args ){
        addSubStation=new ReservationStation[3];

		for(int i=0;i<3;i++) {
			addSubStation[i]=new ReservationStation();
        }


         MulDivStation=new ReservationStation[3];
         for(int i=0;i<3;i++){
        MulDivStation[i]=new ReservationStation();
    
         }
        LoadBuff=new LoadBuffer[3];

		for(int i=0;i<3;i++) {
			LoadBuff[i]=new LoadBuffer();
        }

        StoreBuff=new StoreBuffer[3];

		for(int i=0;i<3;i++) {
			StoreBuff[i]=new StoreBuffer();
        }


    
}
}
