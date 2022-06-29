package com.buenSabor.services;

import org.springframework.core.io.FileSystemResource;

import com.buenSabor.entity.Email;
import com.commons.services.CommonService;

public interface EmailService extends CommonService<Email>{

	// Method
    // To send a simple email
    String sendSimpleMail(Email details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(Email details);
    
}
