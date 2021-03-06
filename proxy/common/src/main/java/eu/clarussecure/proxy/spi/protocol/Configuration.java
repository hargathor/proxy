package eu.clarussecure.proxy.spi.protocol;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import eu.clarussecure.proxy.spi.Mode;
import eu.clarussecure.proxy.spi.Operation;
import eu.clarussecure.proxy.spi.protocol.Configurable;
import eu.clarussecure.proxy.spi.protocol.ProtocolCapabilities;
import eu.clarussecure.proxy.spi.protocol.ProtocolService;

public abstract class Configuration implements Configurable {

    public static final int DEFAULT_NB_OF_ACCEPTOR_THREADS = 1;
    
    public static final int DEFAULT_MESSAGE_PART_MAX_LENGTH = Integer.MAX_VALUE;

    private final ProtocolCapabilities capabilities;

    private int listenPort;

    private Set<InetSocketAddress> serverEndpoints;

    private int nbAcceptorThreads = DEFAULT_NB_OF_ACCEPTOR_THREADS;

    private int messagePartMaxLength = DEFAULT_MESSAGE_PART_MAX_LENGTH;

    private Map<Operation, Mode> datasetProcessingModes = new HashMap<Operation, Mode>();
    private Map<Operation, Mode> recordProcessingModes = new HashMap<Operation, Mode>();

    private Set<String> commands = null;

    private ProtocolService protocolService = null;

    public Configuration(ProtocolCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public int getListenPort() {
        return listenPort != 0 ? listenPort : getDefaultListenPort();
    }
    
    @Override
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    public InetSocketAddress getServerEndpoint() {
        if (serverEndpoints != null) {
            if (serverEndpoints.size() > 1) {
                throw new IllegalStateException(String.format("%s endpoints are defined", serverEndpoints.size()));
            }
            return serverEndpoints.iterator().next();
        }
        return null;
    }

    @Override
    public Set<InetSocketAddress> getServerEndpoints() {
        return serverEndpoints;
    }

    @Override
    public void setServerAddress(InetAddress serverAddress) {
        serverEndpoints = Collections.singleton(new InetSocketAddress(serverAddress, getDefaultListenPort()));
    }

    @Override
    public void setServerEndpoint(InetSocketAddress serverEndpoint) {
        serverEndpoints = Collections.singleton(serverEndpoint);
    }

    @Override
    public void setServerAddresses(Set<InetAddress> serverAddresses) {
        serverAddresses.stream().map(serverAddress -> new InetSocketAddress(serverAddress, getDefaultListenPort())).collect(Collectors.toSet());
    }

    @Override
    public void setServerEndpoints(Set<InetSocketAddress> serverEndpoints) {
        this.serverEndpoints = serverEndpoints;
    }

    @Override
    public int getNbAcceptorThreads() {
        return nbAcceptorThreads;
    }

    @Override
    public void setNbAcceptorThreads(int nThreads) {
        this.nbAcceptorThreads = nThreads;
    }

    @Override
    public int getMessagePartMaxLength() {
        return messagePartMaxLength;
    }

    @Override
    public void setMessagePartMaxLength(int maxlen) {
        this.messagePartMaxLength = maxlen;
    }

    @Override
    public void registerDataTypes() {
    }

    @Override
    public Mode getProcessingMode(boolean wholeDataset, Operation operation) {
        if (!capabilities.getSupportedCRUDOperations(wholeDataset).contains(operation)) {
            throw new IllegalArgumentException(String.format("Operation '%s' is not supported on %s", operation, wholeDataset ? "dataset" : "record"));
        }
        return wholeDataset ? datasetProcessingModes.get(operation) : recordProcessingModes.get(operation);
    }

    @Override
    public void setProcessingMode(boolean wholeDataset, Operation operation, Mode mode) {
        if (!capabilities.getSupportedCRUDOperations(wholeDataset).contains(operation)) {
            throw new IllegalArgumentException(String.format("Operation '%s' is not supported on %s", operation, wholeDataset ? "dataset" : "record"));
        }
        if (mode != null && !capabilities.getSupportedProcessingModes(wholeDataset, operation).contains(mode)) {
            throw new IllegalArgumentException(String.format("Processing mode '%s' is not supported for operation '%s' on %s", mode, operation, wholeDataset ? "dataset" : "record"));
        }
        if (wholeDataset) {
            datasetProcessingModes.put(operation, mode);
        } else {
            recordProcessingModes.put(operation, mode);
        }
    }

    @Override
    public Set<String> getComputationCommands() {
        return commands;
    }

    @Override
    public void setComputationCommands(Set<String> commands) {
        if (!capabilities.getSupportedCRUDOperations(true).contains(Operation.READ) && !capabilities.getSupportedCRUDOperations(false).contains(Operation.READ)) {
            throw new IllegalArgumentException("Operation 'READ' is not supported on dataset and on record");
        }
        if (!capabilities.getSupportedProcessingModes(true, Operation.READ).contains(Mode.ORCHESTRATION) && !capabilities.getSupportedProcessingModes(false, Operation.READ).contains(Mode.ORCHESTRATION)) {
            throw new IllegalArgumentException("Processing mode 'ORCHESTRATION' is not supported for operation 'READ' on dataset and on record");
        }
        this.commands = commands;
    }

    @Override
    public ProtocolService getProtocolService() {
        return protocolService;
    }

    @Override
    public void register(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

}
