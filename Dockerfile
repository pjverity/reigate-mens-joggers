FROM openjdk:11-jre-slim-sid

RUN mkdir -p /usr/local/assets/images \
    && addgroup --system clubbed \
    && adduser --system clubbedapp --ingroup clubbed

USER clubbedapp
WORKDIR /usr/local

EXPOSE 8080

ARG ARTIFACT_NAME
ARG ARTIFACT_VERSION

ENV ARTIFACT ${ARTIFACT_NAME}-${ARTIFACT_VERSION}.war

ADD /target/${ARTIFACT} /usr/local/${ARTIFACT}

ENV DATABASE_HOST=postgres DATABASE_NAME=rmj EXTERNAL_STATIC_ASSETS_PATH=/mnt/tomcat/sites/rmj

CMD /usr/local/${ARTIFACT}
