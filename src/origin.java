package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import src.Instructions.RegisterFile;


public class origin {

   static ReservationStation [] addSubStation ;
   static ReservationStation [] MulDivStation ;
   static LoadBuffer [] LoadBuff;
   static StoreBuffer [] StoreBuff;
   static InstructionQueue [] InstrQueue;
    static RegisterFile regFile = new RegisterFile();
    static int addL;
    static int subL;
    static int mulL;
    static int divL;
    static int loadL;
    static int storeL;
    static int cycle=0;
    static int issue=0;
    static double bus;
    static String busContent;
    static boolean ifWriteBack = false;




   //Read the input file
   public static ArrayList<String[]> readF(String fileName) throws IOException {
         //Read Instructions from file
         File file = new File(fileName);
         String currentLine = "";
         FileReader fileReader;

         ArrayList<String> commands = new ArrayList<String>();
         ArrayList<String[]> finalCommands = new ArrayList<String[]>();


         try {
             fileReader = new FileReader(file);
             BufferedReader br = new BufferedReader(fileReader);
             while ((currentLine = br.readLine()) != null) {
                 commands.add(currentLine);
                 // Parsing the currentLine String
                 }
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
         
         for(int i = 0; i<commands.size();i++) {
             String [] result= ((String)commands.get(i)).split(" ");
             finalCommands.add(result);
         }

            return finalCommands;
         } 
         
         //Instruction Queue
         public static InstructionQueue[] IQueue(ArrayList<String[]> commands) {

             InstrQueue = new InstructionQueue[commands.size()];

             for(int i = 0; i<commands.size();i++) {
                 String [] result= ((String[])commands.get(i));
        
                     InstrQueue[i] = new InstructionQueue(result[0],result[1],result[2],result[3],0,0,0,0);
             }
             return InstrQueue;
         }
    

         //check if reservation station is full or not
            public static int isRSEmpty(ReservationStation [] RS) {
                for(int i = 0; i<RS.length;i++) {
                    if(RS[i].busy == 0) {
                        //empty
                        return i;
                    }
                }
                return -1;
            }

            //check if load buffer is full or not
            public static int isLoadBuffEmpty(LoadBuffer [] LB) {
                for(int i = 0; i<LB.length;i++) {
                    if(LB[i].busy == 0) {
                        //empty
                        return i;
                    }
                }
                return -1;
            }

            //check if store buffer is full or not
            public static int isStoreBuffEmpty(StoreBuffer [] SB) {
                for(int i = 0; i<SB.length;i++) {
                    if(SB[i].busy == 0) {
                        //empty
                        return i;
                    }
                }
                return -1;
            }

            //main
            public static void tomasulo() {
                //tomasulo algorithm
                
                //-------------------------------------------ISSUE-------------------------------------------
                //check if instruction queue is empty or not
                int addResEmpty = isRSEmpty(addSubStation);
                int mulResEmpty = isRSEmpty(MulDivStation);
                int loadBuffEmpty = isLoadBuffEmpty(LoadBuff);
                int storeBuffEmpty = isStoreBuffEmpty(StoreBuff);
               
                for (int i = 0; i < InstrQueue.length; i++) {  //need a method to check if all instruction finished or not (write back is not empty)
                    if(InstrQueue[i].issue == 0) {
                        //check if reservation station is empty or not
                        if(InstrQueue[i].instruction.equals("ADD.D") || InstrQueue[i].instruction.equals("SUB.D")) {
                            if(isRSEmpty(addSubStation)!=-1) {
                                //issue instruction
                                InstrQueue[i].issue = cycle;
                                //find empty reservation station
                                    if(addSubStation[addResEmpty].busy == 0) {
                                        //empty
                                        addSubStation[addResEmpty].busy = 1;
                                        addSubStation[addResEmpty].op = InstrQueue[i].instruction;
                                        
                                        if (regFile.regFileReady((InstrQueue[i].j))){
                                            addSubStation[addResEmpty].Vj = regFile.getRegFileValue(InstrQueue[i].j);
                                            addSubStation[addResEmpty].Qj = " ";
                                        }
                                        else {
                                            addSubStation[addResEmpty].Vj = 0;
                                            addSubStation[addResEmpty].Qj = regFile.getQi(InstrQueue[i].j);
                                        }
                                        if (regFile.regFileReady((InstrQueue[i].k))){
                                            addSubStation[addResEmpty].Vk = regFile.getRegFileValue(InstrQueue[i].k);
                                            addSubStation[addResEmpty].Qk = " ";
                                        }
                                        else {
                                            addSubStation[addResEmpty].Vk = 0;
                                            addSubStation[addResEmpty].Qk = regFile.getQi(InstrQueue[i].k);
                                        }
                                        if (InstrQueue[i].instruction.equals("ADD.D")) {
                                            addSubStation[addResEmpty].time= addL;
                                        }
                                        else if (InstrQueue[i].instruction.equals("SUB.D")) {
                                            addSubStation[addResEmpty].time= subL;
                                        }

                                        InstrQueue[i].startTime = cycle;
                                        regFile.setQi(InstrQueue[i].d, "A"+(addResEmpty+1));
                                            break;

                                        }
                                      
                                    }
                                    addSubStation[i].idInInstrQueue=issue;
                                    issue++;
                                }
                            }
                        
                        else if(InstrQueue[i].instruction.equals("MUL") || InstrQueue[i].instruction.equals("DIV")) {
                            if(isRSEmpty(MulDivStation)!=-1) {
                                //issue instruction
                                InstrQueue[i].issue = cycle;
                                //find empty reservation station
                                for(int j = 0; j<MulDivStation.length;j++) {
                                    if(MulDivStation[j].busy == 0) {
                                        //empty
                                        MulDivStation[j].busy = 1;
                                        MulDivStation[j].op = InstrQueue[i].instruction;
                                        // MulDivStation[j].Vj = InstrQueue[i].j;
                                        // MulDivStation[j].Vk = InstrQueue[i].k;
                                        MulDivStation[j].Qj = " ";
                                        MulDivStation[j].Qk = " ";
                                        // MulDivStation[j].A = " ";
                                        InstrQueue[i].startTime = cycle;
                                        break;
                                    }
                                }
                            }
                        }
                        else if(InstrQueue[i].instruction.equals("LD")) {
                            if(isLoadBuffEmpty(LoadBuff)!=-1) {
                                //issue instruction
                                InstrQueue[i].issue = cycle;
                                //find empty reservation station
                                for(int j = 0; j<LoadBuff.length;j++) {
                                    if(LoadBuff[j].busy == 0) {
                                        //empty
                                        LoadBuff[j].busy = 1;
                                        // LoadBuff[j].op = InstrQueue[i].instruction;
                                        // LoadBuff[j].A = InstrQueue[i].j;
                                        // LoadBuff[j].Qj = " ";
                                        // LoadBuff[j].Qk = " ";
                                        // LoadBuff[j].time = loadL;
                                        InstrQueue[i].startTime = cycle;
                                        break;


                                    } 
                                }
                            }
                        }
                    }

                //-------------------------------------------EXECUTE-------------------------------------------
                //Execute ADD/SUB 
                for(int i=0;i<addSubStation.length;i++){
                    if(addSubStation[i].busy==1 && addSubStation[i].time>0){
                        if(addSubStation[i].Qj.equals(" ") && addSubStation[i].Qk.equals(" ")){
                            if (addSubStation[i].op.equals("ADD.D") && addSubStation[i].time==addL || addSubStation[i].op.equals("SUB.D") && addSubStation[i].time==subL){
                                int id=addSubStation[i].idInInstrQueue;
                                InstrQueue[id].startTime = cycle;
                            }
                            addSubStation[i].time--;
                            if(addSubStation[i].time==0){
                                if (addSubStation[i].op.equals("ADD.D")){
                                    addSubStation[i].result = addSubStation[i].Vj + addSubStation[i].Vk;
                                }
                                else if (addSubStation[i].op.equals("SUB.D")){
                                    addSubStation[i].result = addSubStation[i].Vj - addSubStation[i].Vk;
                                }
                                int idInInstrQueue=addSubStation[i].idInInstrQueue;
                                InstrQueue[idInInstrQueue].endTime=cycle;
                            }
                        }
                    }
                }

                //-------------------------------------------WRITE BACK-------------------------------------------
                //Write Back ADD/SUB
                for(int i=0;i<addSubStation.length;i++){
                    if(addSubStation[i].busy==1 && addSubStation[i].time==0 && ifWriteBack==false){
                        int idInInstrQueue=addSubStation[i].idInInstrQueue;
                        if(InstrQueue[idInInstrQueue].endTime==cycle){
                            regFile.updateRegister(InstrQueue[idInInstrQueue].d, addSubStation[i].result);
                            regFile.setQi(InstrQueue[idInInstrQueue].d, " ");
                            addSubStation[i].busy=0;
                            int id=addSubStation[i].idInInstrQueue;
                            InstrQueue[id].wbTime=cycle;
                            ifWriteBack=true;
                            bus=addSubStation[i].result;
                            busContent="A"+(i+1);
                            break;
                        }
                    }
                }






            }



    public static void main (String [] args ){

        //Add/Sub Reservation Station
        addSubStation=new ReservationStation[3];
		for(int i=0;i<3;i++) {
			addSubStation[i]=new ReservationStation();
        }

        //Mul/Div Reservation Station
         MulDivStation=new ReservationStation[3];
         for(int i=0;i<3;i++){
        MulDivStation[i]=new ReservationStation();
    
         }
        
        //Load Buffer 
        LoadBuff=new LoadBuffer[3];
		for(int i=0;i<3;i++) {
			LoadBuff[i]=new LoadBuffer();
        }

        //Store Buffer
        StoreBuff=new StoreBuffer[3];
		for(int i=0;i<3;i++) {
			StoreBuff[i]=new StoreBuffer();
        }

       
        //Call readF function to read the input file
        ArrayList<String[]> commands = new ArrayList<String[]>();
        try {
            commands = readF("test.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Print the input file
        // for(int i = 0; i<commands.size();i++) {
        //     String [] result= ((String[])commands.get(i));
        //     for(int j = 0; j<result.length;j++) {
        //         System.out.print(result[j]+" ");
        //     }
        //     System.out.println();
        // }


      
        

    
}
}
