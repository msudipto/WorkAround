package coms309.people;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentsController {
    ArrayList<Person> peopleList = new ArrayList<>();

    @PutMapping("/updatePeople/{id}")
    public void updatePerson(@RequestBody Person person, @PathVariable int id){
        if (id < 0 || id >= peopleList.size()) {
            throw new IllegalArgumentException("Not found");

        }
        peopleList.set(id,person);
    }
    @PostMapping("/create/people")
    public  String createPerson(@RequestBody Person person) {
        peopleList.add(person);
        return "New person "+ person.getFirstName() + " Saved " + peopleList.size() + peopleList.get(peopleList.size() - 1);
    }

    @PostMapping("/create/students")
    public String createStudent(@RequestBody Students student) {
        peopleList.add(student);
        return "New student " + student.getFirstName() + " with University ID " + student.getUniversityId() + " saved";
    }

    @GetMapping("/people/uid/{id}")
    public ResponseEntity<Person> getUniversityId(@PathVariable int id) {
        if(id < 0 || id >= peopleList.size()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(peopleList.get(id));
    }
    @DeleteMapping("/deletePeople")
     public void deleteAll()
     {
         peopleList = new ArrayList<>();
     }

}

