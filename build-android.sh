
export ANDROID_HOME=$HOME/android-sdk-linux
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
#echo "sdk.dir=$ANDROID_HOME" > voice-recorder-app/local.properties
if [ ! -d "$HOME/android-sdk-linux" ]; then
curl --location http://dl.google.com/android/android-sdk_r24.2-linux.tgz | tar -x -z -C $HOME
( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | android update sdk --no-ui --all --filter tools,platform-tools,build-tools-23.0.1,android-23
( sleep 5 && while [ 1 ]; do sleep 1; echo y; done ) | android update sdk --no-ui --all --filter extra-android-m2repository,extra-android-support,extra-google-admob_ads_sdk,extra-google-analytics_sdk_v2,extra-google-google_play_services_froyo,extra-google-google_play_services,extra-google-m2repository,extra-google-play_apk_expansion,extra-google-play_billing,extra-google-play_licensing,extra-google-webdriver
fi
if [ "$1" != "" ]
  then
  	echo "Build Android app for environment $1"
    ./gradlew assemble -Penv=$1
  else
  	echo "Build Android app for default environment"
  	./gradlew assemble
fi
