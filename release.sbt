import ReleaseTransformations._

publishTo                     := sonatypePublishToBundle.value
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseTagComment             := s"v${(version in ThisBuild).value}"
releaseVcsSign                := true
sonatypeProfileName           := "com.github.japgolly"
sonatypeSessionName           := s"${name.value} v${version.value}"

pomExtra :=
  <scm>
    <connection>scm:git:github.com/japgolly/tla2json</connection>
    <developerConnection>scm:git:git@github.com:japgolly/tla2json.git</developerConnection>
    <url>github.com:japgolly/tla2json.git</url>
  </scm>
    <developers>
      <developer>
        <id>japgolly</id>
        <name>David Barri</name>
      </developer>
    </developers>

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  runClean, // so that we rebuild with the correct version in BuildInfo
  releaseStepCommand("publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  releaseStepCommand("assembly"), // Generate now for manual upload to the Github release
  setNextVersion,
  commitNextVersion,
  pushChanges
)