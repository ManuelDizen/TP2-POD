#!/bin/bash

tar -xf "server/target/tpe2-g7-server-1.0-SNAPSHOT-bin.tar.gz" -C "server/target/"
java -cp 'server/target/tpe2-g7-server-1.0-SNAPSHOT/lib/jars/*' -Xmx8192m "ar.edu.itba.pod.hazelcast.server.Server" "$@"