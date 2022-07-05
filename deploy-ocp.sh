#!/bin/bash
./mvnw clean package
rm -rf build/target
cp -r target build/.
oc new-build --strategy docker --binary --name jms-fruits 2>/dev/null
oc start-build jms-fruits --from-dir build --follow

oc new-app -i jms-fruits
   oc set env deployment/jms-fruits QUARKUS_BROKER_URL=tcp://amq-standalone.amq-cluster.svc.cluster.local:61616

oc create route edge --service=jms-fruits