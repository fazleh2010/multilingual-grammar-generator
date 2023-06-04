
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class QueGG {

    public static void main(String[] args) throws Exception {
        System.out.println("Grammar Parser!!!");

        try {
            if (args.length < 2) {
                System.err.printf("Too few parameters (%s/%s)", args.length);
                throw new IllegalArgumentException(String.format("Too few parameters (%s/%s)", args.length));
            } else if (args.length == 2) {

            }

        } catch (Exception e) {
            System.err.printf("%s: %s%n", e.getClass().getSimpleName(), e.getMessage());
        }

    }

}
