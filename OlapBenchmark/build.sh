#!/bin/bash
pushd /code/olap/saarang/operators/olap
mvn clean install -DskipTests
popd
mvn clean install -DskipTests

