package model;

public class Parameters<T1, T2> {

    private T1 status;
    private T2 name;

    public Parameters(T1 status, T2 name){
        this.status = status;
        this.name = name;
    }

    public T1 getStatus() {
        return status;
    }

    public T2 getName() {
        return name;
    }
}
