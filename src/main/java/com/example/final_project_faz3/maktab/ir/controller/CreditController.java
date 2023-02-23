package com.example.final_project_faz3.maktab.ir.controller;

import com.example.final_project_faz3.maktab.ir.data.model.entity.Credit;
import com.example.final_project_faz3.maktab.ir.service.CreditService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/credits")
public class CreditController {
    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/postCredit")
    public void registerCredit(@RequestBody Credit credit) {
        creditService.saveCredit(credit);
    }

    @PostMapping("/postCreditCustomer/{ID}")
    public void registerCredit(@RequestBody Credit credit, @PathVariable Long ID) {
        creditService.saveCustomerCredit(ID,credit);
    }
}
