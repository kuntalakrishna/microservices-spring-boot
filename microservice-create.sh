#/bin/bash# file : getProperty.sh

#This pair of options tell the bash interpreter to exit whenever a command returns with a non-zero exit code.
#set -e 
#set -o pipefail

propertyFile=./microservice-config.properties
SOURCE_USER_PROJECT_PATH='./user-service/'
DTO_APPENDER="DTO"
SERVICE_APPENDER="service"
USER_SERVICE_BASE_PACKAGE=com.dev.ops.micro.service.user
USER_SERVICE_BASE_DIRECTORY=`echo $USER_SERVICE_BASE_PACKAGE | sed 's/\./\//g'`
USER_SERVICE_ENTITY_NAME=User
USER_SERVICE_VARIABLE_NAME=`echo $USER_SERVICE_ENTITY_NAME | sed 's/^[A-Z]/\L&/'`
USER_SERVICE_ENTITY_UPPER_CASE_NAME=`echo "$USER_SERVICE_ENTITY_NAME" | tr '[:lower:]' '[:upper:]'`

SERVICE_JDBC_DRIVER_PROPERTY_NAME=service.jdbc.driver
SERVICE_JDBC_URL_PROPERTY_NAME=service.jdbc.url
SERVICE_JDBC_USERNAME_PROPERTY_NAME=service.jdbc.username
SERVICE_JDBC_PASSWORD_PROPERTY_NAME=service.jdbc.password

# we will pass the property name and property file name as a parameter to this function
# from where we want to read the property
# @param propertyName
# @param fileName
function getPropertyFromFile() {
    # substitute “.” with “\.” so that we can use it as sed expression
    propertyName=`echo $1 | sed -e 's/\./\\\./g'`
    fileName=$2;
    cat $fileName | sed -n -e "s/^[ ]*//g;/^#/d;s/^$propertyName=//p" | tail -1
}

# we will pass the property name and property file name as a parameter to this function
# in which we want to replace the property
# @param propertyName
# @param value to replace
# @param fileName
function replacePropertyInFile() {
    #echo tttttttttttttttttttttttttttttttttttttttt
    escapedString=`escapeSpecialCharacters $2`
    #echo $2
    #echo -----------------------------------------
    sed -i "s/.*$1=.*/$1=$escapedString/" $3
}

function escapeSpecialCharacters {
    escapedString=$1
    echo $escapedString | sed 's/\//\\\//g'
}

#Get all the properties
targetDirectory=`getPropertyFromFile target.directory.path $propertyFile`
targetDirectory=`echo $targetDirectory | sed 's/\\\\/\//g'`
entityName=`getPropertyFromFile entity.name $propertyFile`
entityTableName=`getPropertyFromFile entity.table.name $propertyFile`
entityTableNameUpperCase=`echo "$entityTableName" | tr '[:lower:]' '[:upper:]'`
servicePort=`getPropertyFromFile service.port $propertyFile`
logDirectory=`getPropertyFromFile log.directory $propertyFile`
pomGroupId=`getPropertyFromFile pom.groupId $propertyFile`
pomVersion=`getPropertyFromFile pom.version $propertyFile`
swaggerTermsOfServiceUrl=`getPropertyFromFile swagger.termsOfServiceUrl $propertyFile`
swaggerContactName=`getPropertyFromFile swagger.contact.name $propertyFile`
swaggerContactUrl=`getPropertyFromFile swagger.contact.url $propertyFile`
swaggerContactEmail=`getPropertyFromFile swagger.contact.email $propertyFile`
swaggerLicense=`getPropertyFromFile swagger.license $propertyFile`
swaggerLicenseUrl=`getPropertyFromFile swagger.licenseUrl $propertyFile`

serviceJdbcDriver=`getPropertyFromFile $SERVICE_JDBC_DRIVER_PROPERTY_NAME $propertyFile`
serviceJdbcUrl=`getPropertyFromFile $SERVICE_JDBC_URL_PROPERTY_NAME $propertyFile`
serviceJdbcUsername=`getPropertyFromFile $SERVICE_JDBC_USERNAME_PROPERTY_NAME $propertyFile`
serviceJdbcPassword=`getPropertyFromFile $SERVICE_JDBC_PASSWORD_PROPERTY_NAME $propertyFile`

entityDTOName="$entityName$DTO_APPENDER"
entityVariableName=`echo $entityName | sed 's/^[A-Z]/\L&/'`
entityDTOVariableName="$entityVariableName$DTO_APPENDER"

projectName="`echo $entityVariableName | sed 's/[A-Z]/-\L&/'`-$SERVICE_APPENDER"
packageNameAppender=`echo $entityVariableName | sed 's/[A-Z]/.\L&/'`
packagesBaseDirectory=`echo $pomGroupId.$packageNameAppender | sed 's/\./\//g'`

echo '----------------------------------------------------------------------------------------------------'
echo '			Creating microservice with configurations'
echo '----------------------------------------------------------------------------------------------------'
echo targetDirectory = $targetDirectory
echo entityName = $entityName

echo entityVariableName = $entityVariableName
echo entityDTOName = $entityDTOName
echo entityDTOVariableName = $entityDTOVariableName
echo projectName = $projectName
echo packagesBaseDirectory = $packagesBaseDirectory
echo '----------------------------------------------------------------------------------------------------'

projectDirectory=$targetDirectory/$projectName

rm -rf $projectDirectory

#Create the directory structure
mkdir $projectDirectory
cp -R $SOURCE_USER_PROJECT_PATH/src $projectDirectory/
cp -R $SOURCE_USER_PROJECT_PATH/pom.xml $projectDirectory/
cp -R $SOURCE_USER_PROJECT_PATH/.gitignore $projectDirectory/
mv $projectDirectory/src/main $projectDirectory/src/main_temp

mainDirectory=$projectDirectory/src/main
mainTempDirectory=$projectDirectory/src/main_temp

javaSourceDirectory=$mainDirectory/java
resourceDirectory=$mainDirectory/resources

javaTempSourceDirectory=$mainTempDirectory/java
resourceTempDirectory=$mainTempDirectory/resources

mkdir -p $javaSourceDirectory/$packagesBaseDirectory
mkdir -p $resourceDirectory
mv $javaTempSourceDirectory/$USER_SERVICE_BASE_DIRECTORY/* $javaSourceDirectory/$packagesBaseDirectory
mv $resourceTempDirectory/ValidationMessages.properties $resourceDirectory
mv $resourceTempDirectory/log4j.properties $resourceDirectory
mv $resourceTempDirectory/static $resourceDirectory

#Rename all the files which contains USER_SERVICE_ENTITY_NAME word in the file name.
grep -rl $USER_SERVICE_ENTITY_NAME $mainDirectory | while read -r fileName ; do
    newFileName=`echo $fileName | sed 's/\(.*\)'$USER_SERVICE_ENTITY_NAME'/\1'$entityName'/g'`
    if [ $fileName != $newFileName ]
        then
        mv $fileName $newFileName
	fi
done

#Rename the package declaration, Class names, variable references, log text, table names.
grep -rl $USER_SERVICE_BASE_PACKAGE $mainDirectory | xargs sed -i "s/$USER_SERVICE_BASE_PACKAGE/packageDeclarationForReplacement/g"
grep -rl $USER_SERVICE_ENTITY_NAME $mainDirectory | xargs sed -i "s/$USER_SERVICE_ENTITY_NAME/$entityName/g"
grep -rl $USER_SERVICE_VARIABLE_NAME $mainDirectory | xargs sed -i "s/$USER_SERVICE_VARIABLE_NAME/$entityVariableName/g"
grep -rl system_$entityVariableName $mainDirectory | xargs sed -i "s/system_$entityVariableName/$entityTableName/g"
grep -rl $entityVariableName'_' $mainDirectory | xargs sed -i "s/${entityVariableName}_/${entityTableName}_/g"
grep -rl $USER_SERVICE_ENTITY_UPPER_CASE_NAME $mainDirectory | xargs sed -i "s/$USER_SERVICE_ENTITY_UPPER_CASE_NAME/$entityTableNameUpperCase/g"
grep -rl packageDeclarationForReplacement $mainDirectory | xargs sed -i "s/packageDeclarationForReplacement/$pomGroupId.$packageNameAppender/g"

#Replacements for pom.xml
sed -i "s/$USER_SERVICE_BASE_PACKAGE/packageDeclarationForReplacement/g" $projectDirectory/pom.xml
sed -i "s/<artifactId>$USER_SERVICE_VARIABLE_NAME-$SERVICE_APPENDER<\/artifactId>/<artifactId>$projectName<\/artifactId>/g" $projectDirectory/pom.xml
sed -i "s/<name>$USER_SERVICE_VARIABLE_NAME-$SERVICE_APPENDER<\/name>/<name>$projectName<\/name>/g" $projectDirectory/pom.xml
sed -i "s/<finalName>$USER_SERVICE_VARIABLE_NAME/<finalName>$entityVariableName/g" $projectDirectory/pom.xml
sed -i "s/packageDeclarationForReplacement/$pomGroupId.$packageNameAppender/g" $projectDirectory/pom.xml

#Replacements for application.properties
sed -i "s/$USER_SERVICE_ENTITY_NAME/$entityName/g" $resourceTempDirectory/application.properties
sed -i "s/\${$USER_SERVICE_VARIABLE_NAME/\${$packageNameAppender/g" $resourceTempDirectory/application.properties

replacePropertyInFile 'server.port' $servicePort $resourceTempDirectory/application.properties
replacePropertyInFile 'swagger.termsOfServiceUrl' $swaggerTermsOfServiceUrl $resourceTempDirectory/application.properties
replacePropertyInFile 'swagger.contact.name' $swaggerContactName $resourceTempDirectory/application.properties
replacePropertyInFile 'swagger.contact.url' $swaggerContactUrl $resourceTempDirectory/application.properties
replacePropertyInFile 'swagger.contact.email' $swaggerContactEmail $resourceTempDirectory/application.properties
replacePropertyInFile 'swagger.license' $swaggerLicense $resourceTempDirectory/application.properties
replacePropertyInFile 'swagger.licenseUrl' $swaggerLicenseUrl $resourceTempDirectory/application.properties

#Correct log4j configuration
sed -i "s/$entityVariableName-$SERVICE_APPENDER/$projectName/g" $resourceDirectory/log4j.properties

mv $resourceTempDirectory/application.properties $resourceDirectory
rm -rf $mainTempDirectory

#Create services.properties file
servicesPropertiesFile=$projectDirectory/$packageNameAppender'.service.properties'
echo '#--------------------------------------------------------------------------------------------------------------------------------------------------------------------------' >> $servicesPropertiesFile
echo '#															'$entityName' '$SERVICE_APPENDER': START' >> $servicesPropertiesFile
echo '#--------------------------------------------------------------------------------------------------------------------------------------------------------------------------' >> $servicesPropertiesFile
echo '#-----------Database configuration-----------' >> $servicesPropertiesFile
echo $packageNameAppender.$SERVICE_JDBC_DRIVER_PROPERTY_NAME=$serviceJdbcDriver >> $servicesPropertiesFile
echo $packageNameAppender.$SERVICE_JDBC_URL_PROPERTY_NAME=$serviceJdbcUrl >> $servicesPropertiesFile
echo $packageNameAppender.$SERVICE_JDBC_USERNAME_PROPERTY_NAME=$serviceJdbcUsername >> $servicesPropertiesFile
echo $packageNameAppender.$SERVICE_JDBC_PASSWORD_PROPERTY_NAME=$serviceJdbcPassword >> $servicesPropertiesFile
echo '#--------------------------------------------------------------------------------------------------------------------------------------------------------------------------' >> $servicesPropertiesFile
echo '#															'$entityName' '$SERVICE_APPENDER': END' >> $servicesPropertiesFile
echo '#--------------------------------------------------------------------------------------------------------------------------------------------------------------------------' >> $servicesPropertiesFile

echo '----------------------------------------------------------------------------------------------------'
echo Created Microservice \"$projectName\"
echo $projectDirectory
echo '----------------------------------------------------------------------------------------------------'