package br.com.jurix.filemanager.filesystem.adapters.harddisc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class HardDiscProperties {

    @Autowired
    private Environment environment;

    public Path getRootPath(){
        return Paths.get(environment.getProperty("filesystem.harddisc.rootpath"));
    }
}
