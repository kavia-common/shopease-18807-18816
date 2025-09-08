#!/bin/bash
cd /home/kavia/workspace/code-generation/shopease-18807-18816/mobile_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

