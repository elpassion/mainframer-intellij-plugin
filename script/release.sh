#!/usr/bin/env bash
git checkout -b release_from_travis
./gradlew release -Prelease.useAutomaticVersion=true
git checkout master
git merge --no-ff release_from_travis
git push
git push origin --delete release_from_travis