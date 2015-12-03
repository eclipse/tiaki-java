#!/bin/bash

echo "info: '$(basename $0)' executing in: $PWD" 

# Check env variable setup
if [ -z $PATH_TO_JAVA ];
then
    # Path to the Java CLI
    PATH_TO_JAVA="workspaces/java/iot-github/iot-discovery-jcli"
    echo "warning: env variable 'PATH_TO_JAVA' (path to Java CLI) not set: default: $PATH_TO_JAVA"
fi

TEST_SCRIPT=$(basename $0)
EXIT_CODES_FILE="exit-codes.sh"

# Check the same directory of exit codes
stat $TEST_SCRIPT &> /dev/null
if [ $? -ne 0 ];
then
   echo "error: script and exit codes in different directories"
   exit 1
fi

# Tools
export TIAKI_CLI_HOME="$DEV_HOME/$PATH_TO_JAVA/build/libs"
JCLI="$DEV_HOME/$PATH_TO_JAVA/bin/sd-lookup"

# Functions
function help {
    echo "usage: cli-tester.sh <generate>|<check>|<nocheck>"
    echo " <generate>    Generate an associative array of Exit Codes upon an unchecked run (useful when the script runs for the first time on a platform)"
    echo " <check>       Checked run: expected Exit Codes are checked against the actual ones"
    echo " <unchecked>   Unckecked run: Exit Codes are not checked (usefel in the first phases of testing)"

    exit 1
}

function write_script_header () {
   echo "#!/bin/bash" >> $EXIT_CODES_FILE
   echo "declare -A CODES" >> $EXIT_CODES_FILE
}

function write_script_line () {
  echo "CODES['$1']=$2" >> $EXIT_CODES_FILE
}

GENERATE="false"
TEST_EXIT_CODE="false"
if [ $# -eq 0  ];
then
   echo "error: no argument supplied"
   help
else
   if [ "$1" == "check" ];
   then
       echo "argument: suppied: '$1': Exit Code testing"
       TEST_EXIT_CODE="true"
   elif [ "$1" == "nocheck" ];
   then
       echo "argument: supplied: '$1': no Exit Code testing"
   elif [ "$1" == "generate" ];
   then
       echo "argument: supplied: '$1': generating Exit Codes array"
       GENERATE="true"
   else
      echo "argument: supplied: '$1': unrecognized"
      help
   fi
fi

# Test to be executed
OPTIONS=(
# List Text Records
'-d com -t example -n alabalala'
'-d com -t example' 
'-d coma -t example'
'-d coma -t example'
'-d com -t example -e'
'-d com -t example -n 8.8.4.4'
'-d come -t example -n 1.2.3.4'
'-d com -t exampleXYZ -n 8.8.8'
'-d com -t exampleXYZ'
'-d mail.com -t www'
'-d mail.com -t www -e'
'-d mail.com -t www -n 8.8.8.8'
'-d mail.com -t www -n 208.67.222.222'
'-d 47zlpxulsrha.1.iotverisign.com -t indiamqtt._mqtt._tcp'
'-d 47zlpxulsrha.1.iotverisign.com -t indiamqtt._mqtt._tcp -e'
'-d 47zlpxulsrha.1.iotverisign.com -t indiamqtt._mqtt._tcp -n 8.8.8.8'
'-d 47zlpxulsrha.1.iotverisign.com -t ukamqtt.mqtt.tcp'
'-d 47zlpxulsrha.1.iotverisign.com -t ukmqtt._mqtt._tcp -n 1.2.3.4'
'-d 47zlpxulsrha.1.iotverisign.com -t ukmqtt.mqtt.tcp -e'
'-d 47zlpxulsrha.1.iotverisign.com -t ukmqtt.mqtt.tcp -n 8.8.8.8'
'-d 47zlpxulsrha.1.iotverisign.com -t ukmqtt._mqtt._tcp -n 208.67.222.222'
# List Instances
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -i'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -i -e'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -i -n 8.8.8.8'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -i -n 1.2.3.4'
'-d mcn366rzmd2a.1.iotverisign.comXYZ -s mqtt -i -n 1.2.3.4'
'-d mcn366rzmd2a.1.iotverisign.com -s mqttXYZ -i -e'
'-d mcn366rzmd2a.1.iotverisign.com -s abracadabra -i -e'
'-d mcn366rzmd2a.1.iotverisign.com -s abracadabra -i -n 8.8.4.4'
'-d mcn366rzmd2a.1.iotverisign.comXYZ -s mqtt -i -e'
'-d mcn366rzmd2a.1.iotverisign.comXYZ -s mqttXYZ -i -n 8.8.8.8'
'-d mcn366rzmd2a.1.iotverisign.comXYZ -s mqttXYZ -i -n 208.67.222.222'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -i -n 208.67.222.222'
'-d dns-sd.org -s pdl-datastream -i -e'
'-d dns-sd.org -s pdl-datastream -i -e -n 8.8.8.8'
'-d dns-sd.org -s ftp -i -e'
'-d dns-sd.org -s ftp -i -e -n 8.8.8.8'
'-d dns-sd.org -s afpovertcp -i -e'
'-d dns-sd.org -s afpovertcp -i -n 8.8.8.8'
'-d avu7unxcs7ia.1.iotverisign.com -s coap:udp -i -e'
'-d avu7unxcs7ia.1.iotverisign.com -s coapspecial:udp -i -e'
'-d dns-sd.org -s http:printer:tcp -i -e'
'-d dns-sd.org -s http:printer:tcp -i -n 8.8.8.8'
'-d mcn366rzmd2a.1.iotverisign.com -i -s mqtt -e'
'-d dns-sd.org -i -s http:printer -e'
'-d dns-sd.org -i -s ipp -e'
'-d dns-sd.org -i -s ipp:tcp -e'
'-d mcn366rzmd2a.1.iotverisign.com -i -s mqtt -e'
'-d mcn366rzmd2a.1.iotverisign.com -i -s mqtt:udp -e'
'-d mcn366rzmd2a.1.iotverisign.com -i -s mqtt:tcp -e'
'-d mcn366rzmd2a.1.iotverisign.com -i -s coap -e'
# DNSSEC Check
'-c'
'-c -n 8.8.8.8'
'-c -n 208.67.222.222'
'-cgoogle.com'
'-cgoogle.com -n 208.67.222.222'
'-cexample.com'
'-cexample.com -n 8.8.8.8'
'-cexample.com -n 208.67.222.222'
'-cwww.mail.com'
'-cwww.mail.com -n 8.8.8.8'
'-cmail.com'
'-cmail.com -n 8.8.8.8'
'-cexample.coma'
'-cexample.coma -n 8.8.4.4'
# List Types
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -l'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -l -e'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -l -n 8.8.8.8'
'-d mcn366rzmd2a.1.iotverisign.comXYZ -s mqtt -l'
'-d mcn366rzmd2a.1.iotverisign.comXYZ -s mqttXYZ -l -e'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -l -n 1.2.3.4'
'-d mcn366rzmd2a.1.iotverisign.com -s mqttXYZ -l -e'
'-d mcn366rzmd2a.1.iotverisign.com -s mqttXYZ -l -n 8.8.4.4'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt -l -n 208.67.222.222'
'-d mcn366rzmd2a.1.iotverisign.com -s mqttXYZ -l -n 208.67.222.222'
'-d dns-sd.org -l -e'
'-d dns-sd.org -l -n 8.8.8.8'
# TLSA
'-d djk6epmd4tlq.1.iotverisign.com -s Device2 -x'
'-d djk6epmd4tlq.1.iotverisign.comXYZ -s Device2 -x'
'-d djk6epmd4tlq.1.iotverisign.com -s DeviceXYZ -x'
'-d djk6epmd4tlq.1.iotverisign.com -s Device2 -x -e'
'-d mcn366rzmd2a.1.iotverisign.com -s Device2 -x443:tcp -e'
'-d djk6epmd4tlq.1.iotverisign.com -s Device2 -x -n 8.8.8.8'
'-d djk6epmd4tlq.1.iotverisign.com -s DeviceXYZ -x -n 1.2.3.4'
'-d djk6epmd4tlq.1.iotverisign.com -s DeviceXYZ -x -n 8.8.8.8'
'-d djk6epmd4tlq.1.iotverisign.com -s DeviceXYZ -x -n 208.67.222.222'
'-d djk6epmd4tlq.1.iotverisign.com -s Device2 -x -n 208.67.222.222'
# Command Line Options
'-d dns-sd.org -s :printer:udp -i -e'
'-d dns-sd.org -l -e -i'
'-d dns-sd.org -t -l -i'
'-d djk6epmd4tlq.1.iotverisign.com -s :Device2 -x -n 208.67.222.222'
'-d djk6epmd4tlq.1.iotverisign.com -s a:Device2 -x -n 208.67.222.222'
'-d djk6epmd4tlq.1.iotverisign.com -s Device2: -x -n 208.67.222.222'
'-d mcn366rzmd2a.1.iotverisign.com -s :mqtt -i -e'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt: -i -e'
'-d mcn366rzmd2a.1.iotverisign.com -s mqtt:xerox -i -e'
'-d mcn366rzmd2a.1.iotverisign.com -s tcp:mqtt -i -e'
)

# Regenerate the file containing exit codes
if [ $GENERATE == "true" ];
then
    write_script_header
else
    source $EXIT_CODES_FILE
fi


# Comparison loop
CNTR=1
for OPT in "${OPTIONS[@]}"
do
    echo "->> Testing '$OPT'<<-"
    echo "-++  Test $CNTR  of ${#OPTIONS[*]} ++-"
    # TTL is stripped out, cause it may be variable
    $JCLI $OPT
    EXIT_CODE=$?
    echo "-** Exit Code $EXIT_CODE **-"
    # Regenerate this specific line
    if [ $GENERATE == "true" ];
    then
        write_script_line "$OPT" "$EXIT_CODE"
    fi
    if [ $TEST_EXIT_CODE == "true" ];
    then
        if [ $EXIT_CODE != ${CODES[$OPT]} ]; 
        then
           echo ">FAILED: IS '$EXIT_CODE': EXPECTED '${CODES[$OPT]}'<"
           exit 1
        fi
        echo ">PASSED: IS '$EXIT_CODE': EXPECTED '${CODES[$OPT]}' <"
    fi
    let CNTR=CNTR+1
done
