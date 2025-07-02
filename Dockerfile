FROM openjdk:21

WORKDIR /app

COPY *.java ./

RUN javac *.java

CMD java AtomicLetterCounter.java && java LetterCounter.java && java LetterCounterSingleThread.java