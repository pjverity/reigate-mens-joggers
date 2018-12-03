FROM openjdk:11-jre-slim-sid

RUN mkdir -p /usr/local/assets/images \
    && addgroup --system clubbed \
    && adduser --system clubbedapp --ingroup clubbed

USER clubbedapp

ARG BUILD_OUTPUT_PATH=/target
ARG ARTIFACT_NAME=reigate-mens-joggers
ARG ARTIFACT_VERSION

ENV ARTIFACT ${ARTIFACT_NAME}-${ARTIFACT_VERSION}.war
ENV JAVA_OPTS="-Dspring.profiles.active=k8s -Dspring.config.additional-location=file:/usr/local/config/"

COPY ${BUILD_OUTPUT_PATH}/${ARTIFACT} /usr/local/${ARTIFACT}

EXPOSE 8080
WORKDIR /usr/local

CMD /usr/local/${ARTIFACT}
