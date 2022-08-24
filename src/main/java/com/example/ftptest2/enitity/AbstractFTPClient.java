package com.example.ftptest2.enitity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

public interface AbstractFTPClient {

    String getHost();

    String getPassword();

    String[] getWitheListCommand();

    String getUsername();

    Integer getPort();

    String getFilename();

    String getSrc();

}
