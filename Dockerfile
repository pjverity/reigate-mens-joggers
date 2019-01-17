FROM openjdk:11-jre-slim-sid

RUN mkdir -p /usr/local/assets/images \
    && addgroup --system clubbed \
    && adduser --system clubbedapp --ingroup clubbed

WORKDIR /usr/local

EXPOSE 8080

ARG ARTIFACT_NAME
ARG ARTIFACT_VERSION

ENV ARTIFACT ${ARTIFACT_NAME}-${ARTIFACT_VERSION}.war

COPY /workspace/target/${ARTIFACT} /usr/local/${ARTIFACT}

RUN chown clubbedapp:clubbed /usr/local/${ARTIFACT}

ENV DATABASE_HOST=postgres DATABASE_NAME=rmj EXTERNAL_STATIC_ASSETS_PATH=/mnt/tomcat/sites/rmj

USER clubbedapp

CMD /usr/local/${ARTIFACT}
