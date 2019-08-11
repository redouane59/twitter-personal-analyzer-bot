FROM java:8
WORKDIR /
ADD Main.jar Main.jar
EXPOSE 8080
CMD java - jar Main.jar