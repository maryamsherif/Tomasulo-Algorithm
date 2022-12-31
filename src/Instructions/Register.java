package src.Instructions;

public class Register {
    private boolean ready;
    private double value ;


    public Register(){
        this.value = 0;
        this.ready = true;
    }

    public void updateRegister (double newVal) {
        ready = true;
        this.value = newVal;
    }

    public double getValue() {
        return value;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setValue(double value) {
        this.value = value;
    }

    
}
