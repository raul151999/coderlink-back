package com.upao.pe.coderlink.dtos.project;

import com.upao.pe.coderlink.dtos.postulation.PostulationDTO;
import com.upao.pe.coderlink.dtos.skill.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectDTO {
    private String name;
    private String description;
    private String milestones;
    private String presentation;
    private String revision;
    private String status;
    private String category;
    private String qualification;
    private LocalDate createdAt;
    private List<SkillDTO> skills;
    private List<PostulationDTO> postulations;
}
