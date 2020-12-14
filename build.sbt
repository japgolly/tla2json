ThisBuild / homepage     := Some(url("https://github.com/japgolly/tla2json"))
ThisBuild / licenses     := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))
ThisBuild / organization := "com.github.japgolly.tla2json"
ThisBuild / scalaVersion := "2.13.4"
ThisBuild / startYear    := Some(2020)

val defaultScalacOptions = Seq(
  "-deprecation",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-opt-inline-from:**",
  "-opt:l:inline",
  "-opt:l:method",
  "-target:8",
  "-unchecked",                          // Enable additional warnings where generated code depends on assumptions.
  "-Wconf:msg=may.not.be.exhaustive:e",  // Make non-exhaustive matches errors instead of warnings
  "-Wdead-code",                         // Warn when dead code is identified.
  "-Wunused:explicits",                  // Warn if an explicit parameter is unused.
  "-Wunused:implicits",                  // Warn if an implicit parameter is unused.
  "-Wunused:imports",                    // Warn if an import selector is not referenced.
  "-Wunused:locals",                     // Warn if a local definition is unused.
  "-Wunused:nowarn",                     // Warn if a @nowarn annotation does not suppress any warnings.
  "-Wunused:patvars",                    // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates",                   // Warn if a private member is unused.
  "-Xcheckinit",
  "-Xlint:adapted-args",                 // An argument list was modified to match the receiver.
  "-Xlint:constant",                     // Evaluation of a constant arithmetic expression resulted in an error.
  "-Xlint:delayedinit-select",           // Selecting member of DelayedInit.
  "-Xlint:deprecation",                  // Enable -deprecation and also check @deprecated annotations.
  "-Xlint:eta-zero",                     // Usage `f` of parameterless `def f()` resulted in eta-expansion, not empty application `f()`.
  "-Xlint:implicit-not-found",           // Check @implicitNotFound and @implicitAmbiguous messages.
  "-Xlint:inaccessible",                 // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any",                    // A type argument was inferred as Any.
  "-Xlint:missing-interpolator",         // A string literal appears to be missing an interpolator id.
  "-Xlint:nonlocal-return",              // A return statement used an exception for flow control.
  "-Xlint:nullary-unit",                 // `def f: Unit` looks like an accessor; add parens to look side-effecting.
  "-Xlint:option-implicit",              // Option.apply used an implicit view.
  "-Xlint:poly-implicit-overload",       // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow",               // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align",                  // In a pattern, a sequence wildcard `_*` should match all of a repeated parameter.
  "-Xlint:valpattern",                   // Enable pattern checks in val definitions.
  "-Xmixin-force-forwarders:false",      // Only generate mixin forwarders required for program correctness.
  "-Xno-forwarders",                     // Do not generate static forwarders in mirror classes.
  "-Xsource:2.13",
  "-Yjar-compression-level", "9",        // compression level to use when writing jar files
  "-Ymacro-annotations",                 // Enable support for macro annotations, formerly in macro paradise.
  "-Yno-generic-signatures",             // Suppress generation of generic signatures for Java.
  "-Ypatmat-exhaust-depth", "off"
)

val circeVer    = "0.12.3"
val microlibVer = "2.5"

val circeCore          = "io.circe"                      %% "circe-core"   % circeVer
val circeParser        = "io.circe"                      %% "circe-parser" % circeVer
val fastParse          = "com.lihaoyi"                   %% "fastparse"    % "2.2.2"
val microlibTestStdlib = "com.github.japgolly.microlibs" %% "stdlib-ext"   % microlibVer
val microlibTestUtil   = "com.github.japgolly.microlibs" %% "test-util"    % microlibVer
val scopt              = "com.github.scopt"              %% "scopt"        % "4.0.0"
val utest              = "com.github.japgolly.fork"      %% "utest"        % "1.0.3"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "tla2json",

    scalacOptions := defaultScalacOptions,

    libraryDependencies ++= Seq(
      circeCore,
      fastParse,
      microlibTestStdlib,
      scopt,
      utest % Test,
      circeParser % Test,
      microlibTestUtil % Test
    ),

    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := name.value,

    testFrameworks := List(new TestFramework("utest.runner.Framework")),

    // logLevel in assembly := Level.Debug,
    assemblyJarName in assembly := name.value + ".jar",
    test in assembly := {}
  )
