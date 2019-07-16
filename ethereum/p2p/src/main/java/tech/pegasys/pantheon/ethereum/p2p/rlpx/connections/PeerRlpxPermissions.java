/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.pantheon.ethereum.p2p.rlpx.connections;

import tech.pegasys.pantheon.ethereum.p2p.peers.LocalNode;
import tech.pegasys.pantheon.ethereum.p2p.peers.Peer;
import tech.pegasys.pantheon.ethereum.p2p.permissions.PeerPermissions;
import tech.pegasys.pantheon.ethereum.p2p.permissions.PeerPermissions.Action;
import tech.pegasys.pantheon.ethereum.p2p.permissions.PeerPermissionsException;
import tech.pegasys.pantheon.ethereum.p2p.permissions.PermissionsUpdateCallback;

public class PeerRlpxPermissions {
  private final LocalNode localNode;
  private final PeerPermissions peerPermissions;

  public PeerRlpxPermissions(final LocalNode localNode, final PeerPermissions peerPermissions) {
    this.localNode = localNode;
    this.peerPermissions = peerPermissions;
  }

  public boolean allowNewOutboundConnectionTo(final Peer peer) {
    if (!localNode.isReady()) {
      return false;
    }
    return peerPermissions.isPermitted(
        localNode.getPeer(), peer, Action.RLPX_ALLOW_NEW_OUTBOUND_CONNECTION);
  }

  public PeerPermissionsException newOutboundConnectionException(final Peer peer) {
    return new PeerPermissionsException(peer, Action.RLPX_ALLOW_NEW_OUTBOUND_CONNECTION);
  }

  public boolean allowNewInboundConnectionFrom(final Peer peer) {
    if (!localNode.isReady()) {
      return false;
    }
    return peerPermissions.isPermitted(
        localNode.getPeer(), peer, Action.RLPX_ALLOW_NEW_INBOUND_CONNECTION);
  }

  public boolean allowOngoingConnection(final Peer peer, final boolean remotelyInitiated) {
    if (!localNode.isReady()) {
      return false;
    }
    final Action action =
        remotelyInitiated
            ? Action.RLPX_ALLOW_ONGOING_REMOTELY_INITIATED_CONNECTION
            : Action.RLPX_ALLOW_ONGOING_LOCALLY_INITIATED_CONNECTION;
    return peerPermissions.isPermitted(localNode.getPeer(), peer, action);
  }

  public void subscribeUpdate(final PermissionsUpdateCallback callback) {
    peerPermissions.subscribeUpdate(callback);
  }
}
