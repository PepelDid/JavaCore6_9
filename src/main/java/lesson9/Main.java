package lesson9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        List<People> students = new ArrayList<>();
        students.add(new People("Kat", Arrays.asList(People.Course.BIO, People.Course.LANG)));
        students.add(new People("John", Arrays.asList(People.Course.BIO, People.Course.FYZ, People.Course.MATH)));
        students.add (new People("Marco", Arrays.asList(People.Course.BIO, People.Course.CHEM, People.Course.MATH, People.Course.FYZ, People.Course.LANG)));
        students.add(new People("Ronald", Arrays.asList(People.Course.HIST, People.Course.SOC, People.Course.MATH, People.Course.LANG)));
        students.add(new People("Fiona", Arrays.asList(People.Course.POLIT, People.Course.LANG, People.Course.MATH)));
        students.add(new People("Beata", Arrays.asList(People.Course.POLIT, People.Course.LITER)));

        printStudent(students);
        printUniqCourses(students);
        printHardWorkingStudents(students);
        printListForSubject(students, People.Course.MATH);
    }

    public static void printStudent(List<People> students){
        for (People e : students){
        System.out.println(e.getName() + " studies " + e.getAllCourses());
        }
    }

    public static void printUniqCourses(List<People> students){
        System.out.println(
                students.stream()
                        .map(w -> w.getAllCourses())
                        .flatMap(s->s.stream())
                        .distinct()
                        .collect(Collectors.toList()));
    }

    public static void printHardWorkingStudents(List<People> students){
//У меня не получается построить Map с ключом-именем и значением-набором курсов,получается только
// определить первую по величине тройку. Не знаю,как определять имя,если уже упростила поток до набора курсов.
        System.out.println(
                students.stream()
                        .map(w -> w.getAllCourses())
                        .sorted((s1,s2)->s2.size() - s1.size())
                        .limit(3)
                        .collect(Collectors.toList()));

    }
    public static List<People> printListForSubject(List<People> students, People.Course course){
       List<People> subject = students.stream()
                        .filter(o-> o.getAllCourses().contains(course))
                        .collect(Collectors.toList());
 //прописала вывод для себя
        System.out.println(course + " study ");
        for(People e: subject){
            System.out.println(e.getName());

       }
        return subject;
    }
}
