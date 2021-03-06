# java-cmd

A simple command line interpreter made with Java. This project was done as a university course assignment.


## Building and running with Maven:

Compilation:

    mvn compile


Run tests:

    mvn test


Run application:

    ./maven-exec.sh


## Build and run With Python script:

Download JUnit 5 dependencies:

  - apiguardian-api-1.0.0.jar        
  - jars/junit-jupiter-5.X.Y.jar,
  - junit-jupiter-api-5.X.Y.jar
  - junit-jupiter-engine-5.X.Y.jar
  - junit-jupiter-params-5.X.Y.jar

These can be downloaded from [Maven repositories](https://search.maven.org/), more info in
JUnit [docs](https://junit.org/junit5/docs/current/user-guide/#dependency-metadata).

Save these .jar-files into your machine and insert the install path to the Python script:

```python
JARS_LIB_PATH = '/path/to/jars'
```

Insert the standalone console platform jar name:

```python
JUNIT_JAR_PATH = os.path.join(JARS_LIB_PATH, 'junit-platform-console-standalone-X.Y.Zjar')
```

Remember to change the package versions also:

```python
options.classpath = ('.',
    os.path.join(JARS_LIB_PATH, 'apiguardian-api-1.0.0.jar'),        
    os.path.join(JARS_LIB_PATH, 'jars/junit-jupiter-5.X.Y.jar'),
    os.path.join(JARS_LIB_PATH, 'junit-jupiter-api-5.X.Y.jar'),
    os.path.join(JARS_LIB_PATH, 'junit-jupiter-engine-5.X.Y.jar'),
    os.path.join(JARS_LIB_PATH, 'junit-jupiter-params-5.X.Y.jar'),
    )
```

Run the script:

    python build.py
