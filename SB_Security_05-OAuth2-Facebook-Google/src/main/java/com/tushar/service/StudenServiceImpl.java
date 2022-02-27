package com.tushar.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tushar.Exception.StudentNotFound;
import com.tushar.entity.SearchStudent;
import com.tushar.entity.Student;
import com.tushar.entity.UserDetails;
import com.tushar.repo.StudentRepo;
import com.tushar.repo.UserDetailsRepo;

@Service("studentService")
public class StudenServiceImpl implements iStudenService {

	@Autowired
	private StudentRepo repo;
	
	@Autowired
	private UserDetailsRepo userRepo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Override
	public Iterable<Student> getAllStudents() {
		return repo.findAll(Sort.by("ENROLLID").ascending());
	}

	@Override
	public String registerStudent(Student student) {
		student.setPasswrd(encoder.encode(student.getPasswrd()));
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

	@Override
	public  org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		int appRegId = 0;
		try {
		 appRegId =Integer.parseInt(username);
		}
		catch(Exception e) {
			System.out.println("StudenServiceImpl.loadUserByUsername() Exception parseInt");
			new StudentNotFound("Please enter correct Numeric registration Number");
		}
		if(appRegId ==0) {
			new StudentNotFound("Please enter registration Number");
		}
		Optional<UserDetails> userDetailsOpt = userRepo.findById(appRegId);
		org.springframework.security.core.userdetails.User user = null;
		if(userDetailsOpt.isEmpty())
			new StudentNotFound("User details not found");
		else {
			UserDetails appUserDetails = userDetailsOpt.get();
			Set<GrantedAuthority> SGA_roles = new HashSet();
			for(String role : appUserDetails.getRoles()) {
				SimpleGrantedAuthority authRole = new SimpleGrantedAuthority(role);
				SGA_roles.add(authRole);
			}
			user = new User(""+appUserDetails.getAPPUSERID(), appUserDetails.getPasswrd(), SGA_roles );
		}//else
		return user;
		
	}//method

	@Override
	public String registerUser(UserDetails userDetails) {
		userDetails.setPasswrd(encoder.encode(userDetails.getPasswrd()));
		return "User Details saved with  id# "+userRepo.save(userDetails).getAPPUSERID();
	}

	
}//class