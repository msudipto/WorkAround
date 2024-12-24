package com.cs309.tutorial.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/student")
public class StudentController {

	private final StudentRepository studentRepo;
	private final CourseRepository courseRepository;

	// Constructor-based injection to replace field injection
	@Autowired
	public StudentController(StudentRepository studentRepo, CourseRepository courseRepository) {
		this.studentRepo = studentRepo;
		this.courseRepository = courseRepository;
	}

	@PostMapping("/create")
	public void createStudent() {
		Student student = new Student();
		studentRepo.save(student);
	}

	@GetMapping("/get")
	public Student getStudent(@RequestParam Long id) {
		return studentRepo.findById(id).orElse(null); // Replaced deprecated getOne() with findById()
	}

	@PostMapping("/registerCourse")
	public void registerCourse() {
		// Implement registration logic
	}
}