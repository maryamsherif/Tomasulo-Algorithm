package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    static int cycle=1;
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
        
                     InstrQueue[i] = new InstructionQueue(result[0],result[1],result[2],result[3],-1,-1,-1,-1);
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
                
                //check if instruction queue is empty or not
                int addResEmpty = isRSEmpty(addSubStation);
                int mulResEmpty = isRSEmpty(MulDivStation);
                int loadBuffEmpty = isLoadBuffEmpty(LoadBuff);
                int storeBuffEmpty = isStoreBuffEmpty(StoreBuff);
               while(!finish()) {
                      //-------------------------------------------WRITE BACK-------------------------------------------
                //Write Back ADD/SUB
                for(int i=0;i<addSubStation.length;i++){
                    if(addSubStation[i].busy==1 && addSubStation[i].time==0 && ifWriteBack==false){
                        int idInInstrQueue=addSubStation[i].idInInstrQueue;
                        //if(InstrQueue[idInInstrQueue].endTime==cycle){
                            System.out.println("HEEERRREEE");
                            regFile.updateRegister(InstrQueue[idInInstrQueue].d, addSubStation[i].result);
                            regFile.setQi(InstrQueue[idInInstrQueue].d, " ");
                            addSubStation[i].busy=0;
                            addSubStation[i].op=null;
                            addSubStation[i].Vj=0.0;
                            addSubStation[i].Vk=0.0;
                            addSubStation[i].Qj.equals("");
                            addSubStation[i].Qk.equals("");
                            int id=addSubStation[i].idInInstrQueue;
                            InstrQueue[id].wbTime=cycle;
                            ifWriteBack=true;
                            System.out.println(bus);

                            bus=addSubStation[i].result;
                            busContent="A"+(i+1);
                            System.out.println(bus);
                            break;
                        
                    }
                }

                 //-------------------------------------------EXECUTE-------------------------------------------
                //Execute ADD/SUB 
                for(int i=0;i<addSubStation.length;i++){
                    if(addSubStation[i].busy==1 &&  addSubStation[i].time>0){
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


                  //-------------------------------------------ISSUE-------------------------------------------
                if(issue<InstrQueue.length) {
                for (int i = 0; i < InstrQueue.length; i++) {  //need a method to check if all instruction finished or not (write back is not empty)
                    if(InstrQueue[i].issue == -1) {
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
                                        
                                        if (regFile.regFileReady((InstrQueue[i].j))){ //check if op1 is available
                                            addSubStation[addResEmpty].Vj = regFile.getRegFileValue(InstrQueue[i].j);
                                            addSubStation[addResEmpty].Qj = " ";
                                        }
                                      else {
                                            addSubStation[addResEmpty].Vj = 0;
                                            addSubStation[addResEmpty].Qj = regFile.getQi(InstrQueue[i].j);
                                        }
                                        if (regFile.regFileReady((InstrQueue[i].k))){ //check if op2 is available
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

                                       //2 InstrQueue[i].startTime = cycle;
                                        regFile.setQi(InstrQueue[i].d, "A"+(addResEmpty+1));
                                            break;

                                        }
                                      
                                    }
                                    addSubStation[addResEmpty].idInInstrQueue=issue;
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
                }

              
                //check bus content 
                //Add/Sub Reservation Station
                for(int i =0 ; i<addSubStation.length;i++){
                    if(addSubStation[i].Qj.equals(busContent) && ifWriteBack==true && addSubStation[i].busy==1){
                        addSubStation[i].Vj=bus;
                        addSubStation[i].Qj=" ";
                    }
                    if(addSubStation[i].Qk.equals(busContent) && ifWriteBack==true && addSubStation[i].busy==1){
                        addSubStation[i].Vk=bus;
                        addSubStation[i].Qk=" ";
                    }
                }
                //Mul/Div Reservation Station
                for(int i =0 ; i<MulDivStation.length;i++){
                    if(MulDivStation[i].Qj.equals(busContent) && ifWriteBack==true && MulDivStation[i].busy==1){
                        MulDivStation[i].Vj=bus;
                        MulDivStation[i].Qj=" ";
                    }
                    if(MulDivStation[i].Qk.equals(busContent) && ifWriteBack==true&& MulDivStation[i].busy==1){
                        MulDivStation[i].Vk=bus;
                        MulDivStation[i].Qk=" ";
                    }
                }
               //Register File
               System.out.println("Register File");
                for(int i=0;i<regFile.registers.length;i++){
                     System.out.println(regFile.registers[i].getName()+" "+regFile.registers[i].getValue()+" "+regFile.registers[i].getQi());
                }
               for (int i= 0; i<regFile.registers.length;i++){
                String QReg=regFile.getQi("F"+i);
                   if(QReg.equals(busContent) && ifWriteBack==true){
                       regFile.registers[i].setValue(bus);
                       regFile.registers[i].setQi(" ");
                       break;
                   }
                
               }
            ifWriteBack=false;
            bus=0;
            busContent=" ";
            cycle++;
            if (cycle==7){
                break;
            }

            printings();
               }
            }

            //finish method
            public static boolean finish(){
                for(int i=0;i<InstrQueue.length;i++){
                    if(InstrQueue[i].wbTime==-1){
                        return false;
                    }
                }
                return true;

            }

            //printings
            public static void printings(){
                //print instruction queue
                System.out.println("Instruction Queue");
                System.out.println("Instruction\tIssue\tStart\tEnd\tWB");
                for(int i=0;i<InstrQueue.length;i++){
                    System.out.println(InstrQueue[i].instruction+"\t\t"+InstrQueue[i].issue+"\t"+InstrQueue[i].startTime+"\t"+InstrQueue[i].endTime+"\t"+InstrQueue[i].wbTime);
                }
                System.out.println(" ");
                //print add/sub reservation station
                System.out.println("Add/Sub Reservation Station");
                System.out.println("Busy\tOp\tVj\tVk\tQj\tQk\tA\ttime");
                for(int i=0;i<addSubStation.length;i++){
                    System.out.println(addSubStation[i].busy+"\t"+addSubStation[i].op+"\t"+addSubStation[i].Vj+"\t"+addSubStation[i].Vk+"\t"+addSubStation[i].Qj+"\t"+addSubStation[i].Qk+"\t"+addSubStation[i].time);
                }
                System.out.println(" ");
                //print mul/div reservation station
                System.out.println("Mul/Div Reservation Station");
                System.out.println("Busy\tOp\tVj\tVk\tQj\tQk\tA\ttime");
                for(int i=0;i<MulDivStation.length;i++){
                    System.out.println(MulDivStation[i].busy+"\t"+MulDivStation[i].op+"\t"+MulDivStation[i].Vj+"\t"+MulDivStation[i].Vk+"\t"+MulDivStation[i].Qj+"\t"+MulDivStation[i].Qk+"\t"+MulDivStation[i].time);
                }
                System.out.println(" ");
                //print register file
                System.out.println("Register File");
                System.out.println("Register\tValue\tQi");
                for(int i=0;i<regFile.registers.length;i++){
                    System.out.println(regFile.registers[i].getName()+"\t\t"+regFile.registers[i].getValue()+"\t"+regFile.registers[i].getQi());
                }
                System.out.println(" ");
                //print cycle
                System.out.println("Cycle: "+cycle);
                System.out.println(" ");
                //print bus
                System.out.println("Bus Content: "+busContent+"Bus Tag: "+bus);
                System.out.println(" ");
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

        //Take input from user to initialize the register file needed registers
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of registers you want to initialize");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("Enter the name of the register");
            String name = sc.next();
            System.out.println("Enter the value of the register");
            int value = sc.nextInt();
            if (name.charAt(0) == 'F') {
                regFile.registers[Integer.parseInt(name.substring(1))].setValue(value);
            } else {
                System.out.println("Invalid register name");
            }
        }

        //Intialize Latencies from user
        System.out.println("Enter the latency of ADD unit");
        addL = sc.nextInt();
        System.out.println("Enter the latency of SUB unit");
        subL = sc.nextInt();
        // System.out.println("Enter the latency of MUL unit");
        // mulL = sc.nextInt();
        // System.out.println("Enter the latency of DIV unit");
        // divL = sc.nextInt();
        // System.out.println("Enter the latency of LD unit");
        // loadL = sc.nextInt();
        // System.out.println("Enter the latency of SD unit");
        // storeL = sc.nextInt();



        //Print the register file
        // System.out.println("Register File");
        // System.out.println("Register\tValue\tQi");
        // for(int i=0;i<regFile.registers.length;i++){
        //     System.out.println(regFile.registers[i].getName()+"\t\t"+regFile.registers[i].getValue()+"\t"+regFile.registers[i].getQi());
        // }
        // System.out.println(" ");

        //Initialize the instruction queue
        InstrQueue= IQueue(commands);
        tomasulo();
            


        

      
        

    
}
}
