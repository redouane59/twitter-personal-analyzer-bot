FROM openjdk:8
COPY target/social-media-raiser-*.jar /main.jar
ENV NAME="com.socialmediaraiser.UnfollowLauncher"
ENTRYPOINT [ "java", "-cp", "main.jar", "$NAME" ]