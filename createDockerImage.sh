#!/bin/sh
lein ring uberjar
docker build -t analyze-runs:1 .
