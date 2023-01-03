package src.Instructions;

public class RegisterFile {
    public Register registers [] = new Register [32];

    public RegisterFile() {
        for(int i = 0; i<32;i++) {
            registers[i] = new Register();
            registers[i].setName("F"+i);
            //Set Q to empty string
            registers[i].setQi("");
        }
    }

    //check if the register is ready
    public boolean regFileReady(String reg) {
        if(registers[Integer.parseInt(reg.substring(1))].regReady()) {
            return true;
        }
        return false;
    }

    //get the value of the register
    public double getRegFileValue(String reg) {
        return registers[Integer.parseInt(reg.substring(1))].getValue();
    }


    //update the value of the register
    public void updateRegister(String reg , double newVal) {
        registers[Integer.parseInt(reg.substring(1))].updateRegister(newVal);
    }

    //get the Qi of the register
    public String getQi(String reg) {
        return registers[Integer.parseInt(reg.substring(1))].getQi();
    }

    //set the Qi of the register
    public void setQi(String reg , String Qi) {
        registers[Integer.parseInt(reg.substring(1))].setQi(Qi);
    }


    




    
}
