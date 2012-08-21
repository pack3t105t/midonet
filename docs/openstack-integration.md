## Midolman Cloud Integration Overview - Plugging in vNIC

Midolman must be designed to seamlessly integrate with external open source
cloud orchestration services.  OpenStack is one such service. One of the
main challenges of OpenStack integration is designing the attachment 
process of the VMs to the MidoNet network.  This document describes the
process flow and the implementation details involved in providing networking
connectivity to the VMs running on a MidoNet host.  Also, this document
assumes that OpenStack is using KVM to launch its VMs.

## Terminology

- OpenStack: An operating system for a cloud that manages various resources
such compute, storage, and networking.  It is made up of sub-projects each
of which provides unique services.  This document focuses on the integration
with Folsom, the latest OpenStack version.

- Nova: An OpenStack project that focuses on providing the compute service.
It manages the lifecycle of VMs.  It also manages the interface devices
(vNICs) of the VMs.  Nova relies on Quantum service to manage the networking
connectivity of the VMs.

- Quantum: An OpenStack project that focuses on providing the networking
connectivity service.  Even though it is designed to handle both L2 and
L3 services for the VMs, we focus mainly on L2 in this document.  

## Quantum Network

Through Quantum service, a virtual network is created.  And on this network, 
virtual ports can be created.  All VMs attached to the ports on this network 
have L2 connectivity.  In the integration of MidoNet with Quantum, each
Quantum network is represented by a MidoNet bridge, and each Quantum network
port is a virtual external bridge port in MidoNet.  The purpose of this
document is to explain the process of attaching a VM's vNIC to a port, and
what Midolman must do to provide network connetivity to this VM.

## Nova VIF Driver

When a request to launch a VM is sent to Nova, the network(s) to connect the
VM to is specified, and the Nova API server sends a request to one of
the Nova compute hosts to carry out the order.  Nova also creates a virtual
port in a MidoNet bridge that corresponds to the Quantum network that the VM
is requested to be plugged into.  Nova compute service then continues on to 
launch the VM, but right before the VM is launched, Nova provides a hook in
the code path that gives vendors a place to insert their plugin code to 
configure the vNICs on the host.  This plugin code is called a VIF driver, and 
it is in this driver that Nova must coordinate with MidoNet to effectively plug 
the VM into the MidoNet virtual bridge port.

The first thing the VIF driver must do is to create a tap interface on the
host, which represents the vNIC on the VM.  While the creation of the tap
interface is a responsibility of Nova, the creation of a datapath port to
effectively plug the tap into on the host is a responsibility of the Midolman
agent.  In order for the VIF drive to request the Midolman agent to create the
datapath port, it must invoke a REST API call to the Midolman API server.


## Midolman API

The URI for associating a virtual port to a host's interface is retrieved from
making the following GET calls:

GET / 
=> {"hosts": <hostsUri>, ...}

GET <hostsUri>
=> [{"hostId": <hostId>, "map_interface_to_port": <mapInterfaceToPortUri>, ...},
    ...]

Where the first GET call retrieves the URI to get all the hosts in the system
(<hostsUri>) and the second GET call retrieves the URI to map an interface to a
virtual port (<mapInterfaceToPortUri>) for each host in the system.  Once you
have this URI, you can POST to create the mapping:

POST <mapInterfaceToPortUri>
=> {"portId": <portId>, "interfaceName": <interfaceName>}

Where <interfaceName> is the name of the tap that Nova created, <portId> is the
virtual port ID of Midolman.  A successful call to this API alerts Midolman to
set up the datapath port on the host. 

## Zookeeper

'map_interface_and_port' API creates the following entries in Zookeeper:

- /hosts/<hostId>/vrnMappings/ports/<portId> -> { <portId>, <interfaceName> }

This mapping indicates the association of a virtual port to an interface of a
host.  Midolman agents watch the ZK changes of the host that it is running on.
When it is notified of the mapping, it begins the operations to set up the
datapath port on the host that corresponds to the mapped virtual port.
 
- /ports/<portId> -> { ..., <hostId> } 

This entry indicates to Midolman the location of the virtual port.
