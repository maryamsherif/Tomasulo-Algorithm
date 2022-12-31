package src.Instructions;
import src.Instructions.Register;

public class MulDivInstruction {
    boolean op ; // true -> * / false -> /
    Register  s1; 
    Register  s2;
    Register  d;



    public MulDivInstruction (boolean op , Register s1 , Register s2 , Register d){
       this.op=op;
       this.s1=s1;
       this.s2=s2;
       this.d=d;

    }

    public double MulDiv ( Register s1 , Register s2, Register d ){
       double result=0;
       if (op){
           result=s1.getValue()* s2.getValue();
           d.setValue(result);
           return result;
       }
       else 
       {
           result=s1.getValue()/ s2.getValue();
           d.setValue(result);
           return result;
       }
     
    }
    
}
