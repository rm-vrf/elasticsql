#!/bin/sh

kill -15 `ps -ef | grep java | grep elasticsql | awk '{print $2}'`
