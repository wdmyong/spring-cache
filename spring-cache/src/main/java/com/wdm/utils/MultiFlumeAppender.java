package com.wdm.utils;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.FlumeException;
import org.apache.flume.api.RpcClientConfigurationConstants;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.api.RpcClientFactory.ClientType;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * logback用于发送日志到多个flume agent的类
 *
 * @author yongDuan
 * 20150924
 */
public class MultiFlumeAppender extends FlumeAppender{
    private String hosts;
    private String selector;
    private String maxBackoff;
    private boolean configured = false;

    @Override
    public synchronized void append(ILoggingEvent event) {
        if(!configured) {
            if (isUnsafeMode()) {
                return;
            }
            throw new FlumeException("Flume LogbackAppender not configured correctly! Cannot send events to Flume.");
        }
        super.append(event);
    }

    @Override
    public void activateOptions() throws FlumeException {
        try {
            final Properties properties = getProperties(hosts, selector, maxBackoff, getTimeout());
            rpcClient = RpcClientFactory.getInstance(properties);
            configured = true;
        } catch (Exception e) {
            if (isUnsafeMode()) {
                return;
            }
            throw new FlumeException(e);
        }
    }

    private Properties getProperties(String hosts, String selector, String maxBackoff, long timeout) throws FlumeException {
        if (StringUtils.isEmpty(hosts)) {
            throw new FlumeException("hosts must not be null");
        }

        Properties props = new Properties();
        String[] hostsAndPorts = hosts.split("\\s+");
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < hostsAndPorts.length; i++) {
            String hostAndPort = hostsAndPorts[i];
            String name = "h" + i;
            props.setProperty(RpcClientConfigurationConstants.CONFIG_HOSTS_PREFIX + name, hostAndPort);
            names.append(name).append(" ");
        }
        props.put(RpcClientConfigurationConstants.CONFIG_HOSTS, names.toString());
        props.put(RpcClientConfigurationConstants.CONFIG_CLIENT_TYPE, ClientType.DEFAULT_LOADBALANCE.toString());
        if (!StringUtils.isEmpty(selector)) {
            props.put(RpcClientConfigurationConstants.CONFIG_HOST_SELECTOR, selector);
        }

        if (!StringUtils.isEmpty(maxBackoff)) {
            long millis = Long.parseLong(maxBackoff.trim());
            if (millis <= 0) {
                throw new FlumeException("Misconfigured max backoff, value must be greater than 0");
            }
            props.put(RpcClientConfigurationConstants.CONFIG_BACKOFF, String.valueOf(true));
            props.put(RpcClientConfigurationConstants.CONFIG_MAX_BACKOFF, maxBackoff);
        }
        props.setProperty(RpcClientConfigurationConstants.CONFIG_CONNECT_TIMEOUT, String.valueOf(timeout));
        props.setProperty(RpcClientConfigurationConstants.CONFIG_REQUEST_TIMEOUT, String.valueOf(timeout));
        return props;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }
    public void setSelector(String selector) {
        this.selector = selector;
    }
    public void setMaxBackoff(String maxBackoff) {
        this.maxBackoff = maxBackoff;
    }
}

