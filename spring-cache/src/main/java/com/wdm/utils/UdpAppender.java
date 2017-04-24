package com.wdm.utils;

import java.io.OutputStream;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.SyslogStartConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;

public class UdpAppender extends UdpAppenderBase<ILoggingEvent> {

    static final public String DEFAULT_SUFFIX_PATTERN = "[%thread] %logger %msg";
    static final public String DEFAULT_STACKTRACE_PATTERN = "" + CoreConstants.TAB;

    PatternLayout stackTraceLayout = new PatternLayout();
    String stackTracePattern = DEFAULT_STACKTRACE_PATTERN;

    boolean throwableExcluded = false;

    public void start() {
        super.start();
        setupStackTraceLayout();
    }

    String getPrefixPattern() {
        return "%syslogStart{" + getFacility() + "}%nopex";
    }

    @Override
    public int getSeverityForEvent(Object eventObject) {
        ILoggingEvent event = (ILoggingEvent) eventObject;
        return LevelToSyslogSeverity.convert(event);
    }

    @Override
    protected void postProcess(Object eventObject, OutputStream sw) {
        if (throwableExcluded) {
            return;
        }
        ILoggingEvent event = (ILoggingEvent) eventObject;
        IThrowableProxy tp = event.getThrowableProxy();
        if (tp == null) {
            return;
        }
        String stackTracePrefix = stackTraceLayout.doLayout(event);

        while (tp != null) {
            StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
            try {
                for (StackTraceElementProxy step : stepArray) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(stackTracePrefix).append(step);
                    String msg = converHostname2IpPort(sb.toString());
                    sw.write(msg.getBytes());
                    sw.flush();
                }
            } catch (Exception e) {
                break;
            }
            tp = tp.getCause();
        }
    }

    public Layout<ILoggingEvent> buildLayout() {
        PatternLayout layout = new PatternLayout();
        layout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
        if (suffixPattern == null) {
            suffixPattern = DEFAULT_SUFFIX_PATTERN;
        }
        layout.setPattern(getPrefixPattern() + suffixPattern);
        layout.setContext(getContext());
        layout.start();
        return layout;
    }

    private void setupStackTraceLayout() {
        stackTraceLayout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
        stackTraceLayout.setPattern(getPrefixPattern() + stackTracePattern);
        stackTraceLayout.setContext(getContext());
        stackTraceLayout.start();
    }

    public boolean isThrowableExcluded() {
        return throwableExcluded;
    }

    public void setThrowableExcluded(boolean throwableExcluded) {
        this.throwableExcluded = throwableExcluded;
    }

    public String getStackTracePattern() {
        return stackTracePattern;
    }

    public void setStackTracePattern(String stackTracePattern) {
        this.stackTracePattern = stackTracePattern;
    }
}
