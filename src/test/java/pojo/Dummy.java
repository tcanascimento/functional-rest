package pojo;

public class Dummy {

    private String name;

    public Dummy(String name){
        this.name = name;
    }

    public Dummy(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dummy{" +
                "name='" + name + '\'' +
                '}';
    }
}
