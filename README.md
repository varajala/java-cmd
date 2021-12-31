# java-cmd-interpreter

A simple command line interpreter made with Java.


## Building and Running:

Build with Maven:

    mvn compile


Run tests:

    mvn test


Run application:

    ./maven-exec.sh


With Python script:

Download JUnit 5 dependencies:

    - apiguardian-api-1.0.0.jar        
    - jars/junit-jupiter-5.X.Y.jar,
    - junit-jupiter-api-5.X.Y.jar
    - junit-jupiter-engine-5.X.Y.jar
    - junit-jupiter-params-5.X.Y.jar

More info in [JUnit docs](#https://junit.org/junit5/docs/current/user-guide/#dependency-metadata).

Save these .jar-files into your machine and insert the install path to the Python script:

```python
JARS_LIB_PATH = '/path/to/jars'
```

Remember to change the package versions also:

```python
options.classpath = ('.',
    os.path.join(JARS_LIB_PATH, 'apiguardian-api-1.0.0.jar'),        
    os.path.join(JARS_LIB_PATH, 'jars/junit-jupiter-5.7.0.jar'),
    os.path.join(JARS_LIB_PATH, 'junit-jupiter-api-5.7.0.jar'),
    os.path.join(JARS_LIB_PATH, 'junit-jupiter-engine-5.7.0.jar'),
    os.path.join(JARS_LIB_PATH, 'junit-jupiter-params-5.7.0.jar'),
    )
```

Run the script:

    python build.py
