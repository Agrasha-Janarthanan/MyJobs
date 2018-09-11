import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// class UniqueNumber {
public class UniqueNumber {

    // static void execute() {
    public static void main(String[] args) {

        // List<Integer> randomNumbers = Array.asList({1, 6, 10, 1, 25, 78, 10, 25});
        List<Integer> randomNumbers = Arrays.asList(1, 6, 10, 1, 25, 78, 10, 25);

        // List<Integer> uniqueNumbers = randomNumbers.getDistinct();
        List<Integer> uniqueNumbers = randomNumbers
                                     .stream()
                                     .distinct()
                                     .collect(Collectors.toList());

        // Console console = getConsole()...
        // console.print(uniqueNumbers);
        System.out.println("Distinct numbers in the list : " + uniqueNumbers);
    }
}
