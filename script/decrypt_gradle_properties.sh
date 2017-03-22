#!/bin/bash
if [ "TRAVIS_PULL_REQUEST" == "false" ]; then
    openssl aes-256-cbc -K $encrypted_192b72faab89_key -iv $encrypted_192b72faab89_iv -in gradle.properties.enc -out gradle.properties -d
fi