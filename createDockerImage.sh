#!/bin/sh
lein ring uberjar
docker build -t marcopaga/analyze-runs:2 .
