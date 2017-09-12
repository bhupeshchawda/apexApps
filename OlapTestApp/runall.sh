#!/bin/bash
pushd ../../saarang/operators/olap/
mvn clean install -DskipTests
popd
mvn clean install -DskipTests

