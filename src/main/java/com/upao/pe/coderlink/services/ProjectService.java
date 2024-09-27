package com.upao.pe.coderlink.services;

import com.upao.pe.coderlink.dtos.postulation.PostulationDTO;
import com.upao.pe.coderlink.dtos.project.CreateProjectRequest;
import com.upao.pe.coderlink.dtos.project.ProjectDTO;
import com.upao.pe.coderlink.dtos.skill.CreateSkillRequest;
import com.upao.pe.coderlink.dtos.skill.SkillDTO;
import com.upao.pe.coderlink.exceptions.ResourceNotExistsException;
import com.upao.pe.coderlink.models.Customer;
import com.upao.pe.coderlink.models.Postulation;
import com.upao.pe.coderlink.models.Project;
import com.upao.pe.coderlink.models.Skill;
import com.upao.pe.coderlink.models.enums.ProjectStatus;
import com.upao.pe.coderlink.repos.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired SkillService skillService;
    @Autowired
    CustomerService customerService;

    // CREATE
    public ProjectDTO createProject(CreateProjectRequest request){
        List<Skill> skills = new ArrayList<>();
        request.getSkills().forEach(skill -> {
            Skill skillToList;
            if(skillService.getSkill(skill) != null){
                skillToList = skillService.getSkill(skill);
            } else{
                skillToList = skillService.createSkill(new CreateSkillRequest(skill));
            }
            skills.add(skillToList);
        });
        Customer customer = customerService.getCustomer(request.getUsername());
        Project project = new Project(null, request.getName(), request.getDescription(), request.getBudget(), request.getDuration(), request.getMilestones(), request.getPresentation(), request.getRevision(), ProjectStatus.valueOf(request.getStatus().toUpperCase()), request.getCategory(), request.getQualification(), LocalDate.now(), null, customer, skills, new ArrayList<>());
        return returnProjectDTO(projectRepository.save(project));
    }

    // READ
    public List<ProjectDTO> listProjects(){return projectRepository.findAll().stream().map(this::returnProjectDTO).toList();}

    // DELETE
    public List<ProjectDTO> deleteProject(Long id){
        Project project = getProjectById(id);
        projectRepository.delete(project);
        return listProjects();
    }

    // DTO
    public ProjectDTO returnProjectDTO(Project project) {
        List<SkillDTO> skills = new ArrayList<>();
        List<PostulationDTO> postulations = new ArrayList<>();
        for(Skill skill : project.getSkills()) {
            SkillDTO skillDTO = new SkillDTO(skill.getName());
            skills.add(skillDTO);
        }
        for(Postulation postulation : project.getPostulations()){
            PostulationDTO postulationDTO = new PostulationDTO(postulation.getPublicationDate(), postulation.getStatus().toString());
            postulations.add(postulationDTO);
        }
        return new ProjectDTO(project.getName(), project.getDescription(), project.getMilestones(), project.getPresentation(), project.getRevision(), project.getStatus().toString(), project.getCategory(), project.getQualification(), project.getCreatedAt(), skills, postulations);
    }

    // Search
    public List<ProjectDTO> getProjectsByName(String name){
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        Optional<List<Project>> projects = projectRepository.findAllByNameContaining(name);
        if(projects.isEmpty()){
            throw new ResourceNotExistsException("No project has not been founded");
        }
        projects.get().forEach(project -> {
            projectDTOS.add(returnProjectDTO(project));
        });
        return projectDTOS;
    }

    public Project getProjectById(Long id){
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()){
            throw new ResourceNotExistsException("This project has not been founded");
        }
        return project.get();
    }

}
