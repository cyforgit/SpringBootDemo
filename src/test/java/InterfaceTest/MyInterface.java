package InterfaceTest;

public interface MyInterface {

    public default void reportName(){
        System.out.println("I'm your father");
    } ;
}
