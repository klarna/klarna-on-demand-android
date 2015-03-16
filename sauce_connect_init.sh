#!/bin/bash

# Inspired by https://gist.github.com/santiycr/5139565

SC_ARCHIVE_NAME="sc-4.3.7-linux"
SC_ARCHIVE="${SC_ARCHIVE_NAME}.tar.gz"
DOWNLOAD_URL="https://saucelabs.com/downloads/$SC_ARCHIVE"
READY_FILE="connect-ready-$RANDOM"

# Get Connect and start it, excepting our weblayer from SSL re-encryption
curl $DOWNLOAD_URL > $SC_ARCHIVE
tar -xzf $SC_ARCHIVE
./${SC_ARCHIVE_NAME}/bin/sc --readyfile $READY_FILE \
    --tunnel-identifier $TRAVIS_JOB_NUMBER \
    --no-ssl-bump-domains inapp.playground.klarna.com \
    -u $SAUCE_USERNAME -k $SAUCE_ACCESS_KEY &

# Wait for Connect to be ready before exiting
while [ ! -f $READY_FILE ]; do
  sleep .5
done
