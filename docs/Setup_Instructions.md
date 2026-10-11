# Setup Instructions

## Prerequisites
- Install a JDK (Java Development Kit). This project uses basic Java language features and should work with JDK 8 or later.
- Verify the installation in a terminal:

```bash
java -version
javac -version
```

Record the exact JDK version installed on your computer here before submitting: **JDK version used: [fill in your version]**.

## Compile and run
From the root `LearnTrack` directory:

```bash
javac -d out src/com/airtribe/learntrack/entity/*.java src/com/airtribe/learntrack/exception/*.java src/com/airtribe/learntrack/util/*.java src/com/airtribe/learntrack/service/*.java src/com/airtribe/learntrack/ui/*.java
java -cp out com.airtribe.learntrack.Main
```

