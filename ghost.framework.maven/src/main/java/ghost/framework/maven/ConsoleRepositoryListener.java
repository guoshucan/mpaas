/*******************************************************************************
 * Copyright (c) 2010, 2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package ghost.framework.maven;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;

/**
 * A simplistic repository listener that logs events to the console.
 */
public class ConsoleRepositoryListener
    extends AbstractRepositoryListener {
    private final Log log = LogFactory.getLog(ConsoleRepositoryListener.class);

    public void artifactDeployed(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Deployed " + event.getArtifact() + " to " + event.getRepository());
        }
    }

    public void artifactDeploying(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Deploying " + event.getArtifact() + " to " + event.getRepository());
        }
    }

    public void artifactDescriptorInvalid(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Invalid artifact descriptor for " + event.getArtifact() + ": "
                    + event.getException().getMessage());
        }
    }

    public void artifactDescriptorMissing(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Missing artifact descriptor for " + event.getArtifact());
        }
    }

    public void artifactInstalled(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Installed " + event.getArtifact() + " to " + event.getFile());
        }
    }

    public void artifactInstalling(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Installing " + event.getArtifact() + " to " + event.getFile());
        }
    }

    public void artifactResolved(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Resolved artifact " + event.getArtifact() + " from " + event.getRepository());
        }
    }

    public void artifactDownloading(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Downloading artifact " + event.getArtifact() + " from " + event.getRepository());
        }
    }

    public void artifactDownloaded(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Downloaded artifact " + event.getArtifact() + " from " + event.getRepository());
        }
    }

    public void artifactResolving(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Resolving artifact " + event.getArtifact());
        }
    }

    public void metadataDeployed(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Deployed " + event.getMetadata() + " to " + event.getRepository());
        }
    }

    public void metadataDeploying(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Deploying " + event.getMetadata() + " to " + event.getRepository());
        }
    }

    public void metadataInstalled(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Installed " + event.getMetadata() + " to " + event.getFile());
        }
    }

    public void metadataInstalling(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Installing " + event.getMetadata() + " to " + event.getFile());
        }
    }

    public void metadataInvalid(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Invalid metadata " + event.getMetadata());
        }
    }

    public void metadataResolved(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Resolved metadata " + event.getMetadata() + " from " + event.getRepository());
        }
    }

    public void metadataResolving(RepositoryEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("Resolving metadata " + event.getMetadata() + " from " + event.getRepository());
        }
    }
}