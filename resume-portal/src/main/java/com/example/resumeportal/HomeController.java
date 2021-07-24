package com.example.resumeportal;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.resumeportal.models.Education;
import com.example.resumeportal.models.Job;
import com.example.resumeportal.models.UserProfile;

@Controller
public class HomeController {

	
	@Autowired
	UserProfileRepository userProfileRpository;

	@GetMapping("/")
	public String home() {
		
		Optional<UserProfile> userprofile1 = userProfileRpository.findByUserName("Dazy");
		
		userprofile1.orElseThrow(() -> new RuntimeException("Not found"));
		
		UserProfile userProfile = userprofile1.get();
		
		Job job1 = new Job();
		job1.setCompany("Company1");
		job1.setDesignation("Designation");
		job1.setId(1);
		job1.setStartDate(LocalDate.of(2020, 1, 1));
		//job1.setEndDate(LocalDate.of(2020, 3, 1));
		job1.setCurrentJob(true);
		job1.getResponsibilities().add("Job 1 Responsibilities 1");
		job1.getResponsibilities().add("Job 1 Responsibilities 2");
		job1.getResponsibilities().add("Job 1 Responsibilities 3");
		
		
		Job job2 = new Job();
		job2.setCompany("Company2");
		job2.setDesignation("Designation");
		job2.setId(2);
		job2.setStartDate(LocalDate.of(2019, 5, 1));
		job2.setEndDate(LocalDate.of(2020, 1, 1));
		job2.getResponsibilities().add("Job 2 Responsibilities 1");
		job2.getResponsibilities().add("Job 2 Responsibilities 2");
		job2.getResponsibilities().add("Job 2 Responsibilities 3");
		
		
		Education e1 = new Education();
		e1.setCollege("Awesome College 1");
		e1.setQualification("Useless Degree 1");
		e1.setSummary("studies a lot");
		e1.setStartDate(LocalDate.of(2020, 1, 1));
		e1.setEndDate(LocalDate.of(2020, 3, 1));
		
		
		Education e2 = new Education();
		e2.setCollege("Awesome College 2");
		e2.setQualification("Useless Degree 2");
		e2.setSummary("studies a lot");
		e2.setStartDate(LocalDate.of(2020, 1, 1));
		e2.setEndDate(LocalDate.of(2020, 3, 1));
		
		
		userProfile.getSkills().clear();
		userProfile.getSkills().add("Java");
		userProfile.getSkills().add("Spring");
		userProfile.getSkills().add("Hibernate");
		userProfile.getSkills().add("React.js");
		userProfile.getEducations().clear();
		userProfile.getEducations().add(e1);
		userProfile.getEducations().add(e2);
		userProfile.getJobs().clear();
		userProfile.getJobs().add(job1);
		userProfile.getJobs().add(job2);
		
		userProfileRpository.save(userProfile);
		
		return "index";
	}

	@GetMapping("/edit")
	public String edit(Principal principal, Model model, @RequestParam(required = false) String add) {
		
		String userId = principal.getName();
		
		Optional<UserProfile> userProfileOptional = userProfileRpository.findByUserName(userId);
		
		userProfileOptional.orElseThrow(() -> new RuntimeException("Not found" + userId));
		UserProfile savedUserProfile = userProfileOptional.get();
		
		if("job".equals(add)) {
			savedUserProfile.getJobs().add(new Job());
		}else if("education".equals(add)) {
			savedUserProfile.getEducations().add(new Education());
		}else if("skill".equals(add)) {
			savedUserProfile.getSkills().add("");
		}
		
		//userProfileRpository.save(savedUserProfile);
		
		model.addAttribute("userProfile", savedUserProfile);
		model.addAttribute("userId", userId);
		
		return "profile-edit"; 
	}
	
	
	@PostMapping("/edit")
	public String postEdit(Principal principal, Model model, @ModelAttribute UserProfile userProfile) {
		
		String userName = principal.getName();
		Optional<UserProfile> userProfileOptional = userProfileRpository.findByUserName(userName);
		
		userProfileOptional.orElseThrow(() -> new RuntimeException("Not found" + userName));
		UserProfile saveduserProfile = userProfileOptional.get();
		
		userProfile.setId(saveduserProfile.getId());
		userProfile.setUserName(userName);
		
		userProfileRpository.save(userProfile);
		
		return "redirect:/view/" + userName; 
	}
	
	
	@GetMapping("/delete")
	public String postEdit(Principal principal, Model model, @RequestParam String type, @RequestParam int index) {
		
		String userId = principal.getName();
		
		Optional<UserProfile> userProfileOptional = userProfileRpository.findByUserName(userId);
		
		userProfileOptional.orElseThrow(() -> new RuntimeException("Not found" + userId));
		UserProfile savedUserProfile = userProfileOptional.get();
		
		if("job".equals(type)) {
			savedUserProfile.getJobs().remove(index);
		}else if("education".equals(type)) {
			savedUserProfile.getEducations().remove(index);
		}else if("skill".equals(type)) {
			savedUserProfile.getSkills().remove(index);
		}
		
		userProfileRpository.save(savedUserProfile);
		
	
		return "redirect:/edit"; 
	}
	
	
	

	@GetMapping("/view/{userId}")
	public String view(Principal principal, @PathVariable String userId, Model model) {
		if(principal != null && principal.getName() != null) {
			boolean currentUserProfile = principal.getName().equals(userId);
			model.addAttribute("currentUserProfile",currentUserProfile);
		}
		
		Optional<UserProfile> userProfileOptional = userProfileRpository.findByUserName(userId);
		
		userProfileOptional.orElseThrow(() -> new RuntimeException("Not found" + userId));
		UserProfile userProfile = userProfileOptional.get();
		
		model.addAttribute("userProfile", userProfile);
		model.addAttribute("userId", userId);
		System.out.println(userProfile.getJobs());
		return "profile-templates/" + userProfile.getTheme() + "/index";
	}

}
