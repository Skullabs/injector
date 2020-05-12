#!/usr/bin/env sh
set -e
cd $(dirname $0)
. ./builder.conf

# FUNCTIONS
print(){ printf "$@"; }
println(){ printf "$@\n"; }

build(){
  ./mvnw clean package
}

deploy_local(){
  ./mvnw clean install -DskipTests=true
}

deploy_remote(){
  ./mvnw clean deploy -P deploy-sources,use-sonatype-stagging-repo -DskipTests=true
}

bump_version(){
  print "New version: "
  read -r VERSION

  VERSION=$(print $VERSION | sed '/\([0-9]\{1,3\}\)\.\([0-9]\{1,3\}\)\.\([0-9]\{1,3\}\)/!d')
  if [ "$VERSION" = "" ]; then
    println "Please use semantic version."
    exit 2
  fi
  
  POM_FILES=$(find . -name 'pom.yml')

  sed -i -e "s/version: \"$CURRENT_VERSION\"/version: \"$VERSION\"/" $POM_FILES
  println "CURRENT_VERSION='$VERSION'" > ./builder.conf
}

# MAIN
case $1 in
  "bump") bump_version ;;
  "build") build ;;
  "deploy_local"|"local") deploy_local ;;
  "deploy"|"remote") deploy_remote ;;
  *)
    cat <<EOF | sed 's/^[ \t]*//'
      Usage: $0 <OPTION>

      Where OPTION is one of the following:
      - build
      - bump - bumps the version
      - local - deploys generated artifacts locally
      - remote - deploys generated artifacts into Maven Central
      - deploy (same as 'remote')
      - deploy_local (same as 'local')

EOF
    exit 1
  ;;
esac