#!/bin/bash
# USAGE: ./release.sh 1.1.1

if [ $# -ne 1 ]
  then
    echo "USAGE: ./release.sh 1.1.1"
   exit
fi

function checkResult {
    if [ $? -ne 0 ]
      then
        echo "!!!!!     >>>ERROR<<<    !!!!!"
       exit
    fi
}

DIR=$(dirname $0)
VERSION_NAME=$1
git stash
checkResult
git checkout develop -b release/$VERSION_NAME
checkResult
./gradlew build
checkResult
./gradlew updateVersion $VERSION_NAME
checkResult
git commit -a -m "Update version to $VERSION_NAME."
checkResult
git checkout master
checkResult
git merge --no-ff --no-edit release/$VERSION_NAME
checkResult
git push
checkResult
git tag v$VERSION_NAME
checkResult
git push --tags
checkResult
git checkout develop
checkResult
git merge --no-ff --no-edit release/$VERSION_NAME
checkResult
git push
checkResult
git branch -d release/$VERSION_NAME
checkResult