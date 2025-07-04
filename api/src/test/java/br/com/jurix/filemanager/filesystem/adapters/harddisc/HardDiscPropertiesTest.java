package br.com.jurix.filemanager.filesystem.adapters.harddisc;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class HardDiscPropertiesTest {

    @InjectMocks
    private HardDiscProperties hardDiscProperties;

    @Mock
    private Environment environment;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sholdReturnRootPath(){
        when(environment.getProperty("filesystem.harddisc.rootpath")).thenReturn("target");
        Path path = hardDiscProperties.getRootPath();
        assertEquals(path.toString(), "target");
    }

}
