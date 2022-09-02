package com.example.ftptest2.service;

import com.example.ftptest2.response.ResponseEntity;
import com.jcraft.jsch.SftpException;

public interface BoasteService {
    public void beginBoastLoginMess(String keywords) throws SftpException;
    public void beginBoastPayMess();
}
