/*
 * Copyright 2011 Midokura Europe SARL
 */

package com.midokura.midonet.client.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@XmlRootElement
public class DtoRule {
    public static final String Accept = "accept";
    public static final String Continue = "continue";
    public static final String Drop = "drop";
    public static final String Jump = "jump";
    public static final String Reject = "reject";
    public static final String Return = "return";
    public static final String DNAT = "dnat";
    public static final String SNAT = "snat";
    public static final String RevDNAT = "rev_dnat";
    public static final String RevSNAT = "rev_snat";

    private URI uri;
    private UUID id;
    private UUID chainId;
    private boolean condInvert;
    private boolean matchForwardFlow;
    private boolean matchReturnFlow;
    private UUID[] inPorts;
    private boolean invInPorts;
    private UUID[] outPorts;
    private boolean invOutPorts;
    private UUID portGroup;
    private boolean invPortGroup;
    private Short dlType = null;
    private boolean invDlType = false;
    private String dlSrc = null;
    private boolean invDlSrc = false;
    private String dlDst = null;
    private boolean invDlDst = false;
    private int nwTos;
    private boolean invNwTos;
    private int nwProto;
    private boolean invNwProto;
    private String nwSrcAddress;
    private int nwSrcLength;
    private boolean invNwSrc;
    private String nwDstAddress;
    private int nwDstLength;
    private boolean invNwDst;
    private short tpSrcStart;
    private short tpSrcEnd;
    private boolean invTpSrc;
    private short tpDstStart;
    private short tpDstEnd;
    private boolean invTpDst;
    private String type;
    private String jumpChainName;
    private String flowAction;
    private DtoNatTarget[] natTargets;
    private int position;
    private Map<String, String> properties = new HashMap<String, String>();

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getChainId() {
        return chainId;
    }

    public void setChainId(UUID chainId) {
        this.chainId = chainId;
    }

    public boolean isCondInvert() {
        return condInvert;
    }

    public void setCondInvert(boolean condInvert) {
        this.condInvert = condInvert;
    }

    public boolean isMatchForwardFlow() {
        return matchForwardFlow;
    }

    public void setMatchForwardFlow(boolean matchForwardFlow) {
        this.matchForwardFlow = matchForwardFlow;
    }

    public boolean isMatchReturnFlow() {
        return matchReturnFlow;
    }

    public void setMatchReturnFlow(boolean matchReturnFlow) {
        this.matchReturnFlow = matchReturnFlow;
    }

    public UUID[] getInPorts() {
        return inPorts;
    }

    public void setInPorts(UUID[] inPorts) {
        this.inPorts = inPorts;
    }

    public boolean isInvInPorts() {
        return invInPorts;
    }

    public void setInvInPorts(boolean invInPorts) {
        this.invInPorts = invInPorts;
    }

    public UUID[] getOutPorts() {
        return outPorts;
    }

    public void setOutPorts(UUID[] outPorts) {
        this.outPorts = outPorts;
    }

    public boolean isInvOutPorts() {
        return invOutPorts;
    }

    public void setInvOutPorts(boolean invOutPorts) {
        this.invOutPorts = invOutPorts;
    }

    public boolean isInvPortGroup() {
        return invPortGroup;
    }

    public void setInvPortGroup(boolean invPortGroup) {
        this.invPortGroup = invPortGroup;
    }

    public UUID getPortGroup() {
        return portGroup;
    }

    public void setPortGroup(UUID portGroup) {
        this.portGroup = portGroup;
    }

    public String getDlDst() {
        return dlDst;
    }

    public void setDlDst(String dlDst) {
        this.dlDst = dlDst;
    }

    public boolean isInvDlDst() {
        return invDlDst;
    }

    public void setInvDlDst(boolean invDlDst) {
        this.invDlDst = invDlDst;
    }

    public String getDlSrc() {
        return dlSrc;
    }

    public void setDlSrc(String dlSrc) {
        this.dlSrc = dlSrc;
    }

    public boolean isInvDlSrc() {
        return invDlSrc;
    }

    public void setInvDlSrc(boolean invDlSrc) {
        this.invDlSrc = invDlSrc;
    }

    public Short getDlType() {
        return dlType;
    }

    public void setDlType(Short dlType) {
        this.dlType = dlType;
    }

    public boolean isInvDlType() {
        return invDlType;
    }

    public void setInvDlType(boolean invDlType) {
        this.invDlType = invDlType;
    }

    public int getNwTos() {
        return nwTos;
    }

    public void setNwTos(int nwTos) {
        this.nwTos = nwTos;
    }

    public boolean isInvNwTos() {
        return invNwTos;
    }

    public void setInvNwTos(boolean invNwTos) {
        this.invNwTos = invNwTos;
    }

    public int getNwProto() {
        return nwProto;
    }

    public void setNwProto(int nwProto) {
        this.nwProto = nwProto;
    }

    public boolean isInvNwProto() {
        return invNwProto;
    }

    public void setInvNwProto(boolean invNwProto) {
        this.invNwProto = invNwProto;
    }

    public String getNwSrcAddress() {
        return nwSrcAddress;
    }

    public void setNwSrcAddress(String nwSrcAddress) {
        this.nwSrcAddress = nwSrcAddress;
    }

    public int getNwSrcLength() {
        return nwSrcLength;
    }

    public void setNwSrcLength(int nwSrcLength) {
        this.nwSrcLength = nwSrcLength;
    }

    public boolean isInvNwSrc() {
        return invNwSrc;
    }

    public void setInvNwSrc(boolean invNwSrc) {
        this.invNwSrc = invNwSrc;
    }

    public String getNwDstAddress() {
        return nwDstAddress;
    }

    public void setNwDstAddress(String nwDstAddress) {
        this.nwDstAddress = nwDstAddress;
    }

    public int getNwDstLength() {
        return nwDstLength;
    }

    public void setNwDstLength(int nwDstLength) {
        this.nwDstLength = nwDstLength;
    }

    public boolean isInvNwDst() {
        return invNwDst;
    }

    public void setInvNwDst(boolean invNwDst) {
        this.invNwDst = invNwDst;
    }

    public short getTpSrcStart() {
        return tpSrcStart;
    }

    public void setTpSrcStart(short tpSrcStart) {
        this.tpSrcStart = tpSrcStart;
    }

    public short getTpSrcEnd() {
        return tpSrcEnd;
    }

    public void setTpSrcEnd(short tpSrcEnd) {
        this.tpSrcEnd = tpSrcEnd;
    }

    public boolean isInvTpSrc() {
        return invTpSrc;
    }

    public void setInvTpSrc(boolean invTpSrc) {
        this.invTpSrc = invTpSrc;
    }

    public short getTpDstStart() {
        return tpDstStart;
    }

    public void setTpDstStart(short tpDstStart) {
        this.tpDstStart = tpDstStart;
    }

    public short getTpDstEnd() {
        return tpDstEnd;
    }

    public void setTpDstEnd(short tpDstEnd) {
        this.tpDstEnd = tpDstEnd;
    }

    public boolean isInvTpDst() {
        return invTpDst;
    }

    public void setInvTpDst(boolean invTpDst) {
        this.invTpDst = invTpDst;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJumpChainName() {
        return jumpChainName;
    }

    public void setJumpChainName(String jumpChainName) {
        this.jumpChainName = jumpChainName;
    }

    public String getFlowAction() {
        return flowAction;
    }

    public void setFlowAction(String flowAction) {
        this.flowAction = flowAction;
    }

    public DtoNatTarget[] getNatTargets() {
        return natTargets;
    }

    public void setNatTargets(DtoNatTarget[] natTargets) {
        this.natTargets = natTargets;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public static class DtoNatTarget {
        public String addressFrom, addressTo;
        public int portFrom, portTo;

        public DtoNatTarget() {
        }

        public DtoNatTarget(String addressFrom, String addressTo, int portFrom,
                            int portTo) {
            this.addressFrom = addressFrom;
            this.addressTo = addressTo;
            this.portFrom = portFrom;
            this.portTo = portTo;
        }
    }
}