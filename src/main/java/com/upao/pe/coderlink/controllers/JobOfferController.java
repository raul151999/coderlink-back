package com.upao.pe.coderlink.controllers;

import com.upao.pe.coderlink.dtos.offer.CreateJobOfferRequest;
import com.upao.pe.coderlink.dtos.offer.EditJobOfferRequest;
import com.upao.pe.coderlink.dtos.offer.JobOfferDTO;
import com.upao.pe.coderlink.services.JobOfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("offer")
public class JobOfferController {
    @Autowired
    private JobOfferService jobOfferService;

    @GetMapping("/list/")
    public List<JobOfferDTO> listJobOffers(){
        return jobOfferService.listJobOffers();
    }

    @PostMapping("/create/")
    public JobOfferDTO createJobOffer(@Valid @RequestBody CreateJobOfferRequest request){
        return jobOfferService.createJobOffer(request);
    }

    @PutMapping("/update/")
    public JobOfferDTO editJobOffer(@Valid @RequestBody EditJobOfferRequest request){
        return  jobOfferService.editJobOffer(request);
    }

    @DeleteMapping("/delete/{id}")
    public List<JobOfferDTO> deleteJobOffer(@PathVariable Long id){
        return jobOfferService.deleteJobOffer(id);
    }
}
