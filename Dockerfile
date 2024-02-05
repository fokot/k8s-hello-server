FROM sbtscala/scala-sbt:eclipse-temurin-jammy-21_35_1.9.7_3.3.1 as build

ADD . build
RUN cd build && \
    sbt universal:packageZipTarball && \
    cd target/universal && \
    tar zxvf config-server-1.0.tgz && \
    mv config-server-1.0 /config-server

FROM eclipse-temurin:21_35-jre-alpine as main
COPY --from=build /config-server /config-server

ENTRYPOINT ["config-server/bin/config-server"]
CMD [""]
