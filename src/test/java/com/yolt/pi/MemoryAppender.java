package com.yolt.pi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.read.ListAppender;

/**
 * Test memory appender to verify and assert log messages.
 */
public class MemoryAppender extends ListAppender<ILoggingEvent> {

    public MemoryAppender(Context context) {
        setContext(context);
    }

    public void reset() {
        list.clear();
    }

    public long countEvents(Level level) {
        return list.stream()
                .filter(event -> event.getLevel().equals(level))
                .count();
    }

    public boolean contains(String string,
                            Level level) {
        return list.stream()
                .anyMatch(event -> event.getFormattedMessage().contains(string)
                        && event.getLevel().equals(level));
    }
}
