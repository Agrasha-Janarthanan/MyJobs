package com.ofs.training;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.ofs.training.Person.Sex;

//class FilterPerson {
public class FilterPerson {

//    public List checkAge() {
//        ......
//    }
//
//    public List checkGender() {
//        ......
//    }


//    static void execute() {
      public static void main(String[] args) {

//      List person = new List();
        ArrayList<Person> people = new ArrayList<> ();

//      List filteredPerson = person.checkAge();
//      List nextFilteredPerson = filteredPerson.checkGender();
        Predicate<Person> predicate = new Predicate<Person> () {

            @Override
            public boolean test(Person person) {
                if ((person.getAge() > 21) && (person.getGender() == Sex.MALE)) {
                    return true;
                }
                return false;
            }

//      Console console = getConsole()...
//      console.print(nextFilteredPerson);
        System.out.println();
        }
}
