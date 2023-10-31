#!/bin/bash

tar -xf "client/target/tpe2-g7-client-1.0-SNAPSHOT-bin.tar.gz" -C "client/target/"
java.exe "$@" -Dhazelcast.logging.type=none  -cp 'client/target/tpe2-g7-client-1.0-SNAPSHOT/lib/jars/*' "ar.edu.itba.pod.hazelcast.client.ClientQuery3"