package ghost.framework.maven;

import ghost.framework.util.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.internal.*;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.deployment.DeploymentException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.*;
import org.eclipse.aether.metadata.DefaultMetadata;
import org.eclipse.aether.metadata.Metadata;
import org.eclipse.aether.repository.*;
import org.eclipse.aether.resolution.*;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.version.Version;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static javax.accessibility.AccessibleRole.SEPARATOR;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:26 2018/5/31
 */
public final class Booter {
//    public void addArtifactVersion(final MavenPath mavenPath) {
//        checkNotNull(mavenPath);
//        if (mavenPath.isSubordinate() || mavenPath.getCoordinates() == null) {
//            return;
//        }
//        checkState(Objects.equals(groupId, mavenPath.getCoordinates().getGroupId()));
//        checkState(Objects.equals(artifactId, mavenPath.getCoordinates().getArtifactId()));
//        checkState(Objects.equals(baseVersion, mavenPath.getCoordinates().getBaseVersion()));
//
//        log.debug("Discovered {}:{}:{}:{}:{}",
//                mavenPath.getCoordinates().getGroupId(),
//                mavenPath.getCoordinates().getArtifactId(),
//                mavenPath.getCoordinates().getVersion(),
//                mavenPath.getCoordinates().getClassifier(),
//                mavenPath.getCoordinates().getExtension());
//
//        addBaseVersion(mavenPath.getCoordinates().getBaseVersion());
//
//        if (!mavenPath.getCoordinates().isSnapshot()) {
//            return;
//        }
//        if (Objects.equals(mavenPath.getCoordinates().getBaseVersion(), mavenPath.getCoordinates().getVersion())) {
//            log.warn("Non-timestamped snapshot, ignoring it: {}", mavenPath);
//            return;
//        }
//
//        final Version version = parseVersion(mavenPath.getCoordinates().getVersion());
//        if (version == null) {
//            return; // could not parse, omit it from "latest" maintenance
//        }
//        final VersionCoordinates versionCoordinates = new VersionCoordinates(version, mavenPath.getCoordinates());
//
//        // maintain latestVersionCoordinates
//        if (latestVersionCoordinates == null || latestVersionCoordinates.version.compareTo(version) < 0) {
//            latestVersionCoordinates = versionCoordinates;
//        }
//
//        // maintain latestVersionCoordinatesMap
//        final String key = key(mavenPath.getCoordinates());
//        final VersionCoordinates other = latestVersionCoordinatesMap.get(key);
//        // add if contained version is less than version
//        if (other == null || other.version.compareTo(versionCoordinates.version) < 0) {
//            latestVersionCoordinatesMap.put(key, versionCoordinates);
//        }
//    }

//    private boolean verifyVersionRange(ArtifactDescriptor extension,
//                                       List<ArtifactDescriptor> dependencies) {
//        Optional<ArtifactDescriptor> cliDependency = dependencies.stream().filter(
//                d -> d.group().equals(Constants.GROUP) && d.artifact().equals(Constants.ARTIFACT))
//                .findFirst();
//
//        if (cliDependency.isPresent()) {
//            try {
//                VersionScheme scheme = new GenericVersionScheme();
//                VersionConstraint constraint = scheme
//                        .parseVersionConstraint(cliDependency.get().version());
//                Version cliVersion = scheme.parseVersion(VersionUtils.readVersion().get());
//                if (constraint.containsVersion(cliVersion)) {
//                    return true;
//                }
//                else {
//                    if (cliVersion.toString().endsWith("-SNAPSHOT")) {
//                        Version cliSnapshotVersion = scheme.parseVersion(
//                                StringUtils.removeEnd(cliVersion.toString(), "-SNAPSHOT"));
//                        if (constraint.containsVersion(cliSnapshotVersion)) {
//                            log.warn(
//                                    "Assuming this CLI SNAPSHOT is compatible with the extension...",
//                                    extension.group(), extension.artifact(), extension.version(),
//                                    constraint.toString(), Constants.GROUP, Constants.ARTIFACT,
//                                    cliVersion);
//                            return true;
//                        }
//                    }
//                    log.warn("Extension %s:%s (%s) requires %s of %s:%s, but current version is %s",
//                            extension.group(), extension.artifact(), extension.version(),
//                            constraint.toString(), Constants.GROUP, Constants.ARTIFACT, cliVersion);
//                    return false;
//
//                }
//            }
//            catch (InvalidVersionSpecificationException e) {
//                // This will be captured earlier when resolving the dependencies.
//            }
//        }
//        return true;
//    }
//    List<String> requestVersionRange(Artifact artifact) throws VersionRangeResolutionException {
//        VersionRangeRequest rangeRequest = new VersionRangeRequest(artifact, remoteRepositories, null);
//        VersionRangeResult result = repositorySystem.resolveVersionRange(repositorySystemSession, rangeRequest);
//        return result.getVersions().stream().map(Version::toString).collect(toList());
//    }
//    public List<Version> getVersions() throws VersionRangeResolutionException {
//        Artifact artifact = dependency.getArtifact();
//        VersionRangeRequest request = new VersionRangeRequest(artifact,
//                remoteRepos, null);
//        VersionRangeResult resolveVersionRange = repoSystem
//                .resolveVersionRange(repoSession, request);
//        List<Version> versions = resolveVersionRange.getVersions();
//        return versions;
//
//    }

//    public List<Version> getVersions(Artifact artifact) throws VersionRangeResolutionException {
//        VersionRangeRequest rangeRequest = new VersionRangeRequest();
//        rangeRequest.setArtifact( artifact );
//        rangeRequest.setRepositories( repositories );
//
//        VersionRangeResult rangeResult = system.resolveVersionRange( session, rangeRequest );
//
//        List<Version> versions = new ArrayList<Version>(rangeResult.getVersions());
//        Collections.sort(versions);
//
//        return versions;
//    }
//    private static String determineNewestVersion(RepositorySystem repoSystem,
//                                                 RepositorySystemSession repoSession, List<RemoteRepository>[] repos)
//            throws MojoExecutionException {
//        String version;
//        VersionRangeRequest rangeRequest = new VersionRangeRequest();
//        rangeRequest.setArtifact(new DefaultArtifact(SDK_GROUP_ID + ":" + SDK_ARTIFACT_ID + ":[0,)"));
//        for (List<RemoteRepository> repoList : repos) {
//            for (RemoteRepository repo : repoList) {
//                rangeRequest.addRepository(repo);
//            }
//        }
//
//        VersionRangeResult rangeResult;
//        try {
//            rangeResult = repoSystem.resolveVersionRange(repoSession, rangeRequest);
//        } catch (VersionRangeResolutionException e) {
//            throw new MojoExecutionException(
//                    "Could not resolve latest version of the App Engine Java SDK", e);
//        }
//
//        List<Version> versions = rangeResult.getVersions();
//
//        Collections.sort(versions);
//
//        Version newest = Iterables.getLast(versions);
//
//        version = newest.toString();
//        return version;
//    }
//    private static String determineNewestVersion(RepositorySystem repoSystem, RepositorySystemSession repoSession,
//                                                 List<RemoteRepository>[] repos) throws MojoExecutionException {
//        String version;
//        VersionRangeRequest rangeRequest = new VersionRangeRequest();
//        rangeRequest.setArtifact(new DefaultArtifact(SDK_GROUP_ID + ":" + SDK_ARTIFACT_ID + ":[0,)"));
//        for (List<RemoteRepository> repoList : repos) {
//            for (RemoteRepository repo : repoList) {
//                rangeRequest.addRepository(repo);
//            }
//        }
//
//        VersionRangeResult rangeResult;
//        try {
//            rangeResult = repoSystem.resolveVersionRange(repoSession, rangeRequest);
//        } catch (VersionRangeResolutionException e) {
//            throw new MojoExecutionException("Could not resolve latest version of the App Engine Java SDK", e);
//        }
//
//        List<Version> versions = rangeResult.getVersions();
//
//        Collections.sort(versions);
//
//        Version newest = Iterables.getLast(versions);
//
//        version = newest.toString();
//        return version;
//    }
//    /**
//     * Tries to find the newest version of the artifact that matches given regular expression.
//     * The found version will be older than the {@code upToVersion} or newest available if {@code upToVersion} is null.
//     *
//     * @param gav the coordinates of the artifact. The version part is ignored
//     * @param upToVersion the version up to which the versions will be matched
//     * @param versionMatcher the matcher to match the version
//     * @param remoteOnly true if only remotely available artifacts should be considered
//     * @param upToInclusive whether the {@code upToVersion} should be considered inclusive or exclusive
//     * @return the resolved artifact
//     * @throws VersionRangeResolutionException
//     */
//    public Artifact resolveNewestMatching(String gav, @Nullable String upToVersion, Pattern versionMatcher,
//                                          boolean remoteOnly, boolean upToInclusive)
//            throws VersionRangeResolutionException, ArtifactResolutionException {
//
//
//        Artifact artifact = new DefaultArtifact(gav);
//        artifact = artifact.setVersion(upToVersion == null ? "[,)" : "[," + upToVersion + (upToInclusive ? "]" : ")"));
//        VersionRangeRequest rangeRequest = new VersionRangeRequest(artifact, repositories, null);
//
//        RepositorySystemSession session = remoteOnly ? makeRemoteOnly(this.session) : this.session;
//
//        VersionRangeResult result = repositorySystem.resolveVersionRange(session, rangeRequest);
//
//        List<Version> versions = new ArrayList<>(result.getVersions());
//        Collections.reverse(versions);
//
//        for(Version v : versions) {
//            if (versionMatcher.matcher(v.toString()).matches()) {
//                return resolveArtifact(artifact.setVersion(v.toString()), session);
//            }
//        }
//
//        throw new VersionRangeResolutionException(result) {
//            @Override
//            public String getMessage() {
//                return "Failed to find a version of artifact '" + gav + "' that would correspond to an expression '"
//                        + versionMatcher + "'. The versions found were: " + versions;
//            }
//        };
//    }
//    public List<String> getAvailableVersions(String artifactGroup, String artifactName, List<PluginRepository> remoteRepositories) {
//        List<String> versions = new ArrayList<>();
//        try {
//            // TODO figure out how to force remote check
//            VersionRangeRequest rangeRequest = new VersionRangeRequest();
//            rangeRequest.setArtifact(new DefaultArtifact(artifactGroup, artifactName, "jar", "[0,)"));
//            if (remoteRepositories != null) {
//                for (PluginRepository pluginRepository : remoteRepositories) {
//                    rangeRequest.addRepository(
//                            new RemoteRepository.Builder(pluginRepository.getName(), "default", pluginRepository.getUrl()).build());
//                }
//            }
//            DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
//            File tempDir = new File(System.getProperty("java.io.tmpdir"), "temp-local-repo");
//            tempDir.mkdirs();
//            LocalRepository localRepo = new LocalRepository(tempDir.getAbsolutePath());
//            session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(session, localRepo));
//            session.setTransferListener(new PluginTransferListener());
//            session.setRepositoryListener(new PluginRepositoryListener());
//            VersionRangeResult rangeResult = repositorySystem.resolveVersionRange(session, rangeRequest);
//            if (rangeResult != null) {
//                List<Version> versionList = rangeResult.getVersions();
//                for (Version version : versionList) {
//                    versions.add(version.toString());
//                }
//            }
//            FileUtils.deleteQuietly(tempDir);
//        } catch (VersionRangeResolutionException e) {
//            logger.error("", e);
//        }
//        return versions;
//    }
//    public boolean isNewer(Plugin first, Plugin second) {
//        try {
//            GenericVersionScheme versionScheme = new GenericVersionScheme();
//            Version firstVersion = versionScheme.parseVersion(first.getArtifactVersion());
//            Version secondVersion = versionScheme.parseVersion(second.getArtifactVersion());
//            return firstVersion.compareTo(secondVersion) > 0;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//    public ResolvedProject detectAllVersionsOf(ProjectCoordinates project) throws RepositoryException {
//        Artifact artifact = new DefaultArtifact(project.groupId(), project.artifactId(), "jar", "[0,)");
//        Stream<String> versions = repositorySystem
//                .resolveVersionRange(
//                        repositorySystemSession,
//                        new VersionRangeRequest(artifact, singletonList(mavenCentral), NULL_CONTEXT))
//                .getVersions().stream()
//                .map(Version::toString);
//        return new ResolvedProject(project, project.toArtifactsWithVersions(versions));
//    }

//    private Version parseVersion(final String version) {
//        try {
//            return versionScheme.parseVersion(version);
//        }
//        catch (InvalidVersionSpecificationException e) {
//            log.warn("Invalid version: {}", version, e);
//            return null;
//        }
//    }
//    public Maven2Metadata onExitArtifactId() {
//        checkState(artifactId != null);
//        log.debug("<- GA: {}:{}", groupId, artifactId);
//        if (baseVersions.isEmpty()) {
//            log.debug("Nothing to generate: {}:{}", groupId, artifactId);
//            return null;
//        }
//        Iterator<Version> vi = baseVersions.descendingIterator();
//        String latest = vi.next().toString();
//        String release = latest;
//        while (release.endsWith(Constants.SNAPSHOT_VERSION_SUFFIX) && vi.hasNext()) {
//            release = vi.next().toString();
//        }
//        if (release.endsWith(Constants.SNAPSHOT_VERSION_SUFFIX)) {
//            release = null;
//        }
//        return Maven2Metadata.newArtifactLevel(
//                DateTime.now(),
//                groupId,
//                artifactId,
//                latest,
//                release,
//                Iterables.transform(baseVersions, new Function<Version, String>()
//                {
//                    @Override
//                    public String apply(final Version input) {
//                        return input.toString();
//                    }
//                }));
//    }

//    protected void doWithRepositorySession(RepositorySystem system, RepositorySystemSession session,
//                                           ArtifactSource source, Manifest manifest, Artifact zip, Artifact pom, Artifact metadata,
//                                           CommandLine commandLine) {
//        Set<String> ids = ghost.framework.util.StringUtils
//                .commaDelimitedListToSet(CommandLineOptions.getOptionValue("id").orElse(""));
//
//        if (CommandLineOptions.hasOption("U")) {
//            String group = manifest.group();
//            String artifact = manifest.artifact();
//            Version version = VersionUtils.parseVersion(manifest.version());
//
//            SearchCommand search = new SearchCommand();
//            Map<String, List<Operation>> operations = search.search(SettingsReader.read(), null,
//                    new Properties(), null);
//
//            operations.values().stream().filter(ops -> ops.size() > 0).forEach(ops -> {
//                Archive archive = ops.get(0).archive();
//                if (archive.group().equals(group) && archive.artifact().equals(artifact)
//                        && !"global".equals(archive.scope())) {
//                    if (version
//                            .compareTo(VersionUtils.parseVersion(archive.version().value())) > 0) {
//                        ids.add(archive.scope());
//                    }
//                }
//            });
//        }
//
//        List<org.eclipse.aether.repository.RemoteRepository> deployRepositorys = getDeployRepositories(
//                ids);
//        deployRepositorys.forEach(
//                r -> publishToRepository(system, session, source, manifest, zip, pom, metadata, r));
//    }
//    public static Version parseVersion(String version) {
//        try {
//            return VERSION_SCHEME.parseVersion(version);
//        }
//        catch (InvalidVersionSpecificationException e) {
//            throw new CommandException(
//                    String.format("Unable to parse version number '%s'", version), e);
//        }
//    }
//    public static Optional<String> newerVersion() {
//        try {
//            VersionScheme scheme = new GenericVersionScheme();
//            Version runningVersion = scheme.parseVersion(readVersion().orElse("0.0.0"));
//            Version onlineVersion = scheme.parseVersion(readOnlineVersion().orElse("0.0.0"));
//            return (onlineVersion.compareTo(runningVersion) > 0
//                    ? Optional.of(onlineVersion.toString())
//                    : Optional.empty());
//        }
//        catch (InvalidVersionSpecificationException e) {
//        }
//        return Optional.empty();
//    }

    //    public String latestRelease(Artifact artifact) throws VersionRangeResolutionException {
//        List<Version> versions;
//        Version version;
//
//        versions = availableVersions(artifact.setVersion("[" + artifact.getVersion() + ",]"));
//
//        // ranges also return SNAPSHOTS. The release/compatibility notes say they don't, but the respective bug
//        // was re-opened: http://jira.codehaus.org/browse/MNG-3092
//        for (int i = versions.size() - 1; i >= 0; i--) {
//            version = versions.get(i);
//            if (!version.toString().endsWith("SNAPSHOT")) {
//                return version.toString();
//            }
//        }
//        return artifact.getVersion();
//    }
//    private Version highestVersion(Artifact artifact) throws MojoExecutionException {
//        VersionRangeRequest request = new VersionRangeRequest(artifact, repositories, null);
//        VersionRangeResult v = resolve(request);
//
//        if (!includeSnapshots) {
//            List<Version> filtered = new ArrayList<Version>();
//            for (Version aVersion : v.getVersions()) {
//                if (!aVersion.toString().endsWith("SNAPSHOT")) {
//                    filtered.add(aVersion);
//                }
//            }
//            v.setVersions(filtered);
//        }
//
//        if (v.getHighestVersion() == null) {
//            throw (v.getExceptions().isEmpty())
//                    ? new MojoExecutionException("Failed to resolve " + artifact.toString())
//                    : new MojoExecutionException("Failed to resolve " + artifact.toString(), v.getExceptions().get(0));
//        }
//
//        return v.getHighestVersion();
//    }
//    private  Artifact resolveLatestVersionRange(Dependency dependency, String version) throws MojoExecutionException {
//        Matcher versionMatch = matchVersion(version);
//        Artifact artifact = new DefaultArtifact(dependency.getGroupId(), dependency.getArtifactId(),
//                dependency.getType(), dependency.getClassifier(), version);
//
//        if (versionMatch.matches()) {
//
//            Version highestVersion = highestVersion(artifact);
//            String upperVersion = versionMatch.group(1) != null
//                    ? versionMatch.group(1)
//                    : "";
//            String newVersion = "[" + highestVersion.toString() + "," + upperVersion + ")";
//
//            artifact = artifact.setVersion(newVersion);
//            return artifact;
//        }
//        else {
//            return artifact;
//        }
//    }
//    private static String determineNewestVersion(RepositorySystem repoSystem, RepositorySystemSession repoSession, List<RemoteRepository>[] repos) throws MojoExecutionException {
//        String version;VersionRangeRequest rangeRequest = new VersionRangeRequest();
//        rangeRequest.setArtifact(new DefaultArtifact(SDK_GROUP_ID + ":" + SDK_ARTIFACT_ID + ":[0,)"));
//        for(List<RemoteRepository> repoList : repos) {
//            for(RemoteRepository repo : repoList) {
//                rangeRequest.addRepository(repo);
//            }
//        }
//
//        VersionRangeResult rangeResult;
//        try {
//            rangeResult = repoSystem.resolveVersionRange(repoSession, rangeRequest);
//        } catch (VersionRangeResolutionException e) {
//            throw new MojoExecutionException("Could not resolve latest version of the App Engine Java SDK", e);
//        }
//
//        List<Version> versions = rangeResult.getVersions();
//
//        Collections.sort(versions);
//
//        Version newest = Iterables.getLast(versions);
//
//        version = newest.toString();
//        return version;
//    }
//    public static void validateRugCompatibility(ArtifactDescriptor artifact,
//                                                List<ArtifactDescriptor> dependencies) {
//        Optional<ArtifactDescriptor> rugArtifact = dependencies.stream()
//                .filter(f -> f.group().equals(Constants.GROUP)
//                        && f.artifact().equals(Constants.RUG_ARTIFACT))
//                .findAny();
//        if (rugArtifact.isPresent()) {
//            try {
//                Version version = VERSION_SCHEME.parseVersion(rugArtifact.get().version());
//                VersionRange range = VERSION_SCHEME.parseVersionRange(Constants.RUG_VERSION_RANGE);
//                if (!range.containsVersion(version)) {
//                    throw new VersionException(String.format(
//                            "Installed version of Rug CLI is not compatible with archive %s.\n\n"
//                                    + "The archive depends on %s:%s (%s) which is incompatible with Rug CLI (compatible version range %s).\n"
//                                    + "Please update to a more recent version of Rug CLI or change the Rug archive to use a supported Rug version.",
//                            ArtifactDescriptorUtils.coordinates(artifact), Constants.GROUP,
//                            Constants.RUG_ARTIFACT, version.toString(), range.toString()));
//                }
//            }
//            catch (InvalidVersionSpecificationException e) {
//                // Since we were able to resolve the version it is impossible for this to happen
//            }
//        }
//    }
//    public RemoteSnapshotMetadataGenerator( RepositorySystemSession session, DeployRequest request )
//    {
//        legacyFormat = ConfigUtils.getBoolean( session.getConfigProperties(), false, "maven.metadata.legacy" );
//
//        snapshots = new LinkedHashMap<Object, RemoteSnapshotMetadata>();
//
//        /*
//         * NOTE: This should be considered a quirk to support interop with Maven's legacy ArtifactDeployer which
//         * processes one artifact at a time and hence cannot associate the artifacts from the same project to use the
//         * same timestamp+buildno for the snapshot versions. Allowing the caller to pass in metadata from a previous
//         * deployment allows to re-establish the association between the artifacts of the same project.
//         */
//        for ( Metadata metadata : request.getMetadata() )
//        {
//            if ( metadata instanceof RemoteSnapshotMetadata )
//            {
//                RemoteSnapshotMetadata snapshotMetadata = (RemoteSnapshotMetadata) metadata;
//                snapshots.put( snapshotMetadata.getKey(), snapshotMetadata );
//            }
//        }
//    }
//    public MetadataGenerator newInstance(RepositorySystemSession session, DeployRequest request )
//    {
//        return new RemoteSnapshotMetadataGenerator( session, request );
//    }
//    public MetadataGenerator newInstance( RepositorySystemSession session, DeployRequest request )
//    {
//        return new VersionsMetadataGenerator( session, request );
//    }
//    /**
//     * Uploads a collection of artifacts and their accompanying metadata to a remote repository.
//     *
//     * @param request The deployment request, must not be {@code null}.
//     * @return The deployment result, never {@code null}.
//     * @throws DeploymentException If any artifact/metadata from the request could not be deployed.
//     */
//    public DeployResult deploy(DeployRequest request) throws DeploymentException {
//        return repository.deploy(session, request);
//    }
    protected void publishArtifacts(Collection<Artifact> artifacts, RepositorySystem repositorySystem, RepositorySystemSession session, RemoteRepository remoteRepository) throws DeploymentException {
        DeployRequest request = new DeployRequest();
        request.setRepository(remoteRepository);
        for (Artifact artifact : artifacts) {
            request.addArtifact(artifact);
        }
        log.info("Deploying to {}"+ remoteRepository.getUrl());
        repositorySystem.deploy(session, request);
    }

    /**
     * 构建MavenProject
     *
     * @param pomFile
     * @return
     * @throws Exception
     */
    public static MavenProject loadProject(File pomFile) throws Exception {
        MavenProject ret = null;
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();

        if (pomFile != null && pomFile.exists()) {
            FileReader reader = null;

            try {
                reader = new FileReader(pomFile);
                Model model = mavenReader.read(reader);
                model.setPomFile(pomFile);

                ret = new MavenProject(model);
            } finally {
                // Close reader
            }
        }
        return ret;
    }

    public static List<Dependency> getArtifactsDependencies(MavenProject project, String dependencyType, String scope) throws Exception {
        DefaultArtifact pomArtifact = new DefaultArtifact(project.getId());

        RepositorySystemSession repoSession = null; // TODO
        RepositorySystem repoSystem = null; // TODO

        List<RemoteRepository> remoteRepos = project.getRemoteProjectRepositories();
        List<Dependency> ret = new ArrayList<Dependency>();

        Dependency dependency = new Dependency(pomArtifact, scope);

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.setRepositories(remoteRepos);

        DependencyNode node = repoSystem.collectDependencies(repoSession, collectRequest).getRoot();
        DependencyRequest projectDependencyRequest = new DependencyRequest(node, null);

        repoSystem.resolveDependencies(repoSession, projectDependencyRequest);

        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept(nlg);

        ret.addAll(nlg.getDependencies(true));

        return ret;
    }

    //    public DependencyNode resolveDependencyTree(final CollectRequestBuilder requestBuilder, final RepositorySystemSession session, final RemoteRepository... repositories) throws DependencyCollectionException {
//        final CollectRequest request = requestBuilder.getCollectRequest();
//        final List<RemoteRepository> allRepositories = new ArrayList<RemoteRepository>();
//        if (repositories != null && repositories.length > 0) {
//            allRepositories.addAll(asList(repositories));
//        }
//        allRepositories.addAll(request.getRepositories());
//        request.setRepositories(getRemoteRepositoryManager().aggregateRepositories(session, Collections.<RemoteRepository>emptyList(), allRepositories, RECESSIVE_IS_RAW));
//        RepositorySystemSession safeSession = session;
//        if (session.getLocalRepositoryManager() == null || session.getLocalRepository() == null) {
//            safeSession = new RepositorySystemSessionWrapper(session) {
//                final LocalRepositoryManager lrm = getRepositorySystem().newLocalRepositoryManager(session,
//                        new LocalRepository(new File(Names.MAVEN_USER_HOME, "repository")));
//
//                public LocalRepositoryManager getLocalRepositoryManager() {
//                    return lrm;
//                }
//
//                public LocalRepository getLocalRepository() {
//                    return lrm.getRepository();
//                }
//            };
//        }
//        return super.resolveDependencyTree(requestBuilder, safeSession, repositories);
//    }
//    public static void ssss(File localRepositoryFile){
//        RepositorySystem repoSystem = newRepositorySystem();
//        RepositorySystemSession session = newLocalSession(repoSystem, localRepositoryFile);
//        final File repo = session.getLocalRepository().getBasedir();
//        final Collection<Artifact> deps = new Aether(loadProject(localRepositoryFile), repo).resolve(
//                new DefaultArtifact("junit", "junit-dep", "", "jar", "4.10"),
//                JavaScopes.RUNTIME
//        );
//    }

    /**
     * @param paths
     * @param metadatas
     */
    public static void addMetadataPaths(Collection<String> paths, Collection<? extends Metadata> metadatas) {
        if (metadatas != null) {
            for (Metadata metadata : metadatas) {
                String path = getPath(metadata);
                paths.add(path);
            }
        }
    }

    /**
     * @param metadata
     * @return
     */
    private static String getPath(Metadata metadata) {
        // NOTE: Don't use LRM.getPath*() as those paths could be different across processes,
        // e.g. due to staging.
        StringBuilder path = new StringBuilder(128);
        if (metadata.getGroupId().length() > 0) {
            path.append(metadata.getGroupId());
            if (metadata.getArtifactId().length() > 0) {
                path.append(SEPARATOR).append(metadata.getArtifactId());
                if (metadata.getVersion().length() > 0) {
                    path.append(SEPARATOR).append(metadata.getVersion());
                }
            }
        }
        return path.toString();
    }

    /**
     * @param artifact
     * @return
     */
    public static String getArtifactId(Metadata artifact) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(artifact.getGroupId()).append(":").append(artifact.getArtifactId()).append(":").append(artifact.getVersion()).append(':').append(artifact.getType()).append(':').append(artifact.getNature());
        return sb.toString();
    }

    public static File getLocalFile(DefaultArtifact artifact, File localRepositoryFile) {
        return getLocalFile(artifact, null, localRepositoryFile);
    }

    public static File getLocalFile(DefaultArtifact artifact, MavenRepositoryServer repositoryServer, File localRepositoryFile) {
        if (repositoryServer == null) {
            return getLocalFile(new DefaultMetadata(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                    artifact.getExtension(), Metadata.Nature.SNAPSHOT), null, localRepositoryFile, null);
        }
        return getLocalFile(new DefaultMetadata(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(),
                artifact.getExtension(), Metadata.Nature.SNAPSHOT), getRemoteRepository(repositoryServer), localRepositoryFile, null);
    }

    /**
     * 搜索版本
     * @param dependencies
     * @param groupId
     * @param artifactId
     * @return
     */
    public static String findVersion(List<DependencyNode> dependencies, String groupId, String artifactId) {
        for (DependencyNode child : dependencies) {
            Artifact childArtifact = child.getArtifact();
            if (groupId.equals(childArtifact.getGroupId()) && artifactId.equals(childArtifact.getArtifactId())) {
                return childArtifact.getBaseVersion();
            } else {
                String version = findVersion(child.getChildren(), groupId, artifactId);
                if (version != null) {
                    return version;
                }
            }
        }
        return null;
    }
//    private Artifact createFoundArtifact(final File pomFile) {
//        try {
//            if (log.isLoggable(Level.FINE)) {
//                log.fine("Processing " + pomFile.getAbsolutePath() + " for classpath artifact resolution");
//            }
//            // TODO: load pom using Maven Model?
//            // This might include a cycle in graph reconstruction, to be investigated
//            final Document pom = loadPom(pomFile);
//            String groupId = getXPathGroupIdExpression().evaluate(pom);
//            String artifactId = getXPathArtifactIdExpression().evaluate(pom);
//            String type = getXPathTypeExpression().evaluate(pom);
//            String version = getXPathVersionExpression().evaluate(pom);
//            if (Validate.isNullOrEmpty(groupId)) {
//                groupId = getXPathParentGroupIdExpression().evaluate(pom);
//            }
//            if (Validate.isNullOrEmpty(type)) {
//                type = "jar";
//            }
//            if (version == null || version.equals("")) {
//                version = getXPathParentVersionExpression().evaluate(pom);
//            }
//            final Artifact foundArtifact = new DefaultArtifact(groupId + ":" + artifactId + ":" + type + ":" + version);
//            foundArtifact.setFile(pomFile);
//            return foundArtifact;
//        } catch (final Exception e) {
//            throw new RuntimeException("Could not parse pom.xml: " + pomFile, e);
//        }
//    }
//    private RemoteRepository selectRepositoryToDeploy(Artifact artifactToDeploy) {
//        if (artifactToDeploy == null) {
//            throw new IllegalArgumentException("artifactToDeploy should not be null");
//        }
//        RemoteRepository.Builder snapRepoBuilder =  new RemoteRepository.Builder("paas.push.snapshot.repo", "default", mvnConsumerConfigurer.getPushSnapshotRepositoryUrl());
//        RepositoryPolicy disabledRepo = null;
//        snapRepoBuilder.setReleasePolicy(disabledRepo);
//        Authentication snapshotRepositoryAuthen = new AuthenticationBuilder().addUsername(mvnConsumerConfigurer.getPushSnapshotRepositoryUser()).addPassword(
//                mvnConsumerConfigurer.getPushSnapshotRepositoryPassword()).build();
//        snapRepoBuilder.setAuthentication(snapshotRepositoryAuthen);
//        RemoteRepository.Builder releaseRepoBuilder = new RemoteRepository.Builder("paas.push.release.repo", "default", mvnConsumerConfigurer.getPushReleaseRepositoryUrl());
//        releaseRepoBuilder.setReleasePolicy(disabledRepo);
//        Authentication releaseRepositoryAuthen = new AuthenticationBuilder().addUsername(mvnConsumerConfigurer.getPushReleaseRepositoryUser()).addPassword(
//                mvnConsumerConfigurer.getPushReleaseRepositoryPassword()).build();
//        releaseRepoBuilder.setAuthentication(releaseRepositoryAuthen);
//        RemoteRepository result;
//        if (artifactToDeploy.isSnapshot()) {
//            result = snapRepoBuilder.build();
//        } else {
//            result = releaseRepoBuilder.build();
//        }
//        return result;
//    }
    /**
     * 获取本地仓库包文件
     *
     * @param metadata
     * @param remoteRepository
     * @param localRepositoryFile
     * @param context
     * @return
     */
    public static File getLocalFile(DefaultMetadata metadata, RemoteRepository remoteRepository, File localRepositoryFile, String context) {
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newLocalSession(repoSystem, localRepositoryFile);
        return getLocalFile(session, metadata, remoteRepository, context);
    }

    /**
     * 创建本地仓库会话
     *
     * @param system
     * @param localRepositoryFile
     * @return
     */
    private static RepositorySystemSession newLocalSession(RepositorySystem system, File localRepositoryFile) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        //本地仓库
        session.setConfigProperty(ConflictResolver.CONFIG_PROP_VERBOSE, true);
        session.setConfigProperty(DependencyManagerUtils.CONFIG_PROP_VERBOSE, true);
        LocalRepository localRepo = new LocalRepository(localRepositoryFile);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
//        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
//        session.setTransferListener(new ConsoleTransferListener(metadata));
        session.setRepositoryListener(new ConsoleRepositoryListener());
        return session;
    }

    /**
     * 获取本地仓库包
     *
     * @param session          仓库会话
     * @param metadata         包元数据
     * @param remoteRepository 远程仓库地址，默认为null
     * @param context
     * @return
     */
    public static File getLocalFile(RepositorySystemSession session, DefaultMetadata metadata, RemoteRepository remoteRepository, String context) {
        LocalRepositoryManager lrm = session.getLocalRepositoryManager();
        LocalMetadataResult localResult = lrm.find(session, new LocalMetadataRequest(metadata, remoteRepository, context));
        return localResult.getFile();
    }

    public static File getLocalFile(DefaultArtifact artifact, File localRepositoryFile, List<RemoteRepository> remoteRepositoryList, String context) {
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newLocalSession(repoSystem, localRepositoryFile);
        return getLocalFile(session, artifact, remoteRepositoryList, context);
    }

    public static File getLocalFile(RepositorySystemSession session, DefaultArtifact artifact, List<RemoteRepository> remoteRepositoryList, String context) {
        LocalRepositoryManager lrm = session.getLocalRepositoryManager();
        LocalArtifactResult localResult = lrm.find(session, new LocalArtifactRequest(artifact, remoteRepositoryList, context));
        return localResult.getFile();
    }

    /**
     * 从本地maven仓库获取包文件
     *
     * @param localRepositoryFile 本地仓库目录
     * @param artifact            jar包信息
     */
    public static File getJarLocalFile(File localRepositoryFile, Artifact artifact) {
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newLocalSession(repoSystem, localRepositoryFile);
        LocalRepositoryManager manager = session.getLocalRepositoryManager();
        return new File(manager.getRepository().getBasedir(), manager.getPathForLocalArtifact(artifact) + ".jar");
    }
//    public static RepositorySystemSession getSession(ArtifactRepository localRepository) {
//        return LegacyLocalRepositoryManager.overlay( localRepository, legacySupport.getRepositorySession(), repoSystem );
//    }
//    public static RepositorySystemSession getSession(ArtifactRepository localRepository) {
//        return LegacyLocalRepositoryManager.overlay(
//                localRepository, legacySupport.getRepositorySession(), repoSystem);
//    }
//    public static RepositorySystemSession getSession(ArtifactRepository localRepository) {
//        return LegacyLocalRepositoryManager.overlay( localRepository, legacySupport.getRepositorySession(), repoSystem );
//    }
//    public static void install( File source, Artifact artifact, ArtifactRepository localRepository )
//            throws ArtifactInstallationException
//    {
//        RepositorySystemSession session =
//                LegacyLocalRepositoryManager.overlay( localRepository, legacySupport.getRepositorySession(), repoSystem );
//
//        InstallRequest request = new InstallRequest();
//
//        request.setTrace( DefaultRequestTrace.newChild( null, legacySupport.getSession().getCurrentProject() ) );
//
//        org.sonatype.aether.artifact.Artifact mainArtifact = RepositoryUtils.toArtifact( artifact );
//        mainArtifact = mainArtifact.setFile( source );
//        request.addArtifact( mainArtifact );
//
//        for ( ArtifactMetadata metadata : artifact.getMetadataList() )
//        {
//            if ( metadata instanceof ProjectArtifactMetadata )
//            {
//                org.sonatype.aether.artifact.Artifact pomArtifact = new SubArtifact( mainArtifact, "", "pom" );
//                pomArtifact = pomArtifact.setFile( ( (ProjectArtifactMetadata) metadata ).getFile() );
//                request.addArtifact( pomArtifact );
//            }
//            else if ( metadata instanceof SnapshotArtifactRepositoryMetadata
//                    || metadata instanceof ArtifactRepositoryMetadata )
//            {
//                // eaten, handled by repo system
//            }
//            else
//            {
//                request.addMetadata( new MetadataBridge( metadata ) );
//            }
//        }
//
//        try
//        {
//            repoSystem.install( session, request );
//        }
//        catch ( InstallationException e )
//        {
//            throw new ArtifactInstallationException( e.getMessage(), e );
//        }
//
//        /*
//         * NOTE: Not used by Maven core, only here to provide backward-compat with plugins like the Install Plugin.
//         */
//
//        if ( artifact.isSnapshot() )
//        {
//            Snapshot snapshot = new Snapshot();
//            snapshot.setLocalCopy( true );
//            artifact.addMetadata( new SnapshotArtifactRepositoryMetadata( artifact, snapshot ) );
//        }
//
//        Versioning versioning = new Versioning();
//        versioning.updateTimestamp();
//        versioning.addVersion( artifact.getBaseVersion() );
//        if ( artifact.isRelease() )
//        {
//            versioning.setRelease( artifact.getBaseVersion() );
//        }
//        artifact.addMetadata( new ArtifactRepositoryMetadata( artifact, versioning ) );
//    }
//    public static RepositorySystemSession getSession( ArtifactRepository localRepository )
//    {
//        return LegacyLocalRepositoryManager.overlay( localRepository, legacySupport.getRepositorySession(), repoSystem );
//    }
//    /**
//     * 获取包引用列表
//     *
//     * @param file           maven仓库路径
//     * @param repositoryList 仓库地址列表
//     * @param artifact       包信息
//     * @return
//     * @throws DependencyCollectionException
//     * @throws DependencyResolutionException
//     * @throws ArtifactResolutionException
//     */
//    public static List<URLArtifact> getDependencyNodeDownloadURLArtifactList(File file, List<MavenRepositoryServer> repositoryList, Artifact artifact)
//            throws DependencyCollectionException, DependencyResolutionException, ArtifactResolutionException, IllegalArgumentException, IllegalAccessException {
//        return getDependencyNodeDownloadURLArtifactList(file, repositoryList, artifact, null);
//    }

    /**
     * 获取包引用列表
     *
     * @param file           maven仓库路径
     * @param repositoryList 仓库地址列表
     * @param artifact       包信息
     * @return
     */
//    public static List<Artifact> getDependencyNodeArtifactList(File file, List<MavenRepositoryServer> repositoryList, Artifact artifact) {
//        List<Artifact> artifactList = new ArrayList<>();
//        List<Artifact> list = new ArrayList<>();
//        list.add(artifact);
//        for (DependencyNode node : getDependencyNode(file, getRemoteRepositoryList(repositoryList), list)) {
//            dependencyNodeArtifactList(node, artifactList);
//        }
//        return artifactList;
//    }

    /**
     * 获取指定程序包列表的引用节点
     *
     * @param localRepositoryFile 本地仓库目录
     * @param repositoryList
     * @param artifacts
     * @return
     * @throws DependencyCollectionException
     * @throws DependencyResolutionException
     */
    public static List<DependencyNode> getDependencyNode(File localRepositoryFile, List<RemoteRepository> repositoryList, List<Artifact> artifacts) throws DependencyCollectionException, DependencyResolutionException {
        //初始化仓库列表
        List<RemoteRepository> list = null;
        if (repositoryList == null || repositoryList.size() == 0) {
            list = newRepositories();
        } else {
            list = repositoryList;
        }
        RepositorySystem system = newRepositorySystem();
        List<DependencyNode> nodes = new ArrayList<>();
        //下载顶层包列表
        for (Artifact v : artifacts) {
            Params params = new Params(v);
            params.setSavePath(localRepositoryFile.getPath());
            RepositorySystemSession session = newSession(system, localRepositoryFile, new Params(v));
            nodes.add(getDependencyNode(system, session, list, v));
        }
        return nodes;
    }

    public static DependencyNode getDependencyNode(RepositorySystem repoSystem, RepositorySystemSession session, List<RemoteRepository> remoteRepositoryList, Artifact artifact)
            throws DependencyCollectionException {
        return getDependencyNode(repoSystem, session, remoteRepositoryList, artifact, "compile");
    }

    public static DependencyNode getDependencyNode(RepositorySystem repoSystem, RepositorySystemSession session, List<RemoteRepository> remoteRepositoryList, Artifact artifact, String scope)
            throws DependencyCollectionException {
        Dependency dependency = new Dependency(artifact, scope);
        CollectRequest collectRequest = new CollectRequest(dependency, remoteRepositoryList);
        DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot(node);
        try {
            repoSystem.resolveDependencies(session, dependencyRequest);
        } catch (DependencyResolutionException e) {
            log.error(e.getMessage());
        }
        return node;
    }

    /**
     * 转换maven仓库列表
     *
     * @param repositoryList 格式[id=maven-public,type=,user=,password=,url=|id=maven-public,type=,user=,password=,url=]
     * @return
     */
    public static List<RemoteRepository> getRemoteRepositoryList(List<MavenRepositoryServer> repositoryList) {
        List<RemoteRepository> list = new ArrayList<>();
        if (repositoryList == null || repositoryList.size() == 0) {
            return list;
        }
        for (MavenRepositoryServer repository : repositoryList) {
            //格式id=maven-public,type=,user=,password=,url=
            //判断是否使用账号密码登录仓库
            if (StringUtils.isEmpty(repository.getUsername()) && StringUtils.isEmpty(repository.getPassword())) {
                //创建无账号密码仓库地址
                list.add(new RemoteRepository.Builder(
                        (StringUtils.isEmpty(repository.getId()) ? null : repository.getId()),
                        (StringUtils.isEmpty(repository.getType()) ? null : repository.getType()),
                        repository.getUrl()).setSnapshotPolicy(new RepositoryPolicy()).setReleasePolicy(new RepositoryPolicy()).build());
            } else {
                //创建有账号密码仓库地址
                list.add(new RemoteRepository.Builder(
                        (StringUtils.isEmpty(repository.getId()) ? null : repository.getId()),
                        (StringUtils.isEmpty(repository.getType()) ? null : repository.getType()),
                        repository.getUrl()).setSnapshotPolicy(new RepositoryPolicy()).setReleasePolicy(new RepositoryPolicy())
                        .setAuthentication(new AuthenticationBuilder().addUsername(repository.getUsername()).addPassword(repository.getPassword()).build()).build());
            }
        }
        return list;
    }
//    /**
//     * 获取包引用列表
//     *
//     * @param file           maven仓库路径
//     * @param repositoryList 仓库地址列表
//     * @param artifact       包信息
//     * @return
//     * @throws DependencyCollectionException
//     * @throws DependencyResolutionException
//     * @throws ArtifactResolutionException
//     */
//    public static List<URLArtifact> getDependencyNodeDownloadURLArtifactList(File file, List<MavenRepositoryServer> repositoryList, Artifact artifact, ClassLoader loader) throws ArtifactResolutionException, IllegalArgumentException {
//        List<Artifact> artifactList = getDependencyNodeArtifactList(file, repositoryList, artifact);
//        List<URLArtifact> urlArtifacts = new ArrayList<>();
//        //初始化仓库列表
//        List<RemoteRepository> list = null;
//        if (repositoryList != null && repositoryList.size() > 0) {
//            list = getRemoteRepositoryList(repositoryList);
//        }
//        //判断是否有类加载器
//        if (loader == null) {
//            //没有指定类加载器，下载所有依赖包列表
//            for (Artifact a : artifactList) {
//                URLArtifact urlArtifact = new URLArtifact(a);
//                urlArtifacts.add(urlArtifact);
//                Params params = new Params(a);
//                params.setSavePath(file.getPath());
//                download(params, list);
//                urlArtifact.setFile(params.getJarPath());
//            }
//        } else {
////            //获取类加载器加载的包列表
////            final List<URLManifest> list = JarUtil.getManifests(loader);
////            //判断在指定类加载器中判断包是否存在，如果存在侧不下载该包
////            for (Artifact a : artifactList) {
////                for (URLManifest manifest : list) {
////                    if (manifest.getImplTitle().equals(a.getGroupId()) &&
////                            manifest.getImplVendor().equals(a.getArtifactId()) &&
////                            manifest.getImplVersion().equals(a.getVersion())) {
////                        //版本存在
////                    } else {
////                        //不存在
////                        URLArtifact urlArtifact = new URLArtifact(a);
////                        urlArtifacts.add(urlArtifact);
////                        Params params = new Params(a);
////                        params.setSavePath(m2Dir.getPath());
////                        download(params, repositoryList);
////                        urlArtifact.setFile(params.getJarPath());
////                    }
////                }
////            }
//        }
//        return urlArtifacts;
//    }

    /**
     * 然后建立RepositorySystem，这个是用来操作maven仓库的主要接口
     *
     * @return
     */
    public static RepositorySystem newRepositorySystem() {
        //Aether's components implement org.eclipse.aether.spi.locator.Service to ease manual wiring and using the
        //prepopulated DefaultServiceLocator, we only need to register the repository connector and transporter
        //factories.
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(MetadataGeneratorFactory.class, SnapshotMetadataGeneratorFactory.class);
        locator.addService(MetadataGeneratorFactory.class, VersionsMetadataGeneratorFactory.class);
//        locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);

        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        locator.addService(ArtifactDescriptorReader.class, DefaultArtifactDescriptorReader.class);
        locator.addService(VersionRangeResolver.class, DefaultVersionRangeResolver.class);
        locator.addService(VersionResolver.class, DefaultVersionResolver.class);
        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                exception.printStackTrace();
            }
        });
        return locator.getService(RepositorySystem.class);
    }
//    public static DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
//        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
//
//        LocalRepository localRepo = new LocalRepository("target/local-maven");
//        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
//
//        session.setTransferListener(new ConsoleTransferListener());
//        session.setRepositoryListener(new ConsoleRepositoryListener());
//
//        // uncomment to generate dirty trees
//        // session.setDependencyGraphTransformer( null );
//
//        return session;
//    }

    /**
     * maven仓库服务器列表转换maven仓库远程列表。
     *
     * @param serverList maven仓库列表。
     * @return
     */
    public static List<RemoteRepository> newRepositories(List<MavenRepositoryServer> serverList) {
        List<RemoteRepository> list = new ArrayList<>();
        for (MavenRepositoryServer server : serverList) {
            if (server.getUsername() == null && server.getPassword() == null) {
                list.add(new RemoteRepository.Builder(server.getId(), server.getType(), server.getUrl()).build());
            } else {
                list.add(new RemoteRepository.Builder(server.getId(), server.getType(), server.getUrl())
                        .setAuthentication(new AuthenticationBuilder().addUsername(server.getUsername()).addPassword(server.getPassword()).build()).build());
            }
        }
        return list;
    }

    /**
     * 创建默认仓库列表
     * 常用的仓库列表
     *
     * @return
     */
    public static List<RemoteRepository> newRepositories() {
        List<RemoteRepository> list = new ArrayList<>();
        list.add(new RemoteRepository.Builder(null, "default", "http://maven.aliyun.com/nexus/content/groups/public/").build());
        list.add(new RemoteRepository.Builder(null, null, "http://maven.net.cn/content/groups/public/").build());
        list.add(newCentralRepository());
        list.add(new RemoteRepository.Builder(null, null, "http://maven.jahia.org/maven2/").build());
        list.add(new RemoteRepository.Builder(null, null, "https://repo1.maven.org/maven2/").build());
        return list;
    }

    public static RemoteRepository newCentralRepository() {
        return new RemoteRepository.Builder("central", null, "http://central.maven.org/maven2/").build();
    }

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(Booter.class);

    /**
     * 建立RepositorySystem
     *
     * @return RepositorySystem
     */
    /*
    private static RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }
*/

    /**
     * create a repository system session
     *
     * @param system              RepositorySystem
     * @param localRepositoryFile 本地仓库目录
     * @return RepositorySystemSession
     */
    private static RepositorySystemSession newSession(RepositorySystem system, File localRepositoryFile, Params params) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(localRepositoryFile);
        session.setConfigProperty(ConflictResolver.CONFIG_PROP_VERBOSE, true);
        session.setConfigProperty(DependencyManagerUtils.CONFIG_PROP_VERBOSE, true);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        session.setTransferListener(new ConsoleTransferListener(params));
        session.setRepositoryListener(new ConsoleRepositoryListener());
        //判断设置本地仓库路径
        if ((session.getLocalRepositoryManager() == null || session.getLocalRepository() == null) && localRepositoryFile != null) {
            session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, new LocalRepository(localRepositoryFile)));
        }
        return session;
    }

    /**
     * 获取远程仓库列表。
     *
     * @param positorySourceList
     * @return
     */
//    private static List<RemoteRepository> getRemoteRepositoryList(List<IMavenPositorySource> positorySourceList) {
//        List<RemoteRepository> remoteRepositoryList = new ArrayList<>();
//        //先添加默认maven仓库源。
//        if (positorySourceList != null) {
//            for (IMavenPositorySource sourceEntity : positorySourceList) {
//                if (sourceEntity.isDefault()) {
//                    if (sourceEntity.getUsername() == null && sourceEntity.getPassword() == null) {
//                        remoteRepositoryList.add(new RemoteRepository.Builder("central", "default", sourceEntity.getFile()).build());
//                    } else {
//                        remoteRepositoryList.add(new RemoteRepository.Builder("central", "default", sourceEntity.getFile()).setAuthentication(new AuthenticationBuilder().addUsername(sourceEntity.getUsername()).addPassword(sourceEntity.getPassword()).build()).build());
//                    }
//                    break;
//                }
//            }
//            for (IMavenPositorySource sourceEntity : positorySourceList) {
//                if (!sourceEntity.isDefault()) {
//                    if (sourceEntity.getUsername() == null && sourceEntity.getPassword() == null) {
//                        remoteRepositoryList.add(new RemoteRepository.Builder(null, null, sourceEntity.getFile()).build());
//                    } else
//                        remoteRepositoryList.add(new RemoteRepository.Builder(null, null, sourceEntity.getFile()).setAuthentication(new AuthenticationBuilder().addUsername(sourceEntity.getUsername()).addPassword(sourceEntity.getPassword()).build()).build());
//                }
//            }
//        }
//        //没有远程仓库列表的添加默认远程仓库地址。
//        if (remoteRepositoryList.size() == 0) {
//            remoteRepositoryList.addAll(Booter.newRepositories());
//        }
//        return remoteRepositoryList;
//    }

    /**
     * 下载全部引用包
     *
     * @param file                 本地仓库目录
     * @param remoteRepositoryList 格式[id=maven-public,type=,user=,password=,url=|id=maven-public,type=,user=,password=,url=]
     * @param artifacts            格式ghost.framework.web:ghost-framework-spring-module:1.0-SNAPSHOT,ghost.framework.web:ghost-framework-spring-module:1.0-SNAPSHOT
     * @return
     * @throws Exception
     */
    public static HashMap<Integer, FileArtifact> downloadDependencies(File file, List<RemoteRepository> remoteRepositoryList, List<Artifact> artifacts) throws Exception {
        //初始化仓库列表
        List<RemoteRepository> repositoryList = null;
        if (remoteRepositoryList == null || remoteRepositoryList.size() == 0) {
            repositoryList = newRepositories();
        } else {
            repositoryList = remoteRepositoryList;
        }
        //repositoryList.addAll(Booter.newRepositories());
        //下载顶层包列表
        HashMap<Integer, FileArtifact> srcList = new HashMap<>();
        for (Artifact artifact : artifacts) {
            srcList.put(srcList.size(), new FileArtifact(artifact));
        }
        HashMap<Integer, FileArtifact> list = new HashMap<>();
        //遍历下载包引用
        for (FileArtifact artifact : srcList.values()) {
            downloadDependencies(file, repositoryList, list, artifact);
        }
        //下载源包
        download(file, repositoryList, srcList);
        //下载引用包
        download(file, repositoryList, list);
        for (FileArtifact artifact : list.values()) {
            srcList.put(srcList.size(), artifact);
        }
        return srcList;
    }

    /**
     * 循环遍历所有引用包
     *
     * @param m2Dir
     * @param repositoryList
     * @param rootList
     * @param parent
     * @throws Exception
     */
    private static void downloadDependencies(File m2Dir, List<RemoteRepository> repositoryList, HashMap<Integer, FileArtifact> rootList, FileArtifact parent) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug(parent.toString());
        } else {
            log.info(parent.toString());
        }
        Params params = new Params(parent.getArtifact());
        params.setSavePath(m2Dir.getPath());
        //判断是否为多版本引用，解决多版本引用获取依赖列表被中断问题
        if (params.getVersion().startsWith("[")) {
            //处理指定多版本无法正常遍历版本获取问题
            //格式比如[1.2.1, 1.3.0)这种指定多版本格式问题
            String[] strings = StringUtils.split(params.getVersion(), ",");
            //验证是否有多个版本指定
            if (strings.length == 2) {
                //指定开始一个版本
                params.setVersion(strings[0].substring(1));
            }
            //多版本引用
            List<Version> list = getAllVersions(params);
            //判断是否未指定最小版本
            if (params.getVersion().startsWith("[0,")) {
                //未指定版本，获取最大版本包位置
                params.setVersion(list.get(list.size() - 1).toString());
            } else {
                //获取指定版本的第一个开始位置版本
                //该位置是包指定了最小版本号的版本位置
                params.setVersion(list.get(0).toString());
            }
        }
        try {
            final List<FileArtifact> l = filterDependencyJar(params, repositoryList);
            for (FileArtifact a : l) {
                if (!rootList.containsValue(a)) {
                    rootList.put(rootList.size(), a);
                }
            }
        } catch (ArtifactDescriptorException e) {
            //找到不指定包错误
            System.out.println(ExceptionUtil.outStackTrace(e));
        }
    }

    /**
     * 下载全部引用包
     *
     * @param file      本地仓库目录
     * @param urls      格式[id=maven-public,type=,user=,password=,url=|id=maven-public,type=,user=,password=,url=]
     * @param artifacts 格式ghost.framework.web:ghost-framework-spring-module:1.0-SNAPSHOT,ghost.framework.web:ghost-framework-spring-module:1.0-SNAPSHOT
     * @return
     * @throws Exception
     */
    public static HashMap<Integer, FileArtifact> downloadAllDependencies(File file, String urls, List<Artifact> artifacts) throws Exception {
        //初始化仓库列表
        List<RemoteRepository> repositoryList = null;
        if (!StringUtils.isEmpty(urls)) {
            repositoryList = getRemoteRepositoryList(urls);
        }
        //repositoryList.addAll(Booter.newRepositories());
        //下载顶层包列表
        HashMap<Integer, FileArtifact> srcList = new HashMap<>();
        for (Artifact artifact : artifacts) {
            srcList.put(srcList.size(), new FileArtifact(artifact));
        }
        HashMap<Integer, FileArtifact> list = new HashMap<>();
        //遍历下载包引用
        for (FileArtifact artifact : srcList.values()) {
            downloadAllDependencies(file, repositoryList, list, artifact);
        }
        //下载源包
        download(file, repositoryList, srcList);
        //下载引用包
        download(file, repositoryList, list);
        for (FileArtifact artifact : list.values()) {
            if (!srcList.containsValue(artifact)) {
                srcList.put(srcList.size(), artifact);
            }
        }
        return list;
    }

    private static void download(File m2Dir, List<RemoteRepository> repositoryList, HashMap<Integer, FileArtifact> list) {
        for (FileArtifact artifact : list.values()) {
            try {
                Params params = new Params(artifact.getArtifact());
                params.setSavePath(m2Dir.getPath());
                download(params, repositoryList);
                artifact.setFile(params.getJarPath());
            } catch (Exception e) {
                ExceptionUtil.debugOrError(log, e);
            }
        }
    }

    /**
     * 循环遍历所有引用包
     *
     * @param m2Dir
     * @param repositoryList
     * @param rootList
     * @param parent
     * @throws Exception
     */
    private static void downloadAllDependencies(File m2Dir, List<RemoteRepository> repositoryList, HashMap<Integer, FileArtifact> rootList, FileArtifact parent) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug(parent.toString());
        } else {
            log.info(parent.toString());
        }
        Params params = new Params(parent.getArtifact());
        params.setSavePath(m2Dir.getPath());
        //判断是否为多版本引用，解决多版本引用获取依赖列表被中断问题
        if (params.getVersion().startsWith("[")) {
            //处理指定多版本无法正常遍历版本获取问题
            //格式比如[1.2.1, 1.3.0)这种指定多版本格式问题
            String[] strings = StringUtils.split(params.getVersion(), ",");
            //验证是否有多个版本指定
            if (strings.length == 2) {
                //指定开始一个版本
                params.setVersion(strings[0].substring(1));
            }
            //多版本引用
            List<Version> list = getAllVersions(params);
            //判断是否未指定最小版本
            if (params.getVersion().startsWith("[0,")) {
                //未指定版本，获取最大版本包位置
                params.setVersion(list.get(list.size() - 1).toString());
            } else {
                //获取指定版本的第一个开始位置版本
                //该位置是包指定了最小版本号的版本位置
                params.setVersion(list.get(0).toString());
            }
        }
        try {
            final List<FileArtifact> l = filterDependencyJar(params, repositoryList);
            for (FileArtifact a : l) {
                if (!rootList.containsValue(a)) {
                    rootList.put(rootList.size(), a);
                    downloadAllDependencies(m2Dir, repositoryList, rootList, a);
                }
            }
        } catch (ArtifactDescriptorException e) {
            //找到不指定包错误
            log.error(ExceptionUtil.outStackTrace(e));
        }
    }

    /**
     * 从指定maven地址下载指定jar包
     * //     * @param artifact maven-jar包的三围定位（groupId:artifactId:version)
     * //     * @param repositoryURL maven库的URL地址
     * //     * @param username 若需要权限，则需使用此参数添加用户名，否则设为null
     * //     * @param password 同上
     *
     * @throws ArtifactResolutionException
     */
    public static void download(Params params, List<RemoteRepository> remoteRepositoryList) throws ArtifactResolutionException, VersionRangeResolutionException {
        //初始化请求对象
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem, params.getSavePath(), params);
        //下载一个jar包
        Artifact artifact = new DefaultArtifact(params.getGroupId(), params.getArtifactId(), null, params.getVersion());
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(remoteRepositoryList);
        repoSystem.resolveArtifact(session, artifactRequest);
        if (log.isDebugEnabled()) {
            log.debug(artifact + " resolved to  " + params.getFile());
        }
//        if (artifact.isSnapshot()) {
//            VersionRangeRequest rangeRequest = new VersionRangeRequest(artifact, remoteRepositoryList, null);
//            VersionRangeResult rangeResult = repoSystem.resolveVersionRange(session, rangeRequest);
//            System.out.println(artifact + " resolved to  " + rangeResult.toString());
//        }
    }
    public static final List<FileArtifact> filterDependencyJar(Params params, List<RemoteRepository> remoteRepositoryList) throws Exception {
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem, params.getSavePath(), params);
        return filterDependencyJar(new FileArtifact(new DefaultArtifact(params.getGroupId(), params.getArtifactId(), null, params.getVersion())), repoSystem, session, remoteRepositoryList);
    }

    private static final List<FileArtifact> filterDependencyJar(FileArtifact artifact, RepositorySystem repoSystem, RepositorySystemSession session, List<RemoteRepository> remoteRepositoryList) throws Exception {
        List<FileArtifact> artifactList = new ArrayList<>();
        ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
        descriptorRequest.setArtifact(artifact.getArtifact());
        descriptorRequest.setRepositories(remoteRepositoryList);
        ArtifactDescriptorResult descriptorResult = repoSystem.readArtifactDescriptor(session, descriptorRequest);
        for (Dependency dependency : descriptorResult.getDependencies()) {
            FileArtifact dependencyArtifact = new FileArtifact(dependency.getArtifact());
            if (artifactList.contains(dependencyArtifact)) {
                continue;
            }
            artifactList.add(dependencyArtifact);
        }
        return artifactList;
    }

    /**
     * 根据groupId和artifactId获取所有版本列表
     *
     * @param params Params对象，包括基本信息
     * @return version列表
     * @throws VersionRangeResolutionException
     */
    public static List<Version> getAllVersions(Params params) throws VersionRangeResolutionException {
        RepositorySystem repoSystem = newRepositorySystem();
        RepositorySystemSession session = newSession(repoSystem, params.getSavePath(), params);
        //查询所有版本。
        Artifact artifact = new DefaultArtifact(params.getGroupId() + ":" + params.getArtifactId() + ":[0,)");
        if (StringUtils.isEmpty(params.getVersion())) {
            artifact = new DefaultArtifact(params.getGroupId() + ":" + params.getArtifactId() + ":[0,)");
        } else {
            artifact = new DefaultArtifact(params.getGroupId() + ":" + params.getArtifactId() + ":" + params.getVersion());
        }
        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        rangeRequest.setRepositories(Booter.newRepositories());
        VersionRangeResult rangeResult = repoSystem.resolveVersionRange(session, rangeRequest);
        List<Version> versions = rangeResult.getVersions();
        if (log.isDebugEnabled()) {
            log.debug("Available versions " + versions);
        }
        return versions;
    }

    /**
     * @param localRepositoryFile 本地仓库目录
     * @param artifact
     * @return
     * @throws DependencyCollectionException
     */
    public static DependencyNode getDependencyNode(File localRepositoryFile, DefaultArtifact artifact) throws DependencyCollectionException {
        RepositorySystem system = newRepositorySystem();
        RepositorySystemSession session = newSession(system, localRepositoryFile, new Params(artifact));
        return getDependencyNode(system, session, newRepositories(), artifact, "compile");
    }

    public static DependencyNode getDependencyNode(RepositorySystem repoSystem, RepositorySystemSession session, List<RemoteRepository> remoteRepositoryList, DefaultArtifact artifact)
            throws DependencyCollectionException {
        return getDependencyNode(repoSystem, session, remoteRepositoryList, artifact, "compile");
    }

    public static DependencyNode getDependencyNode(RepositorySystem repoSystem, RepositorySystemSession session, List<RemoteRepository> remoteRepositoryList, DefaultArtifact artifact, String scope)
            throws DependencyCollectionException {
        Dependency dependency = new Dependency(artifact, scope);
        CollectRequest collectRequest = new CollectRequest(dependency, remoteRepositoryList);
        DependencyNode node = repoSystem.collectDependencies(session, collectRequest).getRoot();
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot(node);
        try {
            repoSystem.resolveDependencies(session, dependencyRequest);
        } catch (DependencyResolutionException e) {
            log.error(e.getMessage());
        }
        return node;
    }

    public static PreorderNodeListGenerator getNodeListGenerator(RepositorySystem repoSystem, RepositorySystemSession session, List<RemoteRepository> remoteRepositoryList, DefaultArtifact artifact, String scope)
            throws DependencyCollectionException {
        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        getDependencyNode(repoSystem, session, remoteRepositoryList, artifact, scope).accept(nlg);
        return nlg;
    }
    public static List<FileArtifact> getDependencyNodeDownloadURLArtifactList(File localRepositoryPath, List<RemoteRepository> remoteRepositoryList,
                                                                              Artifact artifact)
            throws ArtifactResolutionException, IllegalArgumentException, DependencyCollectionException {
        List<Artifact> artifacts = new ArrayList<>();
        artifacts.add(artifact);
        return getDependencyNodeDownloadURLArtifactList(localRepositoryPath, remoteRepositoryList, artifacts, null);
    }
    public static List<FileArtifact> getDependencyNodeDownloadURLArtifactList(
            File localRepositoryPath,
            List<RemoteRepository> remoteRepositoryList,
            Artifact artifact,
            VerifyDownloadCallback downloadCallback)
            throws ArtifactResolutionException, IllegalArgumentException, DependencyCollectionException {
        List<Artifact> artifacts = new ArrayList<>();
        artifacts.add(artifact);
        return getDependencyNodeDownloadURLArtifactList(localRepositoryPath, remoteRepositoryList, artifacts, downloadCallback, null);
    }

    /**
     * 获取包引用列表
     *
     * @param localRepositoryFile  maven仓库路径
     * @param remoteRepositoryList 仓库地址列表
     * @param artifacts            包信息
     * @return
     * @throws ArtifactResolutionException
     * @throws IllegalArgumentException
     */
    public static List<FileArtifact> getDependencyNodeDownloadURLArtifactList(File localRepositoryFile, List<RemoteRepository> remoteRepositoryList,
                                                                              List<Artifact> artifacts)
            throws ArtifactResolutionException, IllegalArgumentException, DependencyCollectionException {
        return getDependencyNodeDownloadURLArtifactList(localRepositoryFile, remoteRepositoryList, artifacts, null);
    }

    /**
     * 获取包引用列表
     *
     * @param localRepositoryFile  maven仓库路径
     * @param remoteRepositoryList 仓库地址列表
     * @param artifacts            包信息
     * @return
     * @throws DependencyCollectionException
     * @throws DependencyResolutionException
     * @throws ArtifactResolutionException
     */
    public static List<FileArtifact> getDependencyNodeDownloadURLArtifactList(
            File localRepositoryFile,
            List<RemoteRepository> remoteRepositoryList,
            List<Artifact> artifacts,
            ClassLoader loader)
            throws IllegalArgumentException, DependencyCollectionException, ArtifactResolutionException {
        RepositorySystem system = newRepositorySystem();
        RepositorySystemSession session = newSession(system, localRepositoryFile);
        List<Artifact> artifactList = getDependencyNodeArtifactList(system, session, localRepositoryFile, remoteRepositoryList, artifacts);
        List<FileArtifact> fileArtifacts = new ArrayList<>();
        //初始化仓库列表
        List<RemoteRepository> repositoryList = null;
        if (remoteRepositoryList == null || remoteRepositoryList.size() == 0) {
            repositoryList = newRepositories();
        } else {
            repositoryList = remoteRepositoryList;
        }
        for (Artifact a : artifacts) {
            FileArtifact fileArtifact = new FileArtifact(a);
            Params params = new Params(a);
            params.setSavePath(localRepositoryFile);
            download(system, session, params, repositoryList);
            //判断是否重复
            if (ArtifactUitl.contains(fileArtifacts, params.getJarPath())) {
                continue;
            }
            fileArtifact.setFile(params.getJarPath());
            fileArtifacts.add(fileArtifact);
        }
        //没有指定类加载器，下载所有依赖包列表
        for (Artifact a : artifactList) {
            FileArtifact fileArtifact = new FileArtifact(a);
            Params params = new Params(a);
            params.setSavePath(localRepositoryFile);
            download(system, session, params, repositoryList);
            //判断是否重复
            if (ArtifactUitl.contains(fileArtifacts, params.getJarPath())) {
                continue;
            }
            fileArtifact.setFile(params.getJarPath());
            fileArtifacts.add(fileArtifact);
        }
//        } else {
//            //获取类加载器加载的包列表
//            final List<URLManifest> list = JarUtil.getManifests(loader);
//            //判断在指定类加载器中判断包是否存在，如果存在侧不下载该包
//            for (Artifact a : artifactList) {
//                for (URLManifest manifest : list) {
//                    if (manifest.getImplTitle().equals(a.getGroupId()) &&
//                            manifest.getImplVendor().equals(a.getArtifactId()) &&
//                            manifest.getImplVersion().equals(a.getVersion())) {
//                        //版本存在
//                    } else {
//                        //不存在
//                        URLArtifact urlArtifact = new URLArtifact(a);
//                        urlArtifacts.add(urlArtifact);
//                        Params params = new Params(a);
//                        params.setSavePath(m2Dir.getPath());
//                        download(params, repositoryList);
//                        urlArtifact.setFile(params.getJarPath());
//                    }
//                }
//            }
//        }
        return fileArtifacts;
    }
    /**
     * 获取包引用列表
     *
     * @param localRepositoryFile  maven仓库路径
     * @param remoteRepositoryList 仓库地址列表
     * @param artifacts            包信息
     * @return
     * @throws DependencyCollectionException
     * @throws DependencyResolutionException
     * @throws ArtifactResolutionException
     */
    public static List<FileArtifact> getDependencyNodeDownloadURLArtifactList(
            File localRepositoryFile,
            List<RemoteRepository> remoteRepositoryList,
            List<Artifact> artifacts,
            VerifyDownloadCallback downloadCallback,
            ClassLoader loader)
            throws IllegalArgumentException, DependencyCollectionException, ArtifactResolutionException {
        RepositorySystem system = newRepositorySystem();
        RepositorySystemSession session = newSession(system, localRepositoryFile);
        List<Artifact> artifactList = getDependencyNodeArtifactList(system, session, localRepositoryFile, remoteRepositoryList, artifacts);
        List<FileArtifact> fileArtifacts = new ArrayList<>();
        //初始化仓库列表
        List<RemoteRepository> repositoryList = null;
        if (remoteRepositoryList == null || remoteRepositoryList.size() == 0) {
            repositoryList = newRepositories();
        } else {
            repositoryList = remoteRepositoryList;
        }
        for (Artifact a : artifacts) {
            FileArtifact fileArtifact = new FileArtifact(a);
            Params params = new Params(a);
            params.setSavePath(localRepositoryFile);
            //验证包是否需要下载，如果无需下载侧设设置包路径后并返回否，无需下载包
            if (downloadCallback.isDownload(fileArtifact)) {
                //判断是否重复
                if (ArtifactUitl.contains(fileArtifacts, params.getJarPath())) {
                    continue;
                }
                download(system, session, params, repositoryList);
                fileArtifact.setFile(params.getJarPath());
            }
            fileArtifacts.add(fileArtifact);
        }
        //没有指定类加载器，下载所有依赖包列表
        for (Artifact a : artifactList) {
            FileArtifact fileArtifact = new FileArtifact(a);
            Params params = new Params(a);
            params.setSavePath(localRepositoryFile);
            //验证包是否需要下载，如果无需下载侧设设置包路径后并返回否，无需下载包
            if (downloadCallback.isDownload(fileArtifact)) {
                //判断是否重复
                if (ArtifactUitl.contains(fileArtifacts, params.getJarPath())) {
                    continue;
                }
                download(system, session, params, repositoryList);
                fileArtifact.setFile(params.getJarPath());
            }
            fileArtifacts.add(fileArtifact);
        }
//        } else {
//            //获取类加载器加载的包列表
//            final List<URLManifest> list = JarUtil.getManifests(loader);
//            //判断在指定类加载器中判断包是否存在，如果存在侧不下载该包
//            for (Artifact a : artifactList) {
//                for (URLManifest manifest : list) {
//                    if (manifest.getImplTitle().equals(a.getGroupId()) &&
//                            manifest.getImplVendor().equals(a.getArtifactId()) &&
//                            manifest.getImplVersion().equals(a.getVersion())) {
//                        //版本存在
//                    } else {
//                        //不存在
//                        URLArtifact urlArtifact = new URLArtifact(a);
//                        urlArtifacts.add(urlArtifact);
//                        Params params = new Params(a);
//                        params.setSavePath(m2Dir.getPath());
//                        download(params, repositoryList);
//                        urlArtifact.setFile(params.getJarPath());
//                    }
//                }
//            }
//        }
        return fileArtifacts;
    }
    /**
     * @param system
     * @param session
     * @param params
     * @param repositoryList
     * @throws ArtifactResolutionException
     */
    private static void download(RepositorySystem system, RepositorySystemSession session, Params params, List<RemoteRepository> repositoryList) throws ArtifactResolutionException {
        //初始化请求对象
        //下载一个jar包
        Artifact artifact = new DefaultArtifact(params.getGroupId() + ":" + params.getArtifactId() + ":" + params.getVersion());
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(repositoryList);
        system.resolveArtifact(session, artifactRequest);
        if (log.isDebugEnabled()) {
            log.debug(artifact + " resolved to  " + params.getFile());
        }
    }

    private static List<Artifact> getDependencyNodeArtifactList(RepositorySystem system, RepositorySystemSession session, File localRepositoryFile,
                                                                List<RemoteRepository> remoteRepositoryList, List<Artifact> artifacts) throws DependencyCollectionException {
        List<Artifact> artifactList = new ArrayList<>();
        for (DependencyNode node : getDependencyNode(system, session, localRepositoryFile, remoteRepositoryList, artifacts)) {
            dependencyNodeArtifactList(node, artifactList);
        }
        return artifactList;
    }

    private static List<DependencyNode> getDependencyNode(RepositorySystem system, RepositorySystemSession session, File localRepositoryFile, List<RemoteRepository> repositoryList, List<Artifact> artifacts) throws DependencyCollectionException {
        //初始化仓库列表
        List<RemoteRepository> list = null;
        if (repositoryList == null || repositoryList.size() == 0) {
            list = newRepositories();
        } else {
            list = repositoryList;
        }
        //repositoryList.addAll(Booter.newRepositories());
//        RepositorySystem system = newRepositorySystem();
        List<DependencyNode> nodes = new ArrayList<>();
        //下载顶层包列表
        for (Artifact v : artifacts) {
            Params params = new Params(v);
            params.setSavePath(localRepositoryFile.getPath());
//            RepositorySystemSession session = newSession(system, localRepositoryFile, new Params(v));
            nodes.add(getDependencyNode(system, session, list, v));
        }
        return nodes;
    }

    /**
     * 创建会话
     * @param localRepositoryFile 本地仓库目录
     * @return
     */
    private static RepositorySystemSession newSession(File localRepositoryFile) {
        return newSession(newRepositorySystem(), localRepositoryFile);
    }
    /**
     * 使用仓库系统创建会话对象
     *
     * @param system              仓库系统
     * @param localRepositoryFile 本地仓库目录
     * @return
     */
    private static RepositorySystemSession newSession(RepositorySystem system, File localRepositoryFile) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(localRepositoryFile);
        session.setConfigProperty(ConflictResolver.CONFIG_PROP_VERBOSE, true);
        session.setConfigProperty(DependencyManagerUtils.CONFIG_PROP_VERBOSE, true);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());
        //判断设置本地仓库路径
        if ((session.getLocalRepositoryManager() == null || session.getLocalRepository() == null) && localRepositoryFile != null) {
            session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, new LocalRepository(localRepositoryFile)));
        }
        return session;
    }

    /**
     * 获取包引用列表
     *
     * @param localRepositoryFile  maven仓库路径
     * @param repositoryServerList 仓库地址列表
     * @param artifacts            包信息
     * @return
     * @throws DependencyCollectionException
     * @throws DependencyResolutionException
     */
    public static List<Artifact> getDependencyNodeArtifactList(File localRepositoryFile, List<RemoteRepository> repositoryServerList, List<Artifact> artifacts) throws DependencyCollectionException, DependencyResolutionException {
        List<Artifact> artifactList = new ArrayList<>();
        for (DependencyNode node : getDependencyNode(localRepositoryFile, repositoryServerList, artifacts)) {
            dependencyNodeArtifactList(node, artifactList);
        }
        return artifactList;
    }

    /**
     * @param node
     * @param artifacts
     */
    private static void dependencyNodeArtifactList(DependencyNode node, List<Artifact> artifacts) {
        for (DependencyNode n : node.getChildren()) {
            if (!artifacts.contains(n.getArtifact())) {
                //重新new DefaultArtifact解决快照版本带日期的包版本好问题
                artifacts.add(new DefaultArtifact(n.getArtifact().getGroupId(), n.getArtifact().getArtifactId(), null, n.getVersion().toString()));
                dependencyNodeArtifactList(n, artifacts);
            }
        }
    }

//    /**
//     * 获取指定程序包列表的引用节点
//     *
//     * @param file
//     * @param repositoryServerList
//     * @param artifacts
//     * @return
//     * @throws DependencyCollectionException
//     * @throws DependencyResolutionException
//     */
//    public static List<DependencyNode> getDependencyNode(File file, List<MavenRepositoryServer> repositoryServerList, List<Artifact> artifacts) throws DependencyCollectionException {
//        //初始化仓库列表
//        List<RemoteRepository> repositoryList = null;
//        if (repositoryServerList == null || repositoryServerList.size() == 0) {
//            repositoryList = newRepositories();
//        } else {
//            repositoryList = getRemoteRepositoryList(repositoryServerList);
//        }
//        //repositoryList.addAll(Booter.newRepositories());
//        RepositorySystem system = newRepositorySystem();
//        List<DependencyNode> nodes = new ArrayList<>();
//        //下载顶层包列表
//        for (Artifact artifact : artifacts) {
//            Params params = new Params(artifact);
//            params.setSavePath(file.getPath());
//            RepositorySystemSession session = newSession(system, params);
//            nodes.add(getDependencyNode(system, session, repositoryList, artifact));
//        }
//        return nodes;
//    }

    /**
     * 下载一个jar包
     *
     * @param localRepositoryFile     maven仓库路径
     * @param remoteRepositoryList     仓库地址列表
     * @param artifact 包信息
     * @return
     * @throws ArtifactResolutionException
     */
    public static FileArtifact download(File localRepositoryFile, List<MavenRepositoryServer> remoteRepositoryList, Artifact artifact)
            throws ArtifactResolutionException, VersionRangeResolutionException {
        //初始化仓库列表
        List<RemoteRepository> repositoryList = null;
        if (remoteRepositoryList == null || remoteRepositoryList.size() == 0) {
            repositoryList = newRepositories();
        } else {
            repositoryList = getRemoteRepositoryList(remoteRepositoryList);
        }
        FileArtifact a = new FileArtifact(artifact);
        Params params = new Params(a);
        params.setSavePath(localRepositoryFile.getPath());
        download(params, repositoryList);
        a.setFile(params.getJarPath());
        return a;
    }

    /**
     * maven仓库转换为远程仓库对象
     *
     * @param repositoryServer
     * @return
     */
    private static RemoteRepository getRemoteRepository(MavenRepositoryServer repositoryServer) {
        if (StringUtils.isEmpty(repositoryServer.getUsername()) && StringUtils.isEmpty(repositoryServer.getPassword())) {
            return new RemoteRepository.Builder(
                    repositoryServer.getId(),
                    repositoryServer.getType(),
                    repositoryServer.getUrl()).setSnapshotPolicy(new RepositoryPolicy()).setReleasePolicy(new RepositoryPolicy()).build();
        }
        return new RemoteRepository.Builder(
                repositoryServer.getId(),
                repositoryServer.getType(),
                repositoryServer.getUrl()).setSnapshotPolicy(new RepositoryPolicy()).setReleasePolicy(new RepositoryPolicy()).setAuthentication(new AuthenticationBuilder().addUsername(repositoryServer.getUsername()).addPassword(repositoryServer.getPassword()).build()).build();
    }

    /**
     * 使用字符格式仓库信息获取仓库地址列表
     *
     * @param urls 格式[id=maven-public,type=,user=,password=,url=|id=maven-public,type=,user=,password=,url=]
     * @return
     */
    public static List<RemoteRepository> getRemoteRepositoryList(String urls) {
        List<RemoteRepository> list = new ArrayList<>();
        if (StringUtils.isEmpty(urls)) {
            return list;
        }
        for (String url : StringUtils.split(urls, "|")) {
            //格式id=maven-public,type=,user=,password=,url=
            String[] strings = StringUtils.split(url, ",");
            //判断是否使用账号密码登录仓库
            if (StringUtils.isEmpty(strings[2]) && StringUtils.isEmpty(strings[3])) {
                //创建无账号密码仓库地址
                list.add(new RemoteRepository.Builder(
                        (StringUtils.isEmpty(strings[0]) ? null : strings[0]),
                        (StringUtils.isEmpty(strings[1]) ? null : strings[1]),
                        strings[4]).setSnapshotPolicy(new RepositoryPolicy()).setReleasePolicy(new RepositoryPolicy()).build());
            } else {
                //创建有账号密码仓库地址
                list.add(new RemoteRepository.Builder(
                        (StringUtils.isEmpty(strings[0]) ? null : strings[0]),
                        (StringUtils.isEmpty(strings[1]) ? null : strings[1]),
                        strings[4]).setSnapshotPolicy(new RepositoryPolicy()).setReleasePolicy(new RepositoryPolicy())
                        .setAuthentication(new AuthenticationBuilder().addUsername(strings[2]).addPassword(strings[3]).build()).build());
            }
        }
        return list;
    }

//    /**
//     * 下载一个jar包
//     *
//     * @param m2Dir          maven仓库路径
//     * @param repositoryList 仓库地址列表
//     * @param artifact       包信息
//     * @return
//     * @throws ArtifactResolutionException
//     */
//    public static URLArtifact download(File m2Dir, List<MavenRepositoryServer> repositoryList, Artifact artifact) throws ArtifactResolutionException {
//        URLArtifact a = new URLArtifact(artifact);
//        Params params = new Params(a);
//        params.setSavePath(m2Dir.getPath());
//        download(params, getRemoteRepositoryList(repositoryList));
//        a.setFile(params.getJarPath());
//        return a;
//    }

    /**
     * 将DefaultArtifact转换为jar文件全名
     *
     * @param artifact
     * @return
     */
    public static String artifactToJarName(Artifact artifact) {
        return artifact.getArtifactId() + "-" + artifact.getVersion() + ".jar";
    }

    /**
     * @param localRepositoryFile      本地仓库目录
     * @param mavenRepositoryContainer
     * @param artifact
     * @return
     * @throws ArtifactResolutionException
     * @throws IllegalArgumentException
     * @throws DependencyCollectionException
     * @throws DependencyResolutionException
     */
    public static List<FileArtifact> getDependencyNodeDownloadURLArtifactList(
            File localRepositoryFile,
            List<MavenRepositoryServer> mavenRepositoryContainer,
            DefaultArtifact artifact) throws ArtifactResolutionException, IllegalArgumentException, DependencyCollectionException, DependencyResolutionException {
        return getDependencyNodeDownloadURLArtifactList(localRepositoryFile, getRemoteRepositoryList(mavenRepositoryContainer), artifact);
    }
}