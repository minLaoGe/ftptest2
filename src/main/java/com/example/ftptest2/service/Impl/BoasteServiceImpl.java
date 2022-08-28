package com.example.ftptest2.service.Impl;

import com.example.ftptest2.enitity.FTPClient;
import com.example.ftptest2.enitity.FTPConfigAdopt;
import com.example.ftptest2.response.ResponseEntity;
import com.example.ftptest2.service.BoasteService;
import com.example.ftptest2.utils.FtpTestClient;
import com.jcraft.jsch.SftpException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BoasteServiceImpl implements BoasteService {
    private  final FtpTestClient ftpsClient;
    @Override
    @Async
    public void beginBoastLoginMess(String keywords) throws SftpException {
       ftpsClient.beginBoradLoginEvent(keywords);

    }
}
