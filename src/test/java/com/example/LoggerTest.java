package com.example;

import com.example.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;

public class LoggerTest {
    
    @Test
    void testSingleton() {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        assertSame(logger1, logger2);
    }

    @Test
    void testLog() throws IOException {
        Logger logger = Logger.getInstance();
        String testMsg = "hello hello test 1 2";
        logger.log(testMsg, "log_test.txt");

        String content = Files.readString(Paths.get("log_test.txt"));
        assertTrue(content.contains(testMsg));

      
    }

}
