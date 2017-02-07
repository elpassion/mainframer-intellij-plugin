#!/usr/bin/env bash
git config --global user.email "travis@ci.com"
git config --global user.name "Travis CI"
git checkout -b release_from_travis
./gradlew release -Prelease.useAutomaticVersion=true
git checkout master
git merge --no-ff release_from_travis
git push
git push origin --delete release_from_travis