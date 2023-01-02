package src;
import src.Instructions.Register;

public class InstructionQueue {
    String instruction;
    String d ;
    String j;
    String k;
    int issue;
    int startTime;
    int endTime;
    int wbTime;

    public InstructionQueue( String instruction , String result , String result2 , String result3 , int issue , int startTime , int endTime , int wbTime ){
            this.instruction=instruction;
            this.d=result;
            this.j=result2;
            this.k=result3;
            this.issue=issue;
            this.startTime=startTime;
            this.endTime=endTime;
            this.wbTime=wbTime;
    }
    
}
