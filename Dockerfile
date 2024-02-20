FROM sbtscala/scala-sbt:eclipse-temurin-jammy-21_35_1.9.7_3.3.1 as build

ADD . build
RUN cd build && \
    sbt universal:packageZipTarball && \
    cd target/universal && \
    tar zxvf hello-server-1.0.tgz && \
    mv hello-server-1.0 /hello-server

FROM eclipse-temurin:21_35-jre-alpine as main
COPY --from=build /hello-server /hello-server

ENTRYPOINT ["hello-server/bin/hello-server"]
CMD [""]
