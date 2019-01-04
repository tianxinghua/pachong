dependencies {
    compile(

        "com.google.code.gson:gson:2.2.2",
        project(':project-common'),
        project(':openapi-ice'),
        project(':iog-common-product'),
           
    )
    testCompile group: 'junit', name: 'junit', version: '4.11'
}

jar {

    dependsOn copyToLib , copyResources
    exclude ("**/*.properties")
    def manifestCp = configurations.compile.files.collect  {
        File file = it
        "lib/${tmp}"
    }.join(' ')

    manifest {
        attributes 'Main-Class': 'com.shangpin.iog.${supplierName}.StartUp'
        attributes 'Class-Path': '. ./conf/  ' +  manifestCp
    }
}
