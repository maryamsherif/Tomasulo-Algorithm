package src.Instructions;

public class Register {

    private double value ;
    private String name;
    private String Qi;



    public Register(){
        this.value = 0;
       // this.ready = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateRegister (double newVal) {
       // ready = true;
        this.value = newVal;
    }

    public double getValue() {
        return value;
    }

    public boolean regReady() {
        if (Qi == null) {
            return true;
        }
        return false;
    }


    public void setValue(double value) {
        this.value = value;
    }

    public String getQi() {
        return Qi;
    }

    public void setQi(String qi) {
        Qi = qi;
    }

    
}
