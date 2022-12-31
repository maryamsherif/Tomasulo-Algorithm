package src;
import src.Instructions.Register;

public class InstructionQueue {
    String instruction;
    Register d ;
    Register s1;
    Register s2;
    int issue;
    int startTime;
    int endTime;
    int wbTime;

    public InstructionQueue( String instruction , Register d , Register s1 , Register s2 , int issue , int startTime , int endTime , int wbTime ){
            this.instruction=instruction;
            this.d=d;
            this.s1=s1;
            this.s2=s2;
            this.issue=issue;
            this.startTime=startTime;
            this.endTime=endTime;
            this.wbTime=wbTime;
    }
    
}
