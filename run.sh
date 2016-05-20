javac -cp "aipne.jar:lib:src" -d out src/Launcher.java

java -d64 -Xmx6500m -Xms6500m -Xss2048k -Djava.library.path=lib/native -cp "aipne.jar:lib:out" Launcher $1 $2

# /opt/java-oracle/jdk1.8.0_74/bin/jvisualvm

