package org.gradle;

import org.junit.Test;
import static org.junit.Assert.*;

public class ServerLogsTest {
    @Test
    public void checkAlertCondition() {
     
        Long timestampStarted = ServerLogs.timestamp_started;
        Long timestampfinished = ServerLogs.timestamp_finished;
        assertEquals(5, timestampfinished - timestampStarted);
    }
}
