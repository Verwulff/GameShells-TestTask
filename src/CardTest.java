import java.io.IOException;

public class CardTest {
    public static void main(String args[]) throws IOException {
        if (args.length<1)
            System.out.println("Wrong path");
        System.out.println("some changes 3");
        Identifier identifier = new Identifier(args[0]);
        identifier.start();
    }
}