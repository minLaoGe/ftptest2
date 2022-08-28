package com.example.ftptest2.controller;

import com.example.ftptest2.enitity.FTPClient;
import com.example.ftptest2.response.ResponseEntity;
import com.example.ftptest2.service.BoasteService;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Controller("/")
public class BoasteCommand {
    @Autowired
    private BoasteService boasteService;

    @GetMapping("/changeKeyWord/{phoneNumber}")
    public ResponseEntity changeKeyWords(@PathVariable String phoneNumber) throws SftpException {
        boasteService.beginBoastLoginMess(phoneNumber);
        return ResponseEntity.getSuccess(null);
    }
}

