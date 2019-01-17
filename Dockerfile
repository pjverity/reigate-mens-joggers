FROM openjdk:11-jre-slim-sid

ARG ARTIFACT_NAME
ARG ARTIFACT_VERSION

RUN mkdir -p /usr/local/assets/images \
    && addgroup --system clubbed \
    && adduser --system clubbedapp --ingroup clubbed

USER clubbedapp

WORKDIR /usr/local

EXPOSE 8080

ENV ARTIFACT ${ARTIFACT_NAME}-${ARTIFACT_VERSION}.war
ENV DATABASE_HOST=postgres DATABASE_NAME=rmj EXTERNAL_STATIC_ASSETS_PATH=/mnt/tomcat/sites/rmj

COPY /target/${ARTIFACT} /usr/local/${ARTIFACT}

CMD /usr/local/${ARTIFACT}
