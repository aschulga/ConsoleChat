package model;

public class Parameters<T1, T2> {

    private String status;
    private String name;

    public Parameters(String status, String name){
        this.status = status;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
