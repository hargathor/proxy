package eu.clarussecure.proxy.protocol.plugins.pgsql;

import java.util.Arrays;

import eu.clarussecure.proxy.protocol.plugins.tcp.TCPServer;
import eu.clarussecure.proxy.spi.protocol.ProtocolCapabilities;
import eu.clarussecure.proxy.spi.protocol.ProtocolExecutor;

public class PgsqlProtocol extends ProtocolExecutor {

    private static class Helper {
        private static final PgsqlCapabilities CAPABILITIES = new PgsqlCapabilities();
        private static final PgsqlConfiguration CONFIGURATION = new PgsqlConfiguration(CAPABILITIES);
    }

    @Override
    public ProtocolCapabilities getCapabilities() {
        return Helper.CAPABILITIES;
    }

    @Override
    public PgsqlConfiguration getConfiguration() {
        return Helper.CONFIGURATION;
    }

    @Override
    protected TCPServer<FrontendSidePipelineInitializer, BackendSidePipelineInitializer> buildServer() {
        return new TCPServer<>(getConfiguration(), FrontendSidePipelineInitializer.class,
                BackendSidePipelineInitializer.class, 0);
    }

    @Override
    public String[] adaptDataIds(String[] dataIds) {
        // Add the public schema if any
        dataIds = Arrays.stream(dataIds).map(id -> Helper.CONFIGURATION.adaptDataId(id)).toArray(String[]::new);
        return dataIds;
    }

    @Override
    public String[] getDatasetPrefixByServer() {
        return getConfiguration().getBackendDatabaseNames().stream().toArray(String[]::new);
    }
}
