FROM gradle:8.14.3-jdk21 AS build
WORKDIR /workspace

COPY . .

RUN chmod +x ./gradlew \
  && ./gradlew --no-daemon :app-service:bootJar -x test

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S app \
  && adduser -S -G app -u 10001 app

WORKDIR /app

COPY --from=build /workspace/applications/app-service/build/libs/MsFranchises.jar /app/app.jar

RUN chown -R app:app /app
USER app

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=15s --timeout=3s --start-period=30s --retries=10 \
  CMD wget -qO- http://127.0.0.1:8080/ms-franchises/api/v1/health >/dev/null 2>&1 || exit 1

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]