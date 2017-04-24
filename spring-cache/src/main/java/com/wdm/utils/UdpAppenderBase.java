package com.wdm.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.net.SyslogConstants;
import ch.qos.logback.core.net.SyslogOutputStream;

public abstract class UdpAppenderBase<E> extends AppenderBase<E> {

    final static String SYSLOG_LAYOUT_URL = CoreConstants.CODES_URL + "#syslog_layout";
    final static int MSG_SIZE_LIMIT = 256 * 1024;

    Layout<E> layout;
    String facilityStr;
    String syslogHost;
    protected String suffixPattern;
    SyslogOutputStream sos;
    int port = SyslogConstants.SYSLOG_PORT;
    String sendPort = "0000";

    public void start() {
        int errorCount = 0;
        if (facilityStr == null) {
            addError("The Facility option is mandatory");
            errorCount++;
        }
        try {
            sos = new SyslogOutputStream(syslogHost, port);
        } catch (UnknownHostException e) {
            addError("Could not create SyslogWriter", e);
            errorCount++;
        } catch (SocketException e) {
            addWarn("Failed to bind to a random datagram socket. Will try to reconnect later.", e);
        }

        if (layout == null) {
            layout = buildLayout();
        }
        if (errorCount == 0) {
            super.start();
        }
    }

    abstract public Layout<E> buildLayout();

    abstract public int getSeverityForEvent(Object eventObject);

    @Override
    protected void append(E eventObject) {
        if (!isStarted()) {
            return;
        }
        try {
            String msg = layout.doLayout(eventObject);
            if (msg == null) {
                return;
            }
            msg = converHostname2IpPort(msg);
            if (msg.length() > MSG_SIZE_LIMIT) {
                msg = msg.substring(0, MSG_SIZE_LIMIT);
            }
            sos.write(msg.getBytes());
            sos.flush();
            postProcess(eventObject, sos);
        } catch (IOException ioe) {
            addError("Failed to send diagram to " + syslogHost, ioe);
        }
    }

    protected void postProcess(Object event, OutputStream sw) {
    }

    protected String converHostname2IpPort(String hostnameString) {
        String resIpPortString = hostnameString;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipPort = inetAddress.getHostAddress() + "-" + sendPort;
            String hostname = inetAddress.getHostName();
            resIpPortString = hostnameString.replace(hostname, ipPort);
        } catch (Exception e) {
            addError("Failed to convert hostname to IpPort", e);
        }
        return resIpPortString;
    }

    static public int facilityStringToint(String facilityStr) {
        if ("KERN".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_KERN;
        } else if ("USER".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_USER;
        } else if ("MAIL".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_MAIL;
        } else if ("DAEMON".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_DAEMON;
        } else if ("AUTH".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_AUTH;
        } else if ("SYSLOG".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_SYSLOG;
        } else if ("LPR".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LPR;
        } else if ("NEWS".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_NEWS;
        } else if ("UUCP".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_UUCP;
        } else if ("CRON".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_CRON;
        } else if ("AUTHPRIV".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_AUTHPRIV;
        } else if ("FTP".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_FTP;
        } else if ("LOCAL0".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL0;
        } else if ("LOCAL1".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL1;
        } else if ("LOCAL2".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL2;
        } else if ("LOCAL3".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL3;
        } else if ("LOCAL4".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL4;
        } else if ("LOCAL5".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL5;
        } else if ("LOCAL6".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL6;
        } else if ("LOCAL7".equalsIgnoreCase(facilityStr)) {
            return SyslogConstants.LOG_LOCAL7;
        } else {
            throw new IllegalArgumentException(facilityStr + " is not a valid syslog facility string");
        }
    }

    public String getSyslogHost() {
        return syslogHost;
    }

    public void setSyslogHost(String syslogHost) {
        this.syslogHost = syslogHost;
    }

    public String getFacility() {
        return facilityStr;
    }

    public void setFacility(String facilityStr) {
        if (facilityStr != null) {
            facilityStr = facilityStr.trim();
        }
        this.facilityStr = facilityStr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Layout<E> getLayout() {
        return layout;
    }

    public void setLayout(Layout<E> layout) {
        addWarn("The layout of a SyslogAppender cannot be set directly. See also " + SYSLOG_LAYOUT_URL);
    }

    @Override
    public void stop() {
        sos.close();
        super.stop();
    }

    public String getSuffixPattern() {
        return suffixPattern;
    }

    public void setSuffixPattern(String suffixPattern) {
        this.suffixPattern = suffixPattern;
    }

    public String getSendPort() {
        return sendPort;
    }

    public void setSendPort(String sendPort) {
        this.sendPort = sendPort;
    }
}

