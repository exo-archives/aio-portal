#!/bin/sh

#Production Script to launch GateIn
#See gatein-dev.sh for development starup

# Computes the absolute path of eXo
cd `dirname "$0"`

# Sets some variables
LOG_OPTS="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog"
SECURITY_OPTS="-Djava.security.auth.login.config=../conf/jaas.conf"
EXO_OPTS="-Dexo.product.developing=false"

JAVA_OPTS="-Xshare:auto -Xms128m -Xmx512m $JAVA_OPTS $LOG_OPTS $SECURITY_OPTS $EXO_OPTS"
export JAVA_OPTS

# Launches the server
exec "$PRGDIR"./catalina.sh "$@"
