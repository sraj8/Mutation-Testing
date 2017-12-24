name := "zero-allocation-hashing"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq("com.google.guava" % "guava" % "2.0",
  "junit" % "junit" % "4.12",
  "org.apache.directory.server" % "apacheds-all" % "1.5.5",
  "commons-collections" % "commons-collections" % "3.2.1",
  "commons-configuration" % "commons-configuration" % "1.6",
  "commons-lang" % "commons-lang" % "2.5",
  "commons-logging" % "commons-logging" % "1.1.1",
  "org.eclipse.core" % "org.eclipse.core.contenttype" % "3.4.100",
  "org.mod4j.org.eclipse.core" % "jobs" % "3.4.100",
  "org.eclipse.core" % "org.eclipse.core.resources" % "3.6.0.v20100526-0737",
  "org.eclipse.equinox" % "org.eclipse.equinox.common" % "3.6.0.v20100503",
  "org.eclipse.jdt" % "org.eclipse.jdt.core" % "3.10.0",
  "org.eclipse.osgi" % "org.eclipse.osgi" % "3.5.2.R35x_v20100126",
  "org.eclipse.text" % "org.eclipse.text" % "3.5.101",
  "javassist" % "javassist" % "3.12.1.GA",
  "org.hamcrest" % "java-hamcrest" % "2.0.0.0" % "test",
  "com.github.mkolisnyk" % "generex" % "0.0.2",
  "com.googlecode.java-diff-utils" % "diffutils" % "1.3.0",
  "com.google.guava" % "guava" % "22.0"
)

