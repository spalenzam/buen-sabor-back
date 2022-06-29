package com.buenSabor.controllers;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.buenSabor.entity.ArticuloManufacturado;
import com.buenSabor.entity.Email;
import com.buenSabor.services.EmailService;

@RestController
@RequestMapping(path = "api/buensabor/email")
public class EmailController {
	
	@Autowired private EmailService emailService;
	 
    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody Email email)
    {
        String status
            = emailService.sendSimpleMail(email);
 
        return status;
    }
 
    // Sending email with attachment
    @PostMapping("/sendMailAdjunto")
    public String sendMailWithAttachment(@RequestBody Email email)
    {
        String status
            = emailService.sendMailWithAttachment(email);
 
        return status;
    }
    
 
    
    

}
