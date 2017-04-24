package com.wdm.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.FlumeException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientConfigurationConstants;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.clients.log4jappender.Log4jAvroHeaders;
import org.apache.flume.event.EventBuilder;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * logback用于发送日志到flume的appender类
 *
 * @author yongDuan
 * 20150924
 */
public class FlumeAppender extends AppenderBase<ILoggingEvent> {
    private String hostname;
    private int port;
    protected boolean unsafeMode = false;
    private long timeout = RpcClientConfigurationConstants.DEFAULT_REQUEST_TIMEOUT_MILLIS;

    private boolean avroReflectionEnabled;
    private String avroSchemaUrl;
    private Schema schema;
    private ByteArrayOutputStream out;
    private DatumWriter<Object> writer;
    private BinaryEncoder binaryEncoder;

    protected RpcClient rpcClient = null;
    protected PatternLayoutEncoder encoder;

    @Override
    public void start() throws FlumeException{
        activateOptions();
        if (this.encoder == null) {
            addError("No encoder set for the appender named ["+ name +"].");
            return;
        }
        super.start();
    }

    public void activateOptions() throws FlumeException {
        Properties props = new Properties();
        props.setProperty(RpcClientConfigurationConstants.CONFIG_HOSTS, "h1");
        props.setProperty(RpcClientConfigurationConstants.CONFIG_HOSTS_PREFIX + "h1", hostname + ":" + port);
        props.setProperty(RpcClientConfigurationConstants.CONFIG_CONNECT_TIMEOUT, String.valueOf(timeout));
        props.setProperty(RpcClientConfigurationConstants.CONFIG_REQUEST_TIMEOUT, String.valueOf(timeout));
        try {
            rpcClient = RpcClientFactory.getInstance(props);
        } catch (FlumeException e) {
            if (unsafeMode) {
                return;
            }
            throw e;
        }
    }

    @Override
    public void append(ILoggingEvent event) throws FlumeException{
        if (rpcClient == null) {
            String errorMsg = "Cannot Append to Appender! Appender either closed or not setup correctly!";
            if (unsafeMode) {
                return;
            }
            throw new FlumeException(errorMsg);
        }
        if (!rpcClient.isActive()) {
            reconnect();
        }

        Map<String, String> hdrs = new HashMap<String, String>();
        hdrs.put(Log4jAvroHeaders.LOGGER_NAME.toString(), event.getLoggerName());
        hdrs.put(Log4jAvroHeaders.TIMESTAMP.toString(), String.valueOf(event.getTimeStamp()));
        hdrs.put(Log4jAvroHeaders.LOG_LEVEL.toString(), String.valueOf(event.getLevel().toInt()));
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ip = inetAddress.getHostAddress();
            hdrs.put("host", ip);
        } catch (Exception e) {
        }

        Event flumeEvent;
        Object message = event.getMessage();
        if (message instanceof GenericRecord) {
            GenericRecord record = (GenericRecord) message;
            populateAvroHeaders(hdrs, record.getSchema(), message);
            flumeEvent = EventBuilder.withBody(serialize(record, record.getSchema()), hdrs);
        } else if (message instanceof SpecificRecord || avroReflectionEnabled) {
            Schema schema = ReflectData.get().getSchema(message.getClass());
            populateAvroHeaders(hdrs, schema, message);
            flumeEvent = EventBuilder.withBody(serialize(message, schema), hdrs);
        } else {
            hdrs.put(Log4jAvroHeaders.MESSAGE_ENCODING.toString(), "UTF8");
            String msg = encoder != null ? encoder.getLayout().doLayout(event) : message.toString();
            flumeEvent = EventBuilder.withBody(msg, Charset.forName("UTF8"), hdrs);
        }

        try {
            rpcClient.append(flumeEvent);
        } catch (EventDeliveryException e) {
            String msg = "Flume append() failed.";
            if (unsafeMode) {
                return;
            }
            throw new FlumeException(msg + " Exception follows.", e);
        }
    }

    public synchronized void close() throws FlumeException {
        if (rpcClient != null) {
            try {
                rpcClient.close();
            } catch (FlumeException ex) {
                if (unsafeMode) {
                    return;
                }
                throw ex;
            } finally {
                rpcClient = null;
            }
        } else {
            String errorMsg = "Flume logback appender already closed!";
            if(unsafeMode) {
                return;
            }
            throw new FlumeException(errorMsg);
        }
    }

    protected void populateAvroHeaders(Map<String, String> hdrs, Schema schema, Object message) {
        if (avroSchemaUrl != null) {
            hdrs.put(Log4jAvroHeaders.AVRO_SCHEMA_URL.toString(), avroSchemaUrl);
            return;
        }
        hdrs.put(Log4jAvroHeaders.AVRO_SCHEMA_LITERAL.toString(), schema.toString());
    }

    private byte[] serialize(Object datum, Schema datumSchema) throws FlumeException {
        if (schema == null || !datumSchema.equals(schema)) {
            schema = datumSchema;
            out = new ByteArrayOutputStream();
            writer = new ReflectDatumWriter<Object>(schema);
            binaryEncoder = EncoderFactory.get().binaryEncoder(out, null);
        }
        out.reset();
        try {
            writer.write(datum, binaryEncoder);
            binaryEncoder.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new FlumeException(e);
        }
    }

    private void reconnect() throws FlumeException {
        close();
        activateOptions();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isUnsafeMode() {
        return unsafeMode;
    }

    public void setUnsafeMode(boolean unsafeMode) {
        this.unsafeMode = unsafeMode;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setAvroReflectionEnabled(boolean avroReflectionEnabled) {
        this.avroReflectionEnabled = avroReflectionEnabled;
    }

    public void setAvroSchemaUrl(String avroSchemaUrl) {
        this.avroSchemaUrl = avroSchemaUrl;
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }
}

