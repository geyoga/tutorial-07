package com.apap.tu07.controller;


import com.apap.tu07.model.PilotModel;
import com.apap.tu07.rest.PilotDetail;
import com.apap.tu07.rest.Setting;
import com.apap.tu07.service.PilotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/pilot")
public class PilotController {
    
    @Autowired
    private PilotService pilotService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Bean
    public RestTemplate rest() {
    	return new RestTemplate();
    }
    
    @GetMapping(value = "/status/{licenseNumber}")
    private String getStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception{
        String path = Setting.pilotUrl + "/pilot?licenseNumber="+licenseNumber;
        return restTemplate.getForEntity(path, String.class).getBody();
    }
    
    @GetMapping(value = "/full/{licenseNumber}")
    private PilotDetail postStatus(@PathVariable("licenseNumber") String licenseNumber) throws Exception {
    	String path = Setting.pilotUrl + "/pilot";
    	PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
    	PilotDetail detail = restTemplate.postForObject(path, pilot, PilotDetail.class);
        return detail;
    }
    
    @PostMapping(value = "/add")
    public PilotModel addPilotSubmit (@RequestBody PilotModel pilot){
        return pilotService.addPilot(pilot);
    }
    
    @GetMapping(value = "/view/{licenseNumber}")
    public PilotModel pilotView(@PathVariable("licenseNumber") String licenseNumber){
        PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber).get();
        return pilot;
    }
    
    @DeleteMapping(value = "/delete")
    public String deletePilot(@RequestParam("pilotId") long pilotId){
        PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
        pilotService.deletePilot(pilot);
        return "success";
    }
    
    @PutMapping(value = "/update/{pilotId}")
    public String updatePilotSubmit(@PathVariable("pilotId") long pilotId,
                                    @RequestParam("name") String  name,
                                    @RequestParam("flyHour") int flyHour){
        PilotModel pilot = pilotService.getPilotDetailById(pilotId).get();
        if (pilot.equals(null)){
            return "Couldn't find your pilot";
        }
        
        pilot.setName(name);
        pilot.setFlyHour(flyHour);
        pilotService.updatePilot(pilotId, pilot);
        return "update";
    }
    
}