package src.Instructions;
import src.Instructions.Register;
import src.Memory;

public class LoadStoreInstruction {
    boolean op ; // true -> LD , flase -> SD
    Register r ;
    int address ;
    Memory memory ;


    public LoadStoreInstruction( boolean op , Register r , int address , Memory memory){
        this.op=op;
        this.r=r;
        this.address=address;
        this.memory=memory;
    }


    public double LoadStore () {
        if (op){
            return memory.readData(address);
        }
        else 
          memory.writeData(address, r.getValue());
         return r.getValue();
    }
    
}
