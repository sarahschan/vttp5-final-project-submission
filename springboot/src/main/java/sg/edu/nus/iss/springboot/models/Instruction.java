package sg.edu.nus.iss.springboot.models;

public class Instruction {
    
    private String step;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "Instruction [step=" + step + "]";
    }

}
