package eu.clarussecure.proxy.protocol.plugins.pgsql;

import eu.clarussecure.proxy.spi.protocol.Configuration;
import eu.clarussecure.proxy.spi.protocol.ProtocolCapabilities;

public class PgsqlConfiguration extends Configuration {

    public static final String PROTOCOL_NAME = "PostgreSQL";

    public static final int DEFAULT_LISTEN_PORT = 5432;

    public PgsqlConfiguration(ProtocolCapabilities capabilities) {
        super(capabilities);
    }

    @Override
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    public int getDefaultListenPort() {
        return DEFAULT_LISTEN_PORT;
    }

}
