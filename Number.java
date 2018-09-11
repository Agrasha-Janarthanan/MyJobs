package com.ofs.training;

import java.util.Arrays;
import java.util.List;

// class Number {
public class Number {

    // static void execute() {
    public static void main(String[] args) {

        // List<Integer> randomNumbers = Array.asList({1, 6, 10, 25, 78});
        List<Integer> randomNumbers = Arrays.asList(1, 6, 10, 25, 78);

        // int result = randomNumbers.getSum();
        int result = randomNumbers.stream().mapToInt(Integer :: intValue).sum();

        // Console console = getConsole()....
        // console.print(result);
        System.out.format("Sum of the numbers in the list : %d", result);
    }
}