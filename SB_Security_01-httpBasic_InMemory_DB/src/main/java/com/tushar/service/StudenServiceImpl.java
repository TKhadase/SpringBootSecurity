package com.tushar.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tushar.Exception.StudentNotFound;
import com.tushar.entity.SearchStudent;
import com.tushar.entity.Student;
import com.tushar.repo.StudentRepo;

@Service("studentService")
public class StudenServiceImpl implements iStudenService {

	@Autowired
	private StudentRepo repo;
	
	@Override
	public Iterable<Student> getAllStudents() {
		return repo.findAll(Sort.by("ENROLLID").ascending());
	}

	@Override
	public String registerStudent(Student student) {
		return "Student Details saved with enroll id# "+repo.save(student).getENROLLID();
	}

	@Override
	public Student  getStudentDetails(Integer enroll_id) {
		return repo.findById(enroll_id).orElseThrow(()->
			new StudentNotFound("Student details not found"));
	}

	@Override
	public String updateStudentDetails(Student student) {
		return "Student Details updated for enroll id#"+repo.save(student).getENROLLID();
	}

	@Override
	public String removeStudentDetails(Integer enroll_id) {
		repo.deleteById(enroll_id);
		return "Student Details removed for enroll id#"+enroll_id;
	}

	@Override
	public List<Student> searchStudentsByDetails(SearchStudent student) {
				return repo.searchByDetails(student.getENROLLID(), student.getROLLID(), student.getFname(), student.getMname(), student.getLname(), student.getAddress1(), student.getAddress2(), student.getMobile(), student.getDob(), student.getEmail());
	}

	@Override
	public Page<Student> getAllStudentsByPage(Pageable pageable) {
		return repo.findAll(pageable);
	}


}
