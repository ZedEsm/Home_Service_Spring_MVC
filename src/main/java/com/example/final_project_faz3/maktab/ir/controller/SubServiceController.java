package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.model.entity.SubService;
import com.example.final_project_faz3.maktab.ir.exceptions.ServiceExistenceException;
import com.example.final_project_faz3.maktab.ir.exceptions.SubServiceExistenceException;
import com.example.final_project_faz3.maktab.ir.service.SubServicesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subservice")
public class SubServiceController {
    private final SubServicesService subServicesService;

    public SubServiceController(SubServicesService subServicesService) {
        this.subServicesService = subServicesService;
    }

    @PostMapping("/postSubservice")
    public void registerStudent(@RequestBody SubService subService) {
        try {
            subServicesService.checkSubServiceExistence(subService);
            subServicesService.saveSubService(subService);

        } catch (SubServiceExistenceException | ServiceExistenceException e) {
            System.out.println(e.getMessage());
        }
    }

    @PutMapping(path = "/update/{subName}")
    public void updateDescription(@PathVariable("subName") String name,
                                  @RequestParam(required = false) String description) throws SubServiceExistenceException {
        subServicesService.updateDescription(name, description);

    }
}
